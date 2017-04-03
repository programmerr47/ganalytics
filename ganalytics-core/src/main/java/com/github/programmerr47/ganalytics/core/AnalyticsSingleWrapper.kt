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
        val methodPrefix = method.getAnnotation(HasPrefix::class.java)
        return if (methodPrefix != null) {
            val prefix = if (methodPrefix.name.isEmpty()) inputClassName else methodPrefix.name
            prefix + inputName
        } else {
            val classPrefix = clazz.getAnnotation(HasPrefix::class.java)
            return if (classPrefix != null) {
                val prefix = if (classPrefix.name.isEmpty()) inputClassName else classPrefix.name
                prefix + inputName
            } else {
                inputName
            }
        }
    }
}
