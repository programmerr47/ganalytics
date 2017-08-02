package com.github.programmerr47.ganalytics.core

import org.junit.Test

class HasPrefixTest : SingleWrapperTest() {

    @Test
    fun checkPrefixOnWholeInterface() {
        run(AnalyticsHasPrefixInterface::class) {
            assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod1")) { method1() }
            assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod2")) { method2() }
        }
    }

    @Test
    fun checkPrefixOnSingleMethod() {
        run(AnalyticInterfaceWithPrefix::class) {
            assertEquals(Event("analyticinterfacewithprefix", "method1")) { method1() }
            assertEquals(Event("analyticinterfacewithprefix", "analyticinterfacewithprefixmethod2")) { method2() }
        }
    }

    @Test
    fun checkSpecificNamePrefixOnWholeInterface() {
        run(SpecificNamePrefixInterface::class) {
            assertEquals(Event("specificnameprefixinterface", "prefixmethod1")) { method1() }
            assertEquals(Event("specificnameprefixinterface", "prefixmethod2")) { method2() }
        }
    }

    @Test
    fun checkSpecificNamePrefixOnSingleMethod() {
        run(InterfaceWithSpecificNamePrefixOnMethod::class) {
            assertEquals(Event("interfacewithspecificnameprefixonmethod", "prefixmethod1")) { method1() }
            assertEquals(Event("interfacewithspecificnameprefixonmethod", "method2")) { method2() }
        }
    }

    @Test
    fun checkComplexPrefixNamedInterface() {
        run(ComplexNamedPrefixesInterface::class) {
            assertEquals(Event("complexnamedprefixesinterface", "complexnamedprefixesinterfacemethod1")) { method1() }
            assertEquals(Event("complexnamedprefixesinterface", "interfacemethod2")) { method2() }
            assertEquals(Event("complexnamedprefixesinterface", "methodmethod3")) { method3() }
        }
    }

    @Test
    fun checkComplexPrefixWithSplitterInterface() {
        run(SplitterInterface::class) {
            assertEquals(Event("splitterinterface", "splitterinterfacemethod1")) { method1() }
            assertEquals(Event("splitterinterface", "splitterinterface_-_method2")) { method2() }
            assertEquals(Event("splitterinterface", "splitterinterface::method3")) { method3() }
        }
    }

    interface AnalyticInterfaceWithPrefix : SampleInterface {
        @HasPrefix override fun method2()
    }

    @HasPrefix("prefix")
    interface SpecificNamePrefixInterface : SampleInterface

    interface InterfaceWithSpecificNamePrefixOnMethod : SampleInterface {
        @HasPrefix("prefix") override fun method1()
    }

    @HasPrefix("interface")
    interface ComplexNamedPrefixesInterface : SampleInterface{
        @HasPrefix override fun method1()
        @HasPrefix("method") fun method3()
    }
}