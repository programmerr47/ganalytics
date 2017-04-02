package com.github.programmerr47.ganalytics.core

import java.util.concurrent.ConcurrentHashMap

class CacheWrapper(val origin: AnalyticsWrapper) : AnalyticsWrapper {
    private val cache = ConcurrentHashMap<Class<out Any>, Any>()

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T = cache.getOrPut(clazz, { origin.create(clazz) }) as T
}
