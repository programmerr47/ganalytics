package com.github.programmerr47.ganalytics.core.annotations

import com.github.programmerr47.ganalytics.core.*
import com.github.programmerr47.ganalytics.core.wrappers.SingleWrapperTest
import com.github.programmerr47.ganalytics.core.wrappers.assertEquals
import com.github.programmerr47.ganalytics.core.wrappers.run
import org.junit.Test

class CategoryTest : SingleWrapperTest() {

    @Test
    fun checkUselessEmptyAnnotation() {
        run(DummyCategoryInterface::class) {
            assertEquals(Event("dummycategoryinterface", "method1")) { method1() }
            assertEquals(Event("dummycategoryinterface", "method2")) { method2() }
        }
    }

    @Test
    fun checkSpecificCategoryName() {
        run(AnalyticsSpecificCategoryInterface::class) {
            assertEquals(Event("category", "method1")) { method1() }
            assertEquals(Event("category", "method2")) { method2() }
        }
    }

    @Test
    fun checkCategoryNotRemovingAnalyticsPrefix() {
        run(SpecificCategoryWithAnalyticsPrefixInterface::class) {
            assertEquals(Event("analyticscategory", "method1")) { method1() }
            assertEquals(Event("analyticscategory", "method2")) { method2() }
        }
    }

    @Category
    interface DummyCategoryInterface : SampleInterface

    @Category("category")
    interface AnalyticsSpecificCategoryInterface : SampleInterface
}