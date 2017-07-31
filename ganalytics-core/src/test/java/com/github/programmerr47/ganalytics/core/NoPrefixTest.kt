package com.github.programmerr47.ganalytics.core

import org.junit.Test

class NoPrefixTest : SingleWrapperTest() {

    @Test
    fun checkDefaultBehavior() {
        run(NoPrefixDefaultInterface::class) {
            assertEquals(Event("noprefixdefaultinterface", "method1")) { method1() }
            assertEquals(Event("noprefixdefaultinterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkDisablingForSingleMethod() {
        run(AnalyticsDisablingClassPrefixInterface::class) {
            assertEquals(Event("disablingclassprefixinterface", "disablingclassprefixinterface.method1")) { method1() }
            assertEquals(Event("disablingclassprefixinterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkDominatingNoPrefixOverHasPrefix() {
        run(DummyPrefixesInterface::class) {
            assertEquals(Event("dummyprefixesinterface", "method1")) { method1() }
            assertEquals(Event("dummyprefixesinterface", "method2")) { method2() }
        }
    }

    @HasPrefix(splitter = ".")
    interface AnalyticsDisablingClassPrefixInterface : SampleInterface {
        @NoPrefix override fun method2()
    }

    @NoPrefix
    @HasPrefix("dummy")
    interface DummyPrefixesInterface : SampleInterface
}