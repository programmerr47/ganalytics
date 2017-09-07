package com.github.programmerr47.ganalytics.core.wrappers;

import com.github.programmerr47.ganalytics.core.Event;
import com.github.programmerr47.ganalytics.core.SampleGroupInterface;
import com.github.programmerr47.ganalytics.core.SampleInterface;
import com.github.programmerr47.ganalytics.core.TestEventProvider;
import com.github.programmerr47.ganalytics.core.wrappers.AnalyticsGroupWrapper;
import com.github.programmerr47.ganalytics.core.wrappers.AnalyticsSingleWrapper;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectBehaviorJavaTest {
    private final TestEventProvider testProvider = new TestEventProvider();
    private final AnalyticsSingleWrapper singleWrapper = new AnalyticsSingleWrapper(testProvider);
    private final AnalyticsGroupWrapper groupWrapper = new AnalyticsGroupWrapper(testProvider);
    private final SampleInterface testSingleImpl = singleWrapper.create(SampleInterface.class);
    private final SampleGroupInterface testGroupImpl = groupWrapper.create(SampleGroupInterface.class);

    @Test
    public void checkToString() {
        assertTrue(testSingleImpl.toString().startsWith("SampleInterface(Proxy)@"));
        assertTrue(testGroupImpl.toString().startsWith("SampleGroupInterface(Proxy)@"));
        Assert.assertEquals(new Event("", ""), testProvider.getLastEvent());
    }

    @Test
    public void checkEquals() {
        SampleInterface otherSingleImpl = singleWrapper.create(SampleInterface.class);
        SampleGroupInterface otherGroupImpl = groupWrapper.create(SampleGroupInterface.class);
        assertTrue(testSingleImpl.equals(testSingleImpl));
        assertTrue(testGroupImpl.equals(testGroupImpl));
        assertFalse(testSingleImpl.equals(otherSingleImpl));
        assertFalse(testGroupImpl.equals(otherGroupImpl));
        assertEquals(new Event("", ""), testProvider.getLastEvent());
    }

    @Test
    public void checkHashCode() {
        //need to check that methods just invoke without exceptions
        testSingleImpl.hashCode();
        testGroupImpl.hashCode();
        assertEquals(new Event("", ""), testProvider.getLastEvent());
    }
}
