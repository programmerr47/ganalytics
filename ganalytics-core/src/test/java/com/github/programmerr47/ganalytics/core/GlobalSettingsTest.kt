package com.github.programmerr47.ganalytics.core

import org.junit.Test
import kotlin.reflect.KClass

class GlobalSettingsTest : WrapperTest {
    override val testProvider: TestEventProvider = TestEventProvider()

    @Test
    fun checkCuttingOffAnalyticsPrefix() {
        arrayOf(
                GanalyticsSettings { cutOffAnalyticsClassPrefix = false } to "analyticsinterface",
                GanalyticsSettings { cutOffAnalyticsClassPrefix = true } to "interface",
                GanalyticsSettings() to "interface")
                .forEach {
                    run(it.first, AnalyticsInterface::class) {
                        assertEquals(Event(it.second, "method1")) { method1() }
                        assertEquals(Event(it.second, "method2")) { method2() }
                    }
                }
    }

    @Test
    fun checkGlobalSplitter() {
        val settings = GanalyticsSettings { prefixSplitter = "^_^" }
        run(settings, AnalyticsInterface::class) {
            assertEquals(Event("interface", "method1")) { method1() }
            assertEquals(Event("interface", "method2")) { method2() }
        }
        run(settings, AnalyticsHasPrefixInterface::class) {
            assertEquals(Event("hasprefixinterface", "hasprefixinterface^_^method1")) { method1() }
            assertEquals(Event("hasprefixinterface", "hasprefixinterface^_^method1")) { method1() }
        }
        run(settings, SplitterInterface::class) {
            assertEquals(Event("splitterinterface", "splitterinterface^_^method1")) { method1() }
            assertEquals(Event("splitterinterface", "splitterinterface_-_method2")) { method2() }
            assertEquals(Event("splitterinterface", "splitterinterface::method3")) { method3() }
        }
    }

    @Test
    fun checkGlobalConvention() {
        val settings = GanalyticsSettings { namingConvention = testConvention() }
        run(settings, SampleInterface::class) {
            assertEquals(Event("s_a_m_p_l_e_I_n_t_e_r_f_a_c_e", "m_e_t_h_o_d_1")) { method1() }
            assertEquals(Event("s_a_m_p_l_e_I_n_t_e_r_f_a_c_e", "m_e_t_h_o_d_2")) { method2() }
        }
        run(settings, AnalyticsLibConventionInterface::class) {
            assertEquals(Event("lib_convention_interface", "simple_method")) { simpleMethod() }
        }
    }

    @Test
    fun checkGlobalConvertersWithSubtyping() {
        arrayOf(
                GanalyticsSettings { labelTypeConverters += dummyClassConverter() },
                GanalyticsSettings {
                    labelTypeConverters += dummyClassConverter()
                    useTypeConvertersForSubType = true
                })
                .forEach {
                    run(it, ConverterInterface::class) {
                        assertEquals(Event("converterinterface", "method1", "5 and test")) { method1(DummyClass(5, "test")) }
                        assertEquals(Event("converterinterface", "method2", "5 and tset")) { method2(DummyReversedClass(5, "test")) }
                        assertEquals(Event("converterinterface", "method3", "DummyDataClass(id=5, name=test)")) { method3(DummyDataClass(5, "test")) }
                        assertEquals(Event("converterinterface", "method4", "DummyClass(id=5, name=test)")) { method4(DummyClass(5, "test")) }
                        assertEquals(Event("converterinterface", "method5", "5.test")) { method5(DummyClass(5, "test")) }
                    }
                }
    }

    @Test
    fun checkGlobalConvertersWithoutSubtyping() {
        val settings = GanalyticsSettings {
            labelTypeConverters += dummyClassConverter()
            useTypeConvertersForSubType = false
        }
        run(settings, ConverterInterface::class) {
            assertEquals(Event("converterinterface", "method1", "5 and test")) { method1(DummyClass(5, "test")) }
            assertEquals(Event("converterinterface", "method2", "DummyReversedClass(id=5, name=tset)")) { method2(DummyReversedClass(5, "test")) }
            assertEquals(Event("converterinterface", "method3", "DummyDataClass(id=5, name=test)")) { method3(DummyDataClass(5, "test")) }
            assertEquals(Event("converterinterface", "method4", "DummyClass(id=5, name=test)")) { method4(DummyClass(5, "test")) }
            assertEquals(Event("converterinterface", "method5", "5.test")) { method5(DummyClass(5, "test")) }
        }
    }

    private inline fun <T : Any> run(settings: GanalyticsSettings, clazz: KClass<T>, block: T.() -> Unit) =
            run(testSingleWrapper(settings), clazz, block)

    private fun testSingleWrapper(settings: GanalyticsSettings = GanalyticsSettings()) =
            AnalyticsSingleWrapper(testProvider, settings)

    private fun testConvention() = object : NamingConvention {
        override fun convert(name: String) = name.toCharArray().joinToString(separator = "_")
    }

    private fun dummyClassConverter() = TypeConverterPair<DummyClass> { it.run { "$id and $name" } }

    private interface ConverterInterface {
        fun method1(param: DummyClass)
        fun method2(param: DummyReversedClass)
        fun method3(param: DummyDataClass)
        fun method4(@Label param: DummyClass)
        fun method5(@Label(DummyClassConverter::class) param: DummyClass)
    }

    object DummyClassConverter : TypedLabelConverter<DummyClass> {
        override fun convertTyped(label: DummyClass) = label.run { "$id.$name" }
    }
}