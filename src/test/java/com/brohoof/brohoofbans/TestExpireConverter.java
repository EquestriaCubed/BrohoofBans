package com.brohoof.brohoofbans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestExpireConverter {

    private static final long MILLIS_IN_A_WEEK = 604800000L;
    private static final long MILLIS_IN_A_DAY = 86400000L;

    @Test
    void testGetExpires() {
        assertTrue(MILLIS_IN_A_WEEK == ExpireConverter.getExpires("1w"));
        assertTrue(MILLIS_IN_A_DAY == ExpireConverter.getExpires("1d"));
    }

    @Test
    void testGetFriendlyTime() {
        assertTrue("7d-0h-0m-0s".equals(ExpireConverter.getFriendlyTime(MILLIS_IN_A_WEEK)));
        assertTrue("1d-0h-0m-0s".equals(ExpireConverter.getFriendlyTime(MILLIS_IN_A_DAY)));
    }

    @Test
    void testGetTimeStamp() {
        assertEquals("1970-01-07 19:00:00", ExpireConverter.getTimeStamp(MILLIS_IN_A_WEEK));
        assertEquals("1970-01-01 19:00:00", ExpireConverter.getTimeStamp(MILLIS_IN_A_DAY));
    }

}
