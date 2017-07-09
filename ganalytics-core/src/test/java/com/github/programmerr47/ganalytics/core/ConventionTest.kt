package com.github.programmerr47.ganalytics.core

import org.junit.Test

class ConventionTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkUselessEmptyAnnotation() {
        run(AnalyticsConventionInterface::class, {
            assertEquals(Event("conventioninterface", "simplemethod"), { simpleMethod() })
        })
    }

    @Test
    fun checkWorkingAnnotation() {
        run(AnalyticsLibConventionInterface::class, {
            assertEquals(Event("lib_convention_interface", "simple_method"), { simpleMethod() })
        })
    }

    @Test
    fun checkConventionOnSpecificCategory() {
        run(ConventionWithCategoryInterface::class, {
            assertEquals(Event("MySuPerCaT__ego_RY", "Simple_method"), { simpleMethod() })
        })
    }

    @Test
    fun checkConventionOnSpecificAction() {
        run(ConventionWithActionInterface::class, {
            assertEquals(Event("Convention_with_action_interface", "sIiiII__m_plemETHod"), { simpleMethod() })
        })
    }
}

