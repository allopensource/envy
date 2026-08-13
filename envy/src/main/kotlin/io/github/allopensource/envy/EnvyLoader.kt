package io.github.allopensource.envy

import kotlin.reflect.KClass

/**
 * This is the marker interface which supers any loaders generated for the classes annotated with @Envy.
 */
interface EnvyLoader<T : Any> {
    val type: KClass<T>

    fun load(): T
}