package com.github.programmerr47.ganalytics.core

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalyticsSingleWrapperTest {
    val testProvider: TestEventProvider = TestEventProvider()
    val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkDefaultBehavior() {
        val sampleI = wrapper.create(SampleInterface::class)

        sampleI.method1()
        assertEquals(Event("sampleinterface", "method1"), testProvider.lastEvent)

        sampleI.method2()
        assertEquals(Event("sampleinterface", "method2"), testProvider.lastEvent)
    }

    @Test
    fun checkCuttingOffAnalyticsPrefix() {
        val analyticsI = wrapper.create(AnalyticsInterface::class)

        analyticsI.method1()
        assertEquals(Event("interface", "method1"), testProvider.lastEvent)

        analyticsI.method2()
        assertEquals(Event("interface", "method2"), testProvider.lastEvent)
    }

    @Test
    fun checkPrefixOnWholeInterface() {
        val prefixInterface = wrapper.create(AnalyticsHasPrefixInterface::class)

        prefixInterface.method1()
        assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod1"), testProvider.lastEvent)

        prefixInterface.method2()
        assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod2"), testProvider.lastEvent)
    }
}
