package com.github.programmerr47.ganalytics.core

import org.junit.Assert
import kotlin.reflect.KClass

interface AnalyticsWrapperTest {
    val testProvider: TestEventProvider
    val wrapper: AnalyticsWrapper

    fun <T : Any> run(clazz: KClass<T>, block: T.() -> Unit) = wrapper.create(clazz).run(block)

    fun assertEquals(event: Event, interfaceMethod: () -> Unit) {
        interfaceMethod()
        Assert.assertEquals(event, testProvider.lastEvent)
    }
}