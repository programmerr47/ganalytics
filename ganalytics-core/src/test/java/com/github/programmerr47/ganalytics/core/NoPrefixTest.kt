package com.github.programmerr47.ganalytics.core

import org.junit.Assert
import org.junit.Test

class NoPrefixTest {
    val testProvider: TestEventProvider = TestEventProvider()
    val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkDefaultBehavior() {
        val noPrefix = wrapper.create(NoPrefixDefaultInterface::class)

        noPrefix.method1()
        Assert.assertEquals(Event("noprefixdefaultinterface", "method1"), testProvider.lastEvent)

        noPrefix.method2()
        Assert.assertEquals(Event("noprefixdefaultinterface", "method2"), testProvider.lastEvent)
    }

    @Test
    fun checkDisablingForSingleMethod() {
        val disablingPrefix = wrapper.create(AnalyticsDisablingClassPrefixInterface::class)

        disablingPrefix.method1()
        Assert.assertEquals(Event("disablingclassprefixinterface", "disablingclassprefixinterface.method1"), testProvider.lastEvent)

        disablingPrefix.method2()
        Assert.assertEquals(Event("disablingclassprefixinterface", "method2"), testProvider.lastEvent)
    }

    @Test
    fun checkDominatingNoPrefixOverHasPrefix() {
        val dummyInterface = wrapper.create(DummyPrefixesInterface::class)

        dummyInterface.method1()
        Assert.assertEquals(Event("dummyprefixesinterface", "method1"), testProvider.lastEvent)

        dummyInterface.method2()
        Assert.assertEquals(Event("dummyprefixesinterface", "method2"), testProvider.lastEvent)
    }
}