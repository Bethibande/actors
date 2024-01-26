package com.bethibande.actors.cache

import kotlin.reflect.KClass

class ObjectStore {

    private val values = mutableMapOf<KClass<*>, MutableMap<Any, Any>>()

    fun put(key: Any, value: Any) {
        val map = values.computeIfAbsent(value::class) { mutableMapOf() }
        map[key] = value
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> get(key: Any, type: KClass<T>): T? {
        return values[type]?.get(key) as T
    }

    inline fun <reified T: Any> get(key: Any): T? = get(key, T::class)

    inline fun <reified T: Any> getOrPut(key: Any, fn: (Any) -> T): T = get<T>(key) ?: run {
        val value = fn.invoke(key)
        put(key, value)

        value
    }

}