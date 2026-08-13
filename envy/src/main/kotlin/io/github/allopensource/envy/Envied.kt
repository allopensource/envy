package io.github.allopensource.envy

/**
 * "The" annotation. Any classes annotated with @Envy are scanned at compile time and are eligible for a Loader to be generated.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Envied()