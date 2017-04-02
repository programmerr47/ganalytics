package com.github.programmerr47.ganalytics.core

import java.lang.reflect.Proxy

class AnalyticsGroupWrapper(val singleWrapper: AnalyticsWrapper) : AnalyticsWrapper {
    @Suppress("unchecked_cast")
    override fun <T : Any> create(clazz: Class<T>): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf<Class<*>>(clazz)) { proxy, method, args ->
            System.out.println("Method " + method.name + " invoked")
            singleWrapper.create(method.returnType) as T
        } as T
    }
}
