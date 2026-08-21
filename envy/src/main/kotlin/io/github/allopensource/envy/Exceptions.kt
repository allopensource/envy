package io.github.allopensource.envy

open class EnvyException(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)

class EnvyConfigurationException(message: String, cause: Throwable? = null) :
    EnvyException(message, cause)

class EnvyLoaderException(
    message: String,
    cause: Throwable? = null
) : EnvyException(message, cause)

class EnvyMissingDefaultAndEnvVariablesException(
)