package com.github.programmerr47.ganalytics.core

interface SampleInterface {
    fun method1()
    fun method2()
}

interface AnalyticsInterface : SampleInterface

@HasPrefix
interface AnalyticsHasPrefixInterface : SampleInterface

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