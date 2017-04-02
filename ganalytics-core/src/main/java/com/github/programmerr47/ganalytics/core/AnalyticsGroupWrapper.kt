package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class AnalyticsGroupWrapper(eventProvider: EventProvider) {
    val analyticsWrapper = AnalyticsWrapper(eventProvider)

    fun <T : Any> create(kClass: KClass<T>) = create(kClass.java)

    @Suppress("unchecked_cast")
    fun <T> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { proxy, method, args ->
            System.out.println("Method " + method.name + " invoked")
            analyticsWrapper.create(method.returnType) as T
        } as T
    }
}
