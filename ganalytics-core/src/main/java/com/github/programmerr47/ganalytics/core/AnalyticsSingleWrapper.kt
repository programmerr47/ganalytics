package com.github.programmerr47.ganalytics.core

import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Proxy

class AnalyticsSingleWrapper(private val eventProvider: EventProvider) : AnalyticsWrapper {
    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { proxy, method, args ->
            System.out.println("Method " + method.name + " invoked")
            val category = clazz.simpleName.toLowerCase().removePrefix("analytics")
            val action = applyPrefix(method.name, category, method, clazz)
            val event = Event(category, action)
            eventProvider.provide(event)
        } as T
    }

    private fun applyPrefix(input: String, default: String, vararg elements: AnnotatedElement): String {
        return applyPrefix(input, default, elements.map { it.getAnnotation(HasPrefix::class.java) }.firstOrNull { it != null })
    }

    private fun applyPrefix(input: String, default: String, hasPrefix: HasPrefix?): String {
        return if (hasPrefix == null) input else applyPrefix(input, default, hasPrefix.name)
    }

    private fun applyPrefix(input: String, default: String, prefix: String): String {
        return (if (prefix.isEmpty()) default else prefix) + input
    }
}
