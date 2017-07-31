package com.github.programmerr47.ganalytics.core

import org.junit.Test

class ActionTest : SingleWrapperTest() {

    @Test
    fun checkUselessEmptyAnnotation() {
        run(DummyActionInterface::class) {
            assertEquals(Event("dummyactioninterface", "method1")) { method1() }
            assertEquals(Event("dummyactioninterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkSpecificCategoryName() {
        run(SpecificActionInterface::class) {
            assertEquals(Event("specificactioninterface", "function1")) { method1() }
            assertEquals(Event("specificactioninterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkSpecificActionWithSpecificPrefix() {
        run(SpecificActionWithHasPrefixInterface::class) {
            assertEquals(Event("specificactionwithhasprefixinterface", "specific_function1")) { method1() }
            assertEquals(Event("specificactionwithhasprefixinterface", "method2")) { method2() }
        }
    }

    interface DummyActionInterface : SampleInterface {
        @Action override fun method1()
    }

    interface SpecificActionInterface : SampleInterface {
        @Action("function1") override fun method1()
    }

    interface SpecificActionWithHasPrefixInterface : SampleInterface {
        @HasPrefix("specific", splitter = "_") @Action("function1") override fun method1()
    }
}