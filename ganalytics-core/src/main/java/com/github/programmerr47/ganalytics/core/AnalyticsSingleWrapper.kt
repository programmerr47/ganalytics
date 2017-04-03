package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Method
import java.lang.reflect.Proxy

class AnalyticsSingleWrapper(private val eventProvider: EventProvider) : AnalyticsWrapper {
    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { proxy, method, args ->
            System.out.println("Method " + method.name + " invoked")
            val category = clazz.simpleName.toLowerCase().removePrefix("analytics")
            val action = applyPrefix(clazz, method, category, method.name)
            val event = Event(category, action)
            eventProvider.provide(event)
        } as T
    }

    fun applyPrefix(clazz: Class<out Any>, method: Method, inputClassName: String, inputName: String): String {
        val hasPrefix = clazz.getAnnotation(HasPrefix::class.java)
        if (hasPrefix != null) {
            return inputClassName + inputName
        } else {
            val hasMethodPrefix = method.getAnnotation(HasPrefix::class.java)
            return if (hasMethodPrefix != null) inputClassName + inputName else inputName
        }
    }
}
