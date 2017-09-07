package com.github.programmerr47.ganalytics.core.wrappers

import com.github.programmerr47.ganalytics.core.*
import com.github.programmerr47.ganalytics.core.NamingConventions.*
import org.junit.Test

class AnalyticsGroupWrapperTest : GroupWrapperTest() {

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
    fun checkCategoryOnMethod() {
        run(SampleGroupInterfaceWithSpecificCategory::class) {
            with(sampleInterface()) {
                assertEquals(Event("test", "method1")) { method1() }
                assertEquals(Event("test", "method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "method1")) { method1() }
                assertEquals(Event("interface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkInterfaceCategoryBeatsGroupMethodCategory() {
        run(CategoryInterfaceWithInnerCategory::class) {
            with(sampleInterface()) {
                assertEquals(Event("group1", "method1")) { method1() }
                assertEquals(Event("group1", "method2")) { method2() }
            }
            with(categoryInterface()) {
                assertEquals(Event("analyticscategory", "method1")) { method1() }
                assertEquals(Event("analyticscategory", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkHasPrefixOnMethod() {
        run(SampleGroupInterfaceWithSpecificHasPrefix::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "sampleinterface_method1")) { method1() }
                assertEquals(Event("sampleinterface", "sampleinterface_method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "method1")) { method1() }
                assertEquals(Event("interface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkSpecificInterfacePrefixBeatsGroupAndMethod() {
        run(PrefixInterfaceWithPrefixMethodAndInnerPrefix::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "sampleinterface_method1")) { method1() }
                assertEquals(Event("sampleinterface", "sampleinterface_method2")) { method2() }
            }
            with(samplePrefixInterface()) {
                assertEquals(Event("sampleinterface", "sampleinterface...method1")) { method1() }
                assertEquals(Event("sampleinterface", "sampleinterface...method2")) { method2() }
            }
            with(hasPrefixInterface()) {
                assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod1")) { method1() }
                assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod2")) { method2() }
            }
            with(hasPrefixInterfaceWithOneMorePrefix()) {
                assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod1")) { method1() }
                assertEquals(Event("hasprefixinterface", "hasprefixinterfacemethod2")) { method2() }
            }
        }
    }

    @Test
    fun checkNoPrefixOnMethodButGroupHas() {
        run(PrefixGroupInterfaceWithNoPrefix::class) {
            with(sampleInterface()) {
                assertEquals(Event("sampleinterface", "method1")) { method1() }
                assertEquals(Event("sampleinterface", "method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "interface_method1")) { method1() }
                assertEquals(Event("interface", "interface_method2")) { method2() }
            }
        }
    }

    @Test
    fun checkNoPrefixOnMethodBeatsHasPrefixOnSpecificInterface() {
        run(InterfaceWithNoPrefixMethodButInnerHasPrefix::class) {
            with(hasPrefixInterface()) {
                assertEquals(Event("hasprefixinterface", "method1")) { method1() }
                assertEquals(Event("hasprefixinterface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkConventionOnMethod() {
        run(InterfaceWithConventionMethod::class) {
            with(sampleInterface()) {
                assertEquals(Event("SampleInterface", "Method1")) { method1() }
                assertEquals(Event("SampleInterface", "Method2")) { method2() }
            }
            with(analyticsInterface()) {
                assertEquals(Event("interface", "method1")) { method1() }
                assertEquals(Event("interface", "method2")) { method2() }
            }
        }
    }

    @Test
    fun checkSpecificConventionBeatsMethodAndInterface() {
        run(ConventionInterfaceWithConventionMethodAndInnerOne::class) {
            with(sampleInterface()) {
                assertEquals(Event("SampleInterface", "Method1")) { method1() }
                assertEquals(Event("SampleInterface", "Method2")) { method2() }
            }
            with(sampleConventionInterface()) {
                assertEquals(Event("Sample_interface", "Method1")) { method1() }
                assertEquals(Event("Sample_interface", "Method2")) { method2() }
            }
            with(conventionInterface()) {
                assertEquals(Event("lib_convention_interface", "simple_method")) { simpleMethod() }
            }
            with(conventionInterfaceWithOneMoreConvention()) {
                assertEquals(Event("lib_convention_interface", "simple_method")) { simpleMethod() }
            }
        }
    }

    @Test
    fun checkValProperties() {
        run(PropertiesGroupInterface::class) {
            with(sample) {
                assertEquals(Event("sampleinterface", "method1")) { method1() }
                assertEquals(Event("sampleinterface", "method2")) { method2() }
            }
            with(convention) {
                assertEquals(Event("lib_convention_interface", "simple_method")) { simpleMethod() }
            }
        }
    }

    @Test
    fun checkAnnotatedValProperties() {
        run(PropertiesAnnotationInterface::class) {
            with(sample) {
                assertEquals(Event("SAMPLE", "method1")) { method1() }
                assertEquals(Event("SAMPLE", "method2")) { method2() }
            }
            with(sample2) {
                assertEquals(Event("lib_convention_interface", "simple_method")) { simpleMethod() }
            }
            with(sample3) {
                assertEquals(Event("sampleinterface", "sampleinterfacemethod1")) { method1() }
                assertEquals(Event("sampleinterface", "sampleinterfacemethod2")) { method2() }
            }
        }
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

    internal interface SampleGroupInterfaceWithSpecificCategory : SampleGroupInterface {
        @Category("test") override fun sampleInterface(): SampleInterface
    }

    internal interface CategoryInterfaceWithInnerCategory {
        @Category("group1") fun sampleInterface(): SampleInterface
        @Category("group2") fun categoryInterface(): SpecificCategoryWithAnalyticsPrefixInterface
    }

    internal interface SampleGroupInterfaceWithSpecificHasPrefix : SampleGroupInterface {
        @HasPrefix(splitter = "_") override fun sampleInterface(): SampleInterface
    }

    @HasPrefix(splitter = "_")
    internal interface PrefixInterfaceWithPrefixMethodAndInnerPrefix {
        fun sampleInterface(): SampleInterface
        @HasPrefix(splitter = "...") fun samplePrefixInterface(): SampleInterface
        fun hasPrefixInterface(): AnalyticsHasPrefixInterface
        @HasPrefix(splitter = "...") fun hasPrefixInterfaceWithOneMorePrefix(): AnalyticsHasPrefixInterface
    }

    @HasPrefix(splitter = "_")
    internal interface PrefixGroupInterfaceWithNoPrefix : SampleGroupInterface {
        @NoPrefix override fun sampleInterface(): SampleInterface
    }

    internal interface InterfaceWithNoPrefixMethodButInnerHasPrefix {
        @NoPrefix
        fun hasPrefixInterface(): AnalyticsHasPrefixInterface
    }

    internal interface InterfaceWithConventionMethod : SampleGroupInterface {
        @Convention(UPPER_CAMEL_CASE) override fun sampleInterface(): SampleInterface
    }

    @Convention(UPPER_CAMEL_CASE)
    internal interface ConventionInterfaceWithConventionMethodAndInnerOne {
        fun sampleInterface(): SampleInterface
        @Convention(UPPER_SNAKE_CASE) fun sampleConventionInterface(): SampleInterface
        fun conventionInterface(): AnalyticsLibConventionInterface
        @Convention(UPPER_SNAKE_CASE) fun conventionInterfaceWithOneMoreConvention(): AnalyticsLibConventionInterface
    }

    internal interface PropertiesGroupInterface {
        val sample: SampleInterface
        val convention: AnalyticsLibConventionInterface
    }

    internal interface PropertiesAnnotationInterface {
        @Category("SAMPLE") val sample: SampleInterface
        @Convention(UPPER_SNAKE_CASE) val sample2: AnalyticsLibConventionInterface
        @HasPrefix
        val sample3: SampleInterface
    }
}