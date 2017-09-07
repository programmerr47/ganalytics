package com.github.programmerr47.ganalytics.core.wrappers

import com.github.programmerr47.ganalytics.core.Event
import com.github.programmerr47.ganalytics.core.SampleGroupInterface
import com.github.programmerr47.ganalytics.core.SampleInterface
import com.github.programmerr47.ganalytics.core.TestEventProvider
import org.junit.Assert.*
import org.junit.Test

class ObjectBehaviorTest {
    val testProvider: TestEventProvider = TestEventProvider()
    val singleWrapper = AnalyticsSingleWrapper(testProvider)
    val groupWrapper = AnalyticsGroupWrapper(testProvider)
    val testSingleImpl = singleWrapper.create(SampleInterface::class)
    val testGroupImpl = groupWrapper.create(SampleGroupInterface::class)

    @Test
    fun checkToString() {
        assertTrue(testSingleImpl.toString().startsWith("SampleInterface(Proxy)@"))
        assertTrue(testGroupImpl.toString().startsWith("SampleGroupInterface(Proxy)@"))
        assertEquals(Event("", ""), testProvider.lastEvent)
    }

    @Test
    fun checkEquals() {
        val otherSingleImpl = singleWrapper.create(SampleInterface::class)
        val otherGroupImpl = groupWrapper.create(SampleGroupInterface::class)
        assertTrue(testSingleImpl == testSingleImpl)
        assertTrue(testGroupImpl == testGroupImpl)
        assertFalse(testSingleImpl == otherSingleImpl)
        assertFalse(testGroupImpl == otherGroupImpl)
        assertEquals(Event("", ""), testProvider.lastEvent)
    }

    @Test
    fun checkHashCode() {
        //need to check that methods just invoke without exceptions
        testSingleImpl.hashCode()
        testGroupImpl.hashCode()
        assertEquals(Event("", ""), testProvider.lastEvent)
    }
}
