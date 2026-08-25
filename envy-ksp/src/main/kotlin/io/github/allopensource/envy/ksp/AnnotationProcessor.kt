package io.github.allopensource.envy.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference

class AnnotationProcessor(
    val codeGenerator: CodeGenerator,
    val metaFileEntries: MutableSet<String> = mutableSetOf(),
    var serviceFileGenerated: Boolean = false) : SymbolProcessor {

    private val envyAnnotation = "io.github.allopensource.envy.Envied"

    private val loaderSourceTemplate =
"""package io.github.allopensource.envy
    
import {fqClassName}
    
class {loaderName} : EnvyLoader<{enviedClassName}> {
    
    override val type = {enviedClassName}::class
          {loadFunction}
   }
"""


    override fun process(resolver: Resolver): List<KSAnnotated> {

        runCatching {

            val enviedClasses = resolver
                .getSymbolsWithAnnotation(envyAnnotation)
                .filterIsInstance<KSClassDeclaration>()

            for (enviedClass in enviedClasses) {

                val enviedClassName = enviedClass.simpleName.asString()
                val enviedClassPackage = enviedClass.packageName.asString()
                val enviedClassFQName =
                    enviedClass.qualifiedName?.asString() ?: "${enviedClassPackage}.${enviedClassName}"
                val loaderName = "EnvyLoaderFor${enviedClassName}"

                val loadFunctionAsString = generateLoadFunctionAsString(enviedClass)
                val loaderSourceAsString = loaderSourceTemplate
                    .replace("{loadFunction}", loadFunctionAsString)
                    .replace("{loaderName}", loaderName)
                    .replace("{enviedClassName}", enviedClassName)
                    .replace("{fqClassName}", enviedClassFQName)

                val envyLoaderSourceFile = codeGenerator.createNewFile(
                    dependencies = Dependencies(false),
                    packageName = "io.github.allopensource.envy",
                    fileName = loaderName
                )

                envyLoaderSourceFile.writer().use { writer ->
                    writer.write(loaderSourceAsString)
                }

                metaFileEntries.add("io.github.allopensource.envy.$loaderName")

            }
        }.onFailure {
            throw EnvyConfigurationException("Failed to generate envy loader. $it", it)
        }
            return emptyList()
    }

    override fun finish() {
        runCatching {

            if (!serviceFileGenerated) {
                val metaFile = codeGenerator.createNewFile(
                    dependencies = Dependencies.ALL_FILES,
                    packageName = "",
                    fileName = "META-INF/services/io.github.allopensource.envy.EnvyLoader", // fileName must match the FQCN of the Interface
                    extensionName = ""
                )

                metaFile.writer().use { writer ->
                    metaFileEntries.forEach {
                        writer.appendLine(it)
                    }
                }

            }
        }.onFailure {
            throw EnvyConfigurationException("Failed to generate envy service locator file in META-INF. $it", it)
        }.onSuccess {
            serviceFileGenerated = true
        }
    }

    fun generateLoadFunctionAsString(enviedClass: KSClassDeclaration) : String {

        val className = enviedClass.simpleName.asString()
        val props =  enviedClass.getAllProperties().joinToString(",\n                    ") { property ->

            val name = property.simpleName.asString()
            val envVarName = property.annotations
                .firstOrNull { it.shortName.asString() == "EnviedName" }
                ?.arguments
                ?.firstOrNull { it.name?.asString() == "value" }
                ?.value as? String
                ?: name
            val declaration = property.type.resolve().declaration
            val isEnum = declaration is KSClassDeclaration && declaration.classKind == ClassKind.ENUM_CLASS
            val type = property.type.resolve().declaration.qualifiedName?.asString()
            val isNullable = property.type.resolve().isMarkedNullable
            val defaultValue = property.annotations
                .firstOrNull { it.shortName.asString() == "EnviedDefault" }
                ?.arguments
                ?.firstOrNull { it.name?.asString() == "defaultValue" }
                ?.value as? String

            val rhs = when (type) {
                "kotlin.String" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName") ?: "$defaultValue" """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")"""
                    } else
                        """System.getenv("$envVarName")!!"""

                "kotlin.Int" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toInt() ?: ${defaultValue.toInt()} """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toInt()"""
                    } else
                        """System.getenv("$envVarName")!!.toInt()"""

                "kotlin.Long" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toLong() ?: $defaultValue """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toLong()"""
                    } else
                        """System.getenv("$envVarName")!!.toLong()"""

                "kotlin.Boolean" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toBoolean() ?: ${defaultValue.toBoolean()} """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toBoolean()"""
                    } else
                        """System.getenv("$envVarName")!!.toBoolean()"""

                "kotlin.Double" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toDouble() ?: ${defaultValue.toDouble()} """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toDouble()"""
                    } else
                        """System.getenv("$envVarName")!!.toDouble()"""

                "kotlin.Float" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toFloat() ?: $defaultValue """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toFloat()"""
                    } else
                        """System.getenv("$envVarName")!!.toFloat()"""

                "kotlin.Short" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toShort() ?: ${defaultValue.toShort()} """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toShort()"""
                    } else
                        """System.getenv("$envVarName")!!.toShort()"""

                "kotlin.Char" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toCharArray()?.firstOrNull() ?: '${defaultValue.toCharArray().firstOrNull()}' """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toCharArray()?.firstOrNull()"""
                    } else
                        """System.getenv("$envVarName")!!.toCharArray().first()"""

                "kotlin.Byte" ->
                    if(!defaultValue.isNullOrEmpty()) {
                        """System.getenv("$envVarName")?.toByte() ?: ${defaultValue.toByte()} """
                    }
                    else if (isNullable) {
                        """System.getenv("$envVarName")?.toByte()"""
                    } else
                        """System.getenv("$envVarName")!!.toByte()"""

                else -> {
                    if (isEnum) {
                        if(!defaultValue.isNullOrEmpty()) {
                            """$type.valueOf(System.getenv("$envVarName") ?: "$defaultValue" )"""
                        }
                        else if (isNullable) {
                            """if (System.getenv("$envVarName") == null) null else $type.valueOf(System.getenv("$envVarName"))"""
                        } else
                            """$type.valueOf(System.getenv("$envVarName")!!)"""
                    }
                    else {
                        throw EnvyConfigurationException("Unsupported property type encountered while generating Loader : $type")
                    }
                }
            }

            "$name = $rhs"
        }

       return $$"""
    override fun load(): $$className {
        return runCatching {
            $$className(
                $$props
            )
        }.getOrElse { e ->
            throw EnvyLoaderException(
                "Unable to create ${type.simpleName}. $e",
                e
            )
        }
    }"""
    }

}