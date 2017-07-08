package com.github.programmerr47.ganalytics.core

interface SampleInterface {
    fun method1()
    fun method2()
}

interface AnalyticsInterface : SampleInterface

@HasPrefix
interface AnalyticsHasPrefixInterface : SampleInterface

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

@HasPrefix(splitter = "_-_")
interface SplitterInterface : SampleInterface {
    @HasPrefix override fun method1()
    @HasPrefix(splitter = "::") fun method3()
}

@NoPrefix
interface NoPrefixDefaultInterface : SampleInterface {
    @NoPrefix override fun method1()
}

@HasPrefix(splitter = ".")
interface AnalyticsDisablingClassPrefixInterface : SampleInterface {
    @NoPrefix override fun method2()
}

@NoPrefix
@HasPrefix("dummy")
interface DummyPrefixesInterface : SampleInterface

@Category
interface DummyCategoryInterface : SampleInterface

@Category("category")
interface AnalyticsSpecificCategoryInterface : SampleInterface

@Category("analyticscategory")
interface SpecificCategoryWithAnalyticsPrefixInterface : SampleInterface

interface DummyActionInterface : SampleInterface {
    @Action override fun method1()
}

interface SpecificActionInterface : SampleInterface {
    @Action("function1") override fun method1()
}

interface SpecificActionWithHasPrefixInterface : SampleInterface {
    @HasPrefix("specific", splitter = "_") @Action("function1") override fun method1()
}

interface SingleParameterMethodInterface {
    fun intMethod(param: Int)
    fun strMethod(param: String)
    fun dummyClassMethod(param: DummyClass)
    fun dummyDataClassMethod(param: DummyDataClass)
    fun dummyEnumClassMethod(param: DummyEnum)
}

interface MoreTwoParameterMethodInterface {
    fun method1()
    fun method2(param1: String, param2: Int, param3: Long)
}

@Category("interface")
interface TwoParameterMethodStringAndNumberInterface {
    fun intStrMethod(param1: Int, param2: String)
    fun strIntMethod(param1: String, param2: Int)
    fun strLongMethod(param1: String, param2: Long)
    fun strNumberMethod(param1: String, param2: Number)
}

@Category("interface")
interface TwoParameterMethodNoNumberInterface {
    fun noNumberMethod(param1: DummyEnum, param2: String)
}

@Category("interface")
interface TwoParameterMethodOneNumberInterface {
    fun default(param1: String, param2: Number)
    fun reversed(param1: Number, param2: String)
    fun custom(param1: DummyDataClass, param2: Number)
}

@Category("interface")
interface TwoParameterBothNumberInterface {
    fun method(param1: Number, param2: Number)
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

interface Bad___Named_Interface {}

interface `kotlin style super 4357 __ bad named interface` {}