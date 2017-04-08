package com.github.programmerr47.ganalytics.core

import org.junit.Assert
import kotlin.reflect.KClass

interface AnalyticsWrapperTest {
    val testProvider: TestEventProvider
    val wrapper: AnalyticsWrapper
}

inline fun <T : Any> AnalyticsWrapperTest.run(clazz: KClass<T>, block: T.() -> Unit) = wrapper.create(clazz).run(block)

inline fun AnalyticsWrapperTest.assertEquals(event: Event, interfaceMethod: () -> Unit) {
    interfaceMethod()
    Assert.assertEquals(event, testProvider.lastEvent)
}