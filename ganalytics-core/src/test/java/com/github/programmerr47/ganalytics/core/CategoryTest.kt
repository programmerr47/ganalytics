package com.github.programmerr47.ganalytics.core

import org.junit.Test


class CategoryTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

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
}