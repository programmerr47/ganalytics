package com.github.programmerr47.ganalytics.core

import org.junit.Test

class AnalyticsGroupWrapperTest : AnalyticsWrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()
    override val wrapper = AnalyticsGroupWrapper(compose(EventProvider { System.out.println(it) }, testProvider))

    @Test
    fun checkDefaultBehavior() {
        run(SampleGroupInterface::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "method1")) { method1() }
                assertEquals(Event("sampleinterface", "method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "method1")) { method1() }
                assertEquals(Event("interface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkHasPrefixOnGroup() {
        run(SampleGroupInterfaceWithPrefix::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "sampleinterface_method1")) { method1() }
                assertEquals(Event("sampleinterface", "sampleinterface_method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "interface_method1")) { method1() }
                assertEquals(Event("interface", "interface_method2")) { method2() }
            }
        }
    }

    internal interface SampleGroupInterface {
        fun sampleInterface(): SampleInterface
        fun analyticsInterface(): AnalyticsInterface
    }

    @HasPrefix(splitter = "_")
    internal interface SampleGroupInterfaceWithPrefix {
        fun sampleInterface(): SampleInterface
        fun analyticsInterface(): AnalyticsInterface
    }
}