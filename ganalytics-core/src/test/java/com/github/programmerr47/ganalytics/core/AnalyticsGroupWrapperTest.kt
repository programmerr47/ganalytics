package com.github.programmerr47.ganalytics.core

import org.junit.Assert
import org.junit.Test


class AnalyticsGroupWrapperTest {
    val testProvider: TestEventProvider = TestEventProvider()
    val wrapper = AnalyticsGroupWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkDefaultBehavior() {
        val sampleGI = wrapper.create(SampleGroupInterface::class)

        val sampleI = sampleGI.sampleInterface()

        sampleI.method1()
        Assert.assertEquals(Event("sampleinterface", "method1"), testProvider.lastEvent)

        sampleI.method2()
        Assert.assertEquals(Event("sampleinterface", "method2"), testProvider.lastEvent)

        val analyticsI = sampleGI.analyticsInterface()

        analyticsI.method1()
        Assert.assertEquals(Event("interface", "method1"), testProvider.lastEvent)

        analyticsI.method2()
        Assert.assertEquals(Event("interface", "method2"), testProvider.lastEvent)
    }

    internal interface SampleGroupInterface {
        fun sampleInterface(): SampleInterface
        fun analyticsInterface(): AnalyticsInterface
    }
}