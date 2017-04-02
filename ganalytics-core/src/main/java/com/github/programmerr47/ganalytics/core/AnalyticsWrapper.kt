package com.github.programmerr47.ganalytics.core

import kotlin.reflect.KClass

interface AnalyticsWrapper {
    fun <T : Any> create(kClass: KClass<T>) = create(kClass.java)
    fun <T : Any> create(clazz: Class<T>): T
}
