package com.github.programmerr47.ganalytics.core

import com.github.programmerr47.ganalytics.core.NamingConventions.LOWER_CAMEL_CASE
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class GroupWrapperCachingTest : GroupWrapperTest() {

    @Test
    fun checkFillingAndReusingNameCache() {
        run(NotSmartGroupInterface::class) {
            splitter()
            convention()
            splitter()
        }

        assertEquals(2, wrapper.nameCache.size)
        assertEquals(0, wrapper.smartCache.size)
        assertNotNull(wrapper.nameCache["NotSmartGroupInterface.splitter"])
        assertNotNull(wrapper.nameCache["NotSmartGroupInterface.convention"])
    }

    @Test
    fun checkFillingSmartCache() {
        run(SmartGroupInterface::class) {
            simple()
            convention()
            simple()
            splitter()
            simple()
        }

        assertEquals(2, wrapper.smartCache.size)
        assertEquals(0, wrapper.nameCache.size)
        assertNotNull(wrapper.smartCache[""])
        assertNotNull(wrapper.smartCache["test"])
    }

    @Test
    fun checkMixingTwoCaches() {
        run(MixinGroupInterface::class) {
            splitter()
            simple1()
            convention()
            simple2()
        }

        assertEquals(2, wrapper.smartCache.size)
        assertEquals(2, wrapper.nameCache.size)
        assertNotNull(wrapper.nameCache["MixinGroupInterface.splitter"])
        assertNotNull(wrapper.nameCache["MixinGroupInterface.convention"])
        assertNotNull(wrapper.smartCache[""])
        assertNotNull(wrapper.smartCache["test"])
    }

    @Test
    fun checkCacheStateWithDifferentGroups() {
        run(MixinGroupInterface::class) {
            splitter()
            simple1()
            convention()
            simple2()
        }
        run(OtherMixinGroupInterface::class) {
            splitter()
            simple1()
            convention()
            simple2()
        }

        assertEquals(2, wrapper.smartCache.size)
        assertEquals(4, wrapper.nameCache.size)
        assertNotNull(wrapper.nameCache["MixinGroupInterface.splitter"])
        assertNotNull(wrapper.nameCache["MixinGroupInterface.convention"])
        assertNotNull(wrapper.nameCache["OtherMixinGroupInterface.simple1"])
        assertNotNull(wrapper.nameCache["OtherMixinGroupInterface.convention"])
        assertNotNull(wrapper.smartCache[""])
        assertNotNull(wrapper.smartCache["test"])

    }

    interface NotSmartGroupInterface {
        @Convention(LOWER_CAMEL_CASE) fun splitter(): SplitterInterface
        @HasPrefix fun convention(): AnalyticsLibConventionInterface
    }

    interface SmartGroupInterface {
        fun splitter(): SplitterInterface
        fun convention(): AnalyticsLibConventionInterface
        @Category("test") fun simple(): AnalyticsInterface
    }

    interface MixinGroupInterface {
        @Convention(LOWER_CAMEL_CASE) fun splitter(): SplitterInterface
        fun simple1(): AnalyticsInterface
        @HasPrefix fun convention(): AnalyticsLibConventionInterface
        @Category("test") fun simple2(): AnalyticsInterface
    }

    interface OtherMixinGroupInterface {
        fun splitter(): SplitterInterface
        @Convention(LOWER_CAMEL_CASE) fun simple1(): AnalyticsInterface
        @HasPrefix fun convention(): AnalyticsLibConventionInterface
        @Category("test")  fun simple2(): AnalyticsInterface
    }
}
