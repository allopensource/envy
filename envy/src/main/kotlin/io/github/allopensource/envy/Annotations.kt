package io.github.allopensource.envy

/**
 * "The" annotation. Any classes annotated with @Envy are scanned at compile time and are eligible for a Loader to be generated.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Envied()


@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class EnviedDefault(
    val defaultValue : String = ""
)

/**
 * Overrides the environment variable name for a property.
 * When omitted, the property name is used as the environment variable name.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class EnviedName(
    val value: String
)