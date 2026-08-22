package io.github.allopensource.envy

class EnvyLoaderException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)