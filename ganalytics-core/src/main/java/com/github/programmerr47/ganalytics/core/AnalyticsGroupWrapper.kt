package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Proxy

class AnalyticsGroupWrapper(private val eventProvider: EventProvider) : AnalyticsWrapper {

    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { _, method, _ ->
            val requiredClazzAnnotations = listOf(HasPrefix::class, NoPrefix::class, Convention::class)
            val clazzAnnotations = requiredClazzAnnotations.mapNotNull { clazz.getAnnotation(it.java) }

            val defAnnotations = AnalyticsDefAnnotations(clazzAnnotations.toTypedArray())
            AnalyticsSingleWrapper(eventProvider, defAnnotations).create(method.returnType)
        } as T
    }
}
