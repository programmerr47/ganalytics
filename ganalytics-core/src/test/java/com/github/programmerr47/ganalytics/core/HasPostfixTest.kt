package com.github.programmerr47.ganalytics.core

import org.junit.Test

class HasPostfixTest : SingleWrapperTest() {

    @Test
    fun checkSpecificNamePostfixOnWholeInterface() {
        run(SpecificNamePostfixInterface::class) {
            assertEquals(Event("specificnamepostfixinterface", "method1postfix")) { method1() }
            assertEquals(Event("specificnamepostfixinterface", "method2postfix")) { method2() }
        }
    }

    @Test
    fun checkSpecificNamePrefixOnSingleMethod() {
        run(InterfaceWithSpecificNamePostfixOnMethod::class) {
            assertEquals(Event("interfacewithspecificnamepostfixonmethod", "method1postfix")) { method1() }
            assertEquals(Event("interfacewithspecificnamepostfixonmethod", "method2")) { method2() }
        }
    }

    @Test
    fun checkPostfixWithSplitterInterface() {
        run(PostfixSplitterInterface::class) {
            assertEquals(Event("postfixsplitterinterface", "method1test")) { method1() }
            assertEquals(Event("postfixsplitterinterface", "method2:::")) { method2() }
            assertEquals(Event("postfixsplitterinterface", "method3-ttt")) { method3() }
        }
    }

    @Test
    fun checkPostfixPrefixInterface() {
        run(PostfixPrefixInterface::class) {
            assertEquals(Event("postfixprefixinterface", "lolmethod1-mock")) { method1() }
            assertEquals(Event("postfixprefixinterface", "postfixprefixinterfacemethod2ok")) { method2() }
        }
    }

    @HasPostfix("postfix")
    interface SpecificNamePostfixInterface : SampleInterface

    interface InterfaceWithSpecificNamePostfixOnMethod : SampleInterface {
        @HasPostfix("postfix") override fun method1()
    }

    @HasPostfix("ttt", splitter = "-")
    interface PostfixSplitterInterface {
        @HasPostfix("test") fun method1()
        @HasPostfix("", splitter = ":::") fun method2()
        fun method3()
    }

    @HasPrefix
    @HasPostfix("ok")
    interface PostfixPrefixInterface {
        @HasPostfix("mock", splitter = "-") @HasPrefix("lol") fun method1()
        fun method2()
    }
}