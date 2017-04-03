package com.github.programmerr47.ganalytics.core

import org.junit.Assert
import org.junit.Test

class HasPrefixTest {
    val testProvider: TestEventProvider = TestEventProvider()
    val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkPrefixOnWholeInterface() {
        val prefixInterface = wrapper.create(AnalyticsHasPrefixInterface::class)

        prefixInterface.method1()
        Assert.assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod1"), testProvider.lastEvent)

        prefixInterface.method2()
        Assert.assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod2"), testProvider.lastEvent)
    }

    @Test
    fun checkPrefixOnSingleMethod() {
        val interfaceWithPrefixes = wrapper.create(AnalyticInterfaceWithPrefix::class)

        interfaceWithPrefixes.method1()
        Assert.assertEquals(Event("analyticinterfacewithprefix", "method1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        Assert.assertEquals(Event("analyticinterfacewithprefix", "analyticinterfacewithprefixmethod2"), testProvider.lastEvent)
    }

    @Test
    fun checkSpecificNamePrefixOnWholeInterface() {
        val interfaceWithPrefixes = wrapper.create(SpecificNamePrefixInterface::class)

        interfaceWithPrefixes.method1()
        Assert.assertEquals(Event("specificnameprefixinterface", "prefixmethod1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        Assert.assertEquals(Event("specificnameprefixinterface", "prefixmethod2"), testProvider.lastEvent)
    }

    @Test
    fun checkSpecificNamePrefixOnSingleMethod() {
        val interfaceWithPrefixes = wrapper.create(InterfaceWithSpecificNamePrefixOnMethod::class)

        interfaceWithPrefixes.method1()
        Assert.assertEquals(Event("interfacewithspecificnameprefixonmethod", "prefixmethod1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        Assert.assertEquals(Event("interfacewithspecificnameprefixonmethod", "method2"), testProvider.lastEvent)
    }

    @Test
    fun checkComplexPrefixNamedInterface() {
        val interfaceWithPrefixes = wrapper.create(ComplexNamedPrefixesInterface::class)

        interfaceWithPrefixes.method1()
        Assert.assertEquals(Event("complexnamedprefixesinterface", "complexnamedprefixesinterfacemethod1"), testProvider.lastEvent)

        interfaceWithPrefixes.method2()
        Assert.assertEquals(Event("complexnamedprefixesinterface", "interfacemethod2"), testProvider.lastEvent)

        interfaceWithPrefixes.method3()
        Assert.assertEquals(Event("complexnamedprefixesinterface", "methodmethod3"), testProvider.lastEvent)
    }
}