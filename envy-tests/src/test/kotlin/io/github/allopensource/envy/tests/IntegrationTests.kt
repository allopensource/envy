package io.github.allopensource.envy.tests

import io.github.allopensource.envy.Envy
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.io.File
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
        assertTrue(generatedFiles == 3, "Three distinct environment files should be generated")

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

        val string = primitiveConfig.string
        val int = primitiveConfig.int
        val long = primitiveConfig.long
        val double = primitiveConfig.double
        val float = primitiveConfig.float
        val boolean = primitiveConfig.boolean
        val byte = primitiveConfig.byte
        val char = primitiveConfig.char
        val short = primitiveConfig.short
        val nullString = primitiveConfig.nullString
        val nullInt = primitiveConfig.nullInt
        val nullLong = primitiveConfig.nullLong
        val nullDouble = primitiveConfig.nullDouble
        val nullFloat = primitiveConfig.nullFloat
        val nullBoolean = primitiveConfig.nullBoolean
        val nullByte = primitiveConfig.nullByte
        val nullChar = primitiveConfig.nullChar
        val nullShort = primitiveConfig.nullShort

        assertTrue(string == "I am a string.")
        assertTrue(int == 1)
        assertTrue(long == 1L)
        assertTrue(double == 1.0)
        assertTrue(float == 1.0F)
        assertTrue(boolean)
        assertTrue(byte == Byte.MAX_VALUE)
        assertTrue(char == 'a')
        assertTrue(short == 1.toShort())
        assertTrue(nullString == "I am not a null string.")
        assertNull(nullInt)
        assertNull(nullLong)
        assertNull(nullDouble)
        assertNull(nullFloat)
        assertNull(nullBoolean)
        assertNull(nullByte)
        assertNull(nullChar)
        assertNull(nullShort)
    }

    @Test
    fun `IllegalStateException for missing loaders`() {
        assertThrows<IllegalStateException> { Envy.load<NotEnviedConfig>() }
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

}