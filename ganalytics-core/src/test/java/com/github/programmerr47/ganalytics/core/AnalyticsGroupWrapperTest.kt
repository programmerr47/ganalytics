package com.github.programmerr47.ganalytics.core

import com.github.programmerr47.ganalytics.core.NamingConventions.LOWER_SNAKE_CASE
import com.github.programmerr47.ganalytics.core.NamingConventions.UPPER_CAMEL_CASE
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
        run(PrefixGroupInterface::class) {
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

    @Test
    fun checkSpecificInterfacePrefixBeatsGroupOne() {
        run(PrefixInterfaceAndInnerPrefix::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "sampleinterface_method1")) { method1() }
                assertEquals(Event("sampleinterface", "sampleinterface_method2")) { method2() }
            }
            with(hasPrefixInterface()) {
                assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod1")) { method1() }
                assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod2")) { method2() }
            }
        }
    }

    @Test
    fun checkSpecificNoPrefixBeatsGroupPrefix() {
        run(PrefixInterfaceButInnerNoPrefix::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "sampleinterface_method1")) { method1() }
                assertEquals(Event("sampleinterface", "sampleinterface_method2")) { method2() }
            }
            with(noPrefixInterface()) {
                assertEquals(Event("noprefixdefaultinterface", "method1")) { method1() }
                assertEquals(Event("noprefixdefaultinterface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkGroupNoPrefixDominationOverSpecificHasPrefixes() {
        run(NoPrefixInterfaceButInnerHasPrefix::class) {
            with(hasPrefixInterface()) {
                assertEquals(Event("hasprefixinterface", "method1")) { method1() }
                assertEquals(Event("hasprefixinterface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkConventionOnGroup() {
        run(ConventionGroupInterface::class) {
            with(sampleInterface()) {
                assertEquals(Event("sample_interface", "method1")) { method1() }
                assertEquals(Event("sample_interface", "method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "method1")) { method1() }
                assertEquals(Event("interface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkSpecificConventionBeatsGroupOne() {
        run(ConventionInterfaceAndInnerConvention::class) {
            with(sampleInterface()) {
                assertEquals(Event("SampleInterface", "Method1")) { method1() }
                assertEquals(Event("SampleInterface", "Method2")) { method2() }
            }
            with(conventionInterface()) {
                assertEquals(Event("lib_convention_interface", "simple_method")) { simpleMethod() }
            }
        }
    }

    internal interface SampleGroupInterface {
        fun sampleInterface(): SampleInterface
        fun analyticsInterface(): AnalyticsInterface
    }

    @HasPrefix(splitter = "_")
    internal interface PrefixGroupInterface : SampleGroupInterface

    @HasPrefix(splitter = "_")
    internal interface PrefixInterfaceAndInnerPrefix {
        fun sampleInterface(): SampleInterface
        fun hasPrefixInterface(): AnalyticsHasPrefixInterface
    }

    @HasPrefix(splitter = "_")
    internal interface PrefixInterfaceButInnerNoPrefix {
        fun sampleInterface(): SampleInterface
        fun noPrefixInterface(): NoPrefixDefaultInterface
    }

    @NoPrefix
    internal interface NoPrefixInterfaceButInnerHasPrefix {
        fun hasPrefixInterface(): AnalyticsHasPrefixInterface
    }

    @Convention(LOWER_SNAKE_CASE)
    internal interface ConventionGroupInterface : SampleGroupInterface

    @Convention(UPPER_CAMEL_CASE)
    internal interface ConventionInterfaceAndInnerConvention {
        fun sampleInterface(): SampleInterface
        fun conventionInterface(): AnalyticsLibConventionInterface
    }
}