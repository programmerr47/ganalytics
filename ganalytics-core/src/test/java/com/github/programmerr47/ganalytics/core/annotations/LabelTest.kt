package com.github.programmerr47.ganalytics.core.annotations

import com.github.programmerr47.ganalytics.core.*
import com.github.programmerr47.ganalytics.core.wrappers.SingleWrapperTest
import com.github.programmerr47.ganalytics.core.wrappers.assertEquals
import com.github.programmerr47.ganalytics.core.wrappers.run
import org.junit.Test
import java.lang.reflect.UndeclaredThrowableException

class LabelTest : SingleWrapperTest() {

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

    @Test
    fun checkArgumentWithCustomConverter() {
        run(ConverterInterface::class) {
            assertEquals(Event("converterinterface", "method", "5.hello")) { method(DummyDataClass(5, "hello")) }
        }
    }

    @Test(expected = ClassCastException::class)
    fun checkArgumentWithWrongConverter() {
        run(WrongConverterInterface::class) { method(DummyClass(5, "hello")) }
    }

    @Test
    fun checkArgumentWithConverterOfParentClass() {
        run(ParentClassConverterForChildInterface::class) {
            assertEquals(Event("parentclassconverterforchildinterface", "method", "5.olleh")) { method(DummyReversedClass(5, "hello")) }
        }
    }

    @Test(expected = UndeclaredThrowableException::class)
    fun checkBrokenConverterError() {
        run(BrokenConverterInterface::class) { method(DummyDataClass(5, "hello")) }
    }

    interface SimpleLabelInterface {
        fun method(@Label param1: String, param2: Number)
    }

    interface NumberLabelInterface {
        fun method1(@Label param1: Number, param2: Number)
        fun method2(param1: Number, @Label param2: Number)
    }

    interface NumberLabelStringNotInterface {
        fun method(@Label param1: Number, param2: String)
    }

    interface TwoLabelsInterface {
        fun method(@Label param1: Number, @Label param2: Number)
    }

    interface ConverterInterface {
        fun method(@Label(DummyDataClassConverter::class) param: DummyDataClass)
    }

    interface WrongConverterInterface {
        fun method(@Label(DummyDataClassConverter::class) param: DummyClass)
    }

    interface ParentClassConverterForChildInterface {
        fun method(@Label(DummyClassConverter::class) param: DummyReversedClass)
    }

    interface BrokenConverterInterface {
        fun method(@Label(BrokenConverter::class) param: DummyDataClass)
    }

    object DummyDataClassConverter : TypedLabelConverter<DummyDataClass> {
        override fun convertTyped(label: DummyDataClass) = label.run { "$id.$name" }
    }

    object DummyClassConverter : TypedLabelConverter<DummyClass> {
        override fun convertTyped(label: DummyClass) = label.run { "$id.$name" }
    }

    class BrokenConverter(val arg: Int) : TypedLabelConverter<DummyDataClass> {
        override fun convertTyped(label: DummyDataClass) = "$arg, ${label.name}"
    }
}