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

    @Test
    fun checkPrefixOnSingleMethod() {
        val interfaceWithPrefixes = wrapper.create(AnalyticInterfaceWithPrefix::class)

        interfaceWithPrefixes.method1()
        assertEquals(Event("analyticinterfacewithprefix", "method1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        assertEquals(Event("analyticinterfacewithprefix", "analyticinterfacewithprefixmethod2"), testProvider.lastEvent)
    }

    @Test
    fun checkSpecificNamePrefixOnWholeInterface() {
        val interfaceWithPrefixes = wrapper.create(SpecificNamePrefixInterface::class)

        interfaceWithPrefixes.method1()
        assertEquals(Event("specificnameprefixinterface", "prefixmethod1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        assertEquals(Event("specificnameprefixinterface", "prefixmethod2"), testProvider.lastEvent)
    }

    @Test
    fun checkSpecificNamePrefixOnSingleMethod() {
        val interfaceWithPrefixes = wrapper.create(InterfaceWithSpecificNamePrefixOnMethod::class)

        interfaceWithPrefixes.method1()
        assertEquals(Event("interfacewithspecificnameprefixonmethod", "prefixmethod1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        assertEquals(Event("interfacewithspecificnameprefixonmethod", "method2"), testProvider.lastEvent)
    }

    @Test
    fun checkComplexPrefixNamedInterface() {
        val interfaceWithPrefixes = wrapper.create(ComplexNamedPrefixesInterface::class)

        interfaceWithPrefixes.method1()
        assertEquals(Event("complexnamedprefixesinterface", "complexnamedprefixesinterfacemethod1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        assertEquals(Event("complexnamedprefixesinterface", "interfacemethod2"), testProvider.lastEvent)

        interfaceWithPrefixes.method3()
        assertEquals(Event("complexnamedprefixesinterface", "methodmethod3"), testProvider.lastEvent)
    }
}
