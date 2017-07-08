package com.github.programmerr47.ganalytics.core

import com.github.programmerr47.ganalytics.core.NamingConventions.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ConventionsTest(
        var testName: String,
        var upperSC: String,
        var lowerSC: String,
        var upperCC: String,
        var lowerCC: String,
        var lower: String) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<String>> {
            return listOf(
                    arrayOf("camelCaseName", "Camel_case_name", "camel_case_name", "CamelCaseName", "camelCaseName", "camelcasename"),
                    arrayOf("ABCD", "Abcd", "abcd", "ABCD", "aBCD", "abcd"),
                    arrayOf("snake_case_name", "Snake_case_name", "snake_case_name", "Snake_case_name", "snake_case_name", "snake_case_name"),
                    arrayOf(ConventionsTest::class.java.simpleName, "Conventions_test", "conventions_test", "ConventionsTest", "conventionsTest", "conventionstest"))
        }
    }


    @Test
    fun checkNames() {
        assertEquals(
                upperSC to UPPER_SNAKE_CASE,
                lowerSC to LOWER_SNAKE_CASE,
                upperCC to UPPER_CAMEL_CASE,
                lowerCC to LOWER_CAMEL_CASE,
                lower to LOWER_CASE)
    }

    private fun assertEquals(vararg pairs: Pair<String, NamingConvention>) =
            pairs.forEach { Assert.assertEquals(it.first, it.second.convert(testName)) }
}

class FixingBadCodeStyleConventionTest {
    private val convention = fixingBadCodeStyleConvention()

    @Test
    fun checkBadInterfacesAndMethods() {
        Assert.assertEquals("BadNamedInterface", convention.convert(Bad___Named_Interface::class.simpleName!!))
        Assert.assertEquals("DasTinHoffmanNamedMethod", convention.convert("dasTinHoffman_Named_Method"))
        Assert.assertEquals("KotlinStyleSuper4357BadNamedInterface", convention.convert(`kotlin style super 4357 __ bad named interface`::class.simpleName!!))
    }
}