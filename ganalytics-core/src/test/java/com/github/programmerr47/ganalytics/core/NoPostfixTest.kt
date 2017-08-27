package com.github.programmerr47.ganalytics.core

import org.junit.Test

class NoPostfixTest : SingleWrapperTest() {

    @Test
    fun checkDefaultBehavior() {
        run(NoPostfixDefaultInterface::class) {
            assertEquals(Event("nopostfixdefaultinterface", "method1")) { method1() }
            assertEquals(Event("nopostfixdefaultinterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkDisablingForSingleMethod() {
        run(AnalyticsDisablingClassPostfixInterface::class) {
            assertEquals(Event("disablingclasspostfixinterface", "method1.end")) { method1() }
            assertEquals(Event("disablingclasspostfixinterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkDominatingNoPostfixOverHasPostfix() {
        run(DummyPostfixesInterface::class) {
            assertEquals(Event("dummypostfixesinterface", "method1")) { method1() }
            assertEquals(Event("dummypostfixesinterface", "method2")) { method2() }
        }
    }

    @NoPostfix
    interface NoPostfixDefaultInterface : SampleInterface {
        @NoPostfix override fun method1()
    }

    @HasPostfix("end", splitter = ".")
    interface AnalyticsDisablingClassPostfixInterface : SampleInterface {
        @NoPostfix override fun method2()
    }

    @NoPostfix
    @HasPostfix("dummy")
    interface DummyPostfixesInterface : SampleInterface
}