package com.github.programmerr47.ganalytics.core

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsWrapperTest {
    @Test
    fun checkReturnName() {
        val sampleI = AnalyticsWrapper().create(SampleInterface::class)
        assertEquals("method1", sampleI.method1())
        assertEquals("method2", sampleI.method2())
    }

    internal interface SampleInterface {
        fun method1(): String
        fun method2(): String
    }
}
