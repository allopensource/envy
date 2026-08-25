package io.github.allopensource.envy.tests

import io.github.allopensource.envy.Envied
import io.github.allopensource.envy.EnviedDefault
import io.github.allopensource.envy.EnviedName

@Envied
class PrimitiveConfig (
    val string: String,
    val int: Int,
    val long: Long,
    val double: Double,
    val float: Float,
    val boolean: Boolean,
    val byte: Byte,
    val char: Char,
    val short: Short,
    val nullString: String?,
    val nullInt: Int?,
    val nullLong: Long?,
    val nullDouble: Double?,
    val nullFloat: Float?,
    val nullBoolean: Boolean?,
    val nullByte: Byte?,
    val nullChar: Char?,
    val nullShort: Short?,
)

@Envied
class EmptyConfig

class NotEnviedConfig

@Envied
class TwoConstructorConfig(
    val int: Int,
)
{
    constructor(int: Int, string: String): this(int)
}

@Envied
class PrimitiveConfigWithDefaultValuesAndEnvVariables(
    @EnviedDefault("I am a default string")
    val string: String,
    @EnviedDefault("2")
    val int: Int,
    @EnviedDefault("2L")
    val long: Long,
    @EnviedDefault("2.0")
    val double: Double,
    @EnviedDefault("2.0F")
    val float: Float,
    @EnviedDefault("false")
    val boolean: Boolean,
    @EnviedDefault("-128")
    val byte: Byte,
    @EnviedDefault("z")
    val char: Char,
    @EnviedDefault("2")
    val short: Short,
)


@Envied
class PrimitiveConfigWithDefaultValues(
    @EnviedDefault("I am a default string")
    val sstring: String,
    @EnviedDefault("2")
    val iint: Int,
    @EnviedDefault("2L")
    val llong: Long,
    @EnviedDefault("2.0")
    val ddouble: Double,
    @EnviedDefault("2.0F")
    val ffloat: Float,
    @EnviedDefault("false")
    val bboolean: Boolean,
    @EnviedDefault("-128")
    val bbyte: Byte,
    @EnviedDefault("z")
    val cchar: Char,
    @EnviedDefault("2")
    val sshort: Short,
)
@Envied
class PrimitiveConfigWithIncorrectEnvValue(
    val incorrectInt: Int
)

@Envied
class MissingDefaultAndEnvVariables(
    val iint: Int
)

@Envied
class ConfigWithCustomNames(
    @EnviedName("CUSTOM_STRING")
    val string: String,

    @EnviedName("CUSTOM_INT")
    val int: Int,
)

@Envied
class ConfigWithEnums(
    @EnviedName("LOG_LEVEL")
    val logLevel: LOGLEVEL
){
    enum class LOGLEVEL{
        INFO,
        WARN
    }
}
