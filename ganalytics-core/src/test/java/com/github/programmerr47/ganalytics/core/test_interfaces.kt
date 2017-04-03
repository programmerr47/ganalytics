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

@HasPrefix("prefix")
interface SpecificNamePrefixInterface {
    fun method1()
    fun method2()
}

interface InterfaceWithSpecificNamePrefixOnMethod {
    @HasPrefix("prefix") fun method1()
    fun method2()
}

@HasPrefix("interface")
interface ComplexNamedPrefixesInterface {
    @HasPrefix fun method1()
    fun method2()
    @HasPrefix("method") fun method3()
}

@HasPrefix(splitter = "_-_")
interface SplitterInterface {
    @HasPrefix fun method1()
    fun method2()
    @HasPrefix(splitter = "::") fun method3()
}

@NoPrefix
interface NoPrefixDefaultInterface {
    @NoPrefix fun method1()
    fun method2()
}

@HasPrefix(splitter = ".")
interface AnalyticsDisablingClassPrefixInterface {
    fun method1()
    @NoPrefix fun method2()
}

@NoPrefix
@HasPrefix("dummy")
interface DummyPrefixesInterface {
    fun method1()
    fun method2()
}