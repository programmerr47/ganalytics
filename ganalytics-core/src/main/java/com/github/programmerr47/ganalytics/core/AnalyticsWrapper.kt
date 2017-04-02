package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class AnalyticsWrapper(val eventProvider: EventProvider) {
    fun <T : Any> create(kClass: KClass<T>) = create(kClass.java)

    @Suppress("unchecked_cast")
    fun <T> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { proxy, method, args ->
            System.out.println("Method " + method.name + " invoked")
            val event = Event(clazz.simpleName, method.name)
            eventProvider.provide(event)
        } as T
    }
}
