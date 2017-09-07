package com.github.programmerr47.ganalytics.core.annotations

import com.github.programmerr47.ganalytics.core.*
import com.github.programmerr47.ganalytics.core.wrappers.SingleWrapperTest
import com.github.programmerr47.ganalytics.core.wrappers.assertEquals
import com.github.programmerr47.ganalytics.core.wrappers.run
import org.junit.Test

class ConventionTest : SingleWrapperTest() {

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

    @Convention
    interface AnalyticsConventionInterface {
        fun simpleMethod()
    }

    @Convention(NamingConventions.UPPER_SNAKE_CASE)
    @Category("MySuPerCaT__ego_RY")
    interface ConventionWithCategoryInterface {
        fun simpleMethod()
    }

    @Convention(NamingConventions.UPPER_SNAKE_CASE)
    interface ConventionWithActionInterface {
        @Action("sIiiII__m_plemETHod") fun simpleMethod()
    }
}

