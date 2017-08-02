package com.github.programmerr47.ganalytics.core

interface SampleInterface {
    fun method1()
    fun method2()
}

interface AnalyticsInterface : SampleInterface

@HasPrefix
interface AnalyticsHasPrefixInterface : SampleInterface

@HasPrefix(splitter = "_-_")
interface SplitterInterface : SampleInterface {
    @HasPrefix override fun method1()
    @HasPrefix(splitter = "::") fun method3()
}

@NoPrefix
interface NoPrefixDefaultInterface : SampleInterface {
    @NoPrefix override fun method1()
}

@Category("analyticscategory")
interface SpecificCategoryWithAnalyticsPrefixInterface : SampleInterface

@Convention(NamingConventions.LOWER_SNAKE_CASE)
interface AnalyticsLibConventionInterface {
    fun simpleMethod()
}