package com.github.programmerr47.ganalytics.core

import org.junit.Test

class ActionTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkUselessEmptyAnnotation() {
        run(DummyActionInterface::class) {
            assertEquals(Event("dummyactioninterface", "method1")) { method1() }
            assertEquals(Event("dummyactioninterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkSpecificCategoryName() {
        run(SpecificActionInterface::class) {
            assertEquals(Event("specificactioninterface", "function1")) { method1() }
            assertEquals(Event("specificactioninterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkSpecificActionWithSpecificPrefix() {
        run(SpecificActionWithHasPrefixInterface::class) {
            assertEquals(Event("specificactionwithhasprefixinterface", "specific_function1")) { method1() }
            assertEquals(Event("specificactionwithhasprefixinterface", "method2")) { method2() }
        }
    }
}