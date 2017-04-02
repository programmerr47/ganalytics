package com.github.programmerr47.ganalytics.core

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsWrapperTest {
    @Test
    fun checkDefaultBehavior() {
        val testProvider = TestEventProvider()
        val sampleI = AnalyticsWrapper(compose(EventProvider { System.out.println(it) }, testProvider)).create(SampleInterface::class)

        sampleI.method1()
        assertEquals("SampleInterface", testProvider.lastEvent.category)
        assertEquals("method1", testProvider.lastEvent.action)

        sampleI.method2()
        assertEquals("SampleInterface", testProvider.lastEvent.category)
        assertEquals("method2", testProvider.lastEvent.action)
    }

    internal interface SampleInterface {
        fun method1()
        fun method2()
    }
}
