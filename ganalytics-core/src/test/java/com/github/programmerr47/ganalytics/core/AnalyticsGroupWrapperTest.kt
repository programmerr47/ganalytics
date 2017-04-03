package com.github.programmerr47.ganalytics.core

import org.junit.Test

class AnalyticsGroupWrapperTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsGroupWrapper(AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider)))

    @Test
    fun checkDefaultBehavior() {
        run(SampleGroupInterface::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "method1")) { method1() }
                assertEquals(Event("sampleinterface", "method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "method1")) { method1() }
                assertEquals(Event("interface", "method2")) { method2() }
            }
        }
    }

    internal interface SampleGroupInterface {
        fun sampleInterface(): SampleInterface
        fun analyticsInterface(): AnalyticsInterface
    }
}