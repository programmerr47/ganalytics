package com.github.programmerr47.ganalytics.core

import org.junit.Test


class LabelTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsSingleWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkSimpleLabelTest() {
        run(SimpleLabelInterface::class) {
            assertEquals(Event("simplelabelinterface", "method", "1", 2)) { method("1", 2) }
        }
    }

    @Test
    fun checkCorrectingChoosingBetweenNumbersTest() {
        run(NumberLabelInterface::class) {
            assertEquals(Event("numberlabelinterface", "method1", "1", 2)) { method1(1, 2) }
            assertEquals(Event("numberlabelinterface", "method2", "2", 1)) { method2(1, 2) }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkErrorOnLabelNumberButOtherNotNumber() {
        run(NumberLabelStringNotInterface::class) { method(1, "hello") }
    }

    @Test(expected = IllegalArgumentException::class)
    fun checkErrorWhenUseTwoLabelArguments() {
        run(TwoLabelsInterface::class) { method(1, 2) }
    }
}