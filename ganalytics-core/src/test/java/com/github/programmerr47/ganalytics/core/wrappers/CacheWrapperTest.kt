package com.github.programmerr47.ganalytics.core.wrappers

import com.github.programmerr47.ganalytics.core.EventProvider
import com.github.programmerr47.ganalytics.core.SampleInterface
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CacheWrapperTest {
    val wrapper = AnalyticsSingleWrapper(EventProvider {})

    @Test
    fun checkDifferentInstancesByDefault() {
        val instance1 = wrapper.create(SampleInterface::class)
        val instance2 = wrapper.create(SampleInterface::class)
        assertFalse(instance1 === instance2)
    }

    @Test
    fun checkCachingAndReturningSameInstance() {
        val cachedWrapper = CacheWrapper(wrapper)
        val instance = cachedWrapper.create(SampleInterface::class)
        val cachedInstance = cachedWrapper.create(SampleInterface::class)
        assertTrue(instance === cachedInstance)
    }
}