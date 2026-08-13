package io.github.allopensource.envy.tests

import io.github.allopensource.envy.Envied

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
    val nullShort: Short?
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
