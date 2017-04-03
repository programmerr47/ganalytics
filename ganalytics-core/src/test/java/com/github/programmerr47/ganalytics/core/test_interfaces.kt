package com.github.programmerr47.ganalytics.core

interface SampleInterface {
    fun method1()
    fun method2()
}

interface AnalyticsInterface {
    fun method1()
    fun method2()
}

@HasPrefix
interface AnalyticsHasPrefixInterface {
    fun method1()
    fun method2()
}

interface AnalyticInterfaceWithPrefix {
    fun method1()
    @HasPrefix fun method2()
}