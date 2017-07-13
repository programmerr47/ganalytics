package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Proxy

class AnalyticsGroupWrapper(private val eventProvider: EventProvider) : AnalyticsWrapper {

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { _, method, _ ->
            val requiredAnnotations = listOf(HasPrefix::class, NoPrefix::class, Convention::class, Category::class)
            val actualAnnotations = requiredAnnotations.mapNotNull {
                method.getAnnotation(it.java) ?: clazz.getAnnotation(it.java)
            }

            val defAnnotations = AnalyticsDefAnnotations(actualAnnotations.toTypedArray())
            AnalyticsSingleWrapper(eventProvider, defAnnotations).create(method.returnType)
        } as T
    }
}
