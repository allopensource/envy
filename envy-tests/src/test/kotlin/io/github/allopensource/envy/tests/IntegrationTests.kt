package io.github.allopensource.envy.tests

import io.github.allopensource.envy.Envy
import io.github.allopensource.envy.EnvyLoaderException
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class IntegrationTests {

    @Test
    fun `Distinct envied files are generated for distinct Configuration classes`() {

        val buildDir = File(System.getProperty("buildDir"))

        val generatedFiles = buildDir.resolve(
            "generated/ksp/main/kotlin/io/github/allopensource/envy"

        ).listFiles()?.count()
        assertNotNull(generatedFiles, "Distinct environment files should be generated")
        assertTrue(generatedFiles == 8, "Eight distinct environment files should be generated")

    }

    @Test
    fun `Envied file is not empty`() {
        val buildDir = File(System.getProperty("buildDir"))
        val generated = buildDir.resolve("generated/ksp/main/kotlin/io/github/allopensource/envy/EnvyLoaderForPrimitiveConfig.kt")
        val content = generated.readText()
        assertTrue(content.isNotBlank(), "Envied file should be not empty")
    }

    @Test
    fun `Envied file is generated for empty constructor`() {
        val buildDir = File(System.getProperty("buildDir"))
        val generated = buildDir.resolve("generated/ksp/main/kotlin/io/github/allopensource/envy/EnvyLoaderForEmptyConfig.kt")
        val content = generated.readText()
        assertTrue(content.isNotBlank(), "Envied file should be not empty")
    }

    @Test
    fun `Envied values are available in Runtime`() {

        val primitiveConfig = Envy.load<PrimitiveConfig>()

        assertTrue(primitiveConfig.string == "I am a string.")
        assertTrue(primitiveConfig.int == 1)
        assertTrue(primitiveConfig.long == 1L)
        assertTrue(primitiveConfig.double == 1.0)
        assertTrue(primitiveConfig.float == 1.0F)
        assertTrue(primitiveConfig.boolean)
        assertTrue(primitiveConfig.byte == Byte.MAX_VALUE)
        assertTrue(primitiveConfig.char == 'a')
        assertTrue(primitiveConfig.short == 1.toShort())
        assertTrue(primitiveConfig.nullString == "I am not a null string.")
        assertNull(primitiveConfig.nullInt)
        assertNull(primitiveConfig.nullLong)
        assertNull(primitiveConfig.nullDouble)
        assertNull(primitiveConfig.nullFloat)
        assertNull(primitiveConfig.nullBoolean)
        assertNull(primitiveConfig.nullByte)
        assertNull(primitiveConfig.nullChar)
        assertNull(primitiveConfig.nullShort)
    }

    @Test
    fun `EnvyLoaderException for missing loaders`() {
        assertThrows<EnvyLoaderException> { Envy.load<NotEnviedConfig>() }
    }

    @Test
    fun `Envied values are available in Runtime for primary constructor`() {
        val twoConstructorConfig =  Envy.load<TwoConstructorConfig>()
        val int = twoConstructorConfig.int
        assertTrue(int == 1)
    }

    @Test
    fun `Only ony config class instance is created by EnvyLoaders`() {
        val primitiveConfigFirst = Envy.load<PrimitiveConfig>()
        val primitiveConfigSecond = Envy.load<PrimitiveConfig>()
        assertTrue(primitiveConfigFirst == primitiveConfigSecond)
    }

    @Test
    fun `environment variables overrides default values` () {
        val primitiveConfig = Envy.load<PrimitiveConfigWithDefaultValuesAndEnvVariables>()
        assertTrue(primitiveConfig.string == "I am a string.")
        assertTrue(primitiveConfig.int == 1)
        assertTrue(primitiveConfig.long == 1L)
        assertTrue(primitiveConfig.double == 1.0)
        assertTrue(primitiveConfig.float == 1.0F)
        assertTrue(primitiveConfig.boolean)
        assertTrue(primitiveConfig.byte == Byte.MAX_VALUE)
        assertTrue(primitiveConfig.char == 'a')
    }

    @Test
    fun `default values` () {
        val primitiveConfig = Envy.load<PrimitiveConfigWithDefaultValues>()
        assertTrue(primitiveConfig.sstring == "I am a default string")
        assertTrue(primitiveConfig.iint == 2)
        assertTrue(primitiveConfig.llong == 2L)
        assertTrue(primitiveConfig.ddouble == 2.0)
        assertTrue(primitiveConfig.ffloat == 2.0F)
        assertTrue(primitiveConfig.sshort == 2.toShort())
        assertFalse (primitiveConfig.bboolean)
        assertTrue(primitiveConfig.bbyte == Byte.MIN_VALUE)
        assertTrue(primitiveConfig.cchar == 'z')
    }


    @Test
    fun `EnvyLoaderException for missing default and env variables`() {
        assertThrows<EnvyLoaderException> {
            Envy.load<MissingDefaultAndEnvVariables>()
        }
    }

    @Test
    fun `EnvyLoaderException for incorrect env values`() {
        assertThrows<EnvyLoaderException> {
            Envy.load<PrimitiveConfigWithIncorrectEnvValue>()
        }
    }

    @Test
    fun `custom environment variable names are resolved via EnviedName`() {
        val config = Envy.load<ConfigWithCustomNames>()
        assertTrue(config.string == "Custom string.")
        assertTrue(config.int == 100)
    }

}