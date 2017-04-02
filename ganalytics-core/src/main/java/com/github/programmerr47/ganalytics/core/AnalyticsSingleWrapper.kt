package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Proxy

class AnalyticsSingleWrapper(private val eventProvider: EventProvider) : AnalyticsWrapper {
    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { proxy, method, args ->
            System.out.println("Method " + method.name + " invoked")
            val category = clazz.simpleName.toLowerCase().removePrefix("analytics")
            val event = Event(category, method.name)
            eventProvider.provide(event)
        } as T
    }
}
