package io.github.allopensource.envy

import java.util.ServiceLoader
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * This class loads to the runtime all the Loaders generated at the compile time. The load() method is then called to return the appropriate loader. The load()
 * can be called as many times as needed as it returns the same object.
 */

object Envy {

    private val loaders  = ServiceLoader
        .load(EnvyLoader::class.java)
        .associateBy { it.type }

    private val configCache = ConcurrentHashMap<KClass<*>, Any>()

    @PublishedApi
    internal fun <T : Any> load(type: KClass<T>): T {
        val loader = loaders[type]
            ?: throw EnvyConfigurationException("No loader found for $type")

        @Suppress("UNCHECKED_CAST")
        return configCache.computeIfAbsent(type){
            loader.load()
        } as T
    }

    inline fun <reified T : Any> load(): T =
        load(T::class)
}