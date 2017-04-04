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