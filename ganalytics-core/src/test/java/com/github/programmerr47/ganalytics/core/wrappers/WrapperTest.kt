package com.github.programmerr47.ganalytics.core.wrappers

import com.github.programmerr47.ganalytics.core.Event
import com.github.programmerr47.ganalytics.core.TestEventProvider
import org.junit.Assert
import kotlin.reflect.KClass

interface WrapperTest {
    val testProvider: TestEventProvider
}

inline fun <T : Any> WrapperTest.run(wrapper: AnalyticsWrapper, clazz: KClass<T>, block: T.() -> Unit) =
        wrapper.create(clazz).run(block)

inline fun WrapperTest.assertEquals(event: Event, interfaceMethod: () -> Unit) {
    interfaceMethod()
    Assert.assertEquals(event, testProvider.lastEvent)
}

interface StableWrapperTest : WrapperTest {
    val wrapper: AnalyticsWrapper
}

inline fun <T : Any> StableWrapperTest.run(clazz: KClass<T>, block: T.() -> Unit) = run(wrapper, clazz, block)

abstract class SingleWrapperTest : StableWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper: AnalyticsSingleWrapper = AnalyticsSingleWrapper(testProvider)
}

abstract class GroupWrapperTest : StableWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper: AnalyticsGroupWrapper = AnalyticsGroupWrapper(testProvider)
}