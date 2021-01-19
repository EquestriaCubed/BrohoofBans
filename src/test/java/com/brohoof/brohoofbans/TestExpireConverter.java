package com.brohoof.brohoofbans;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestExpireConverter {

    private static final long MILLIS_IN_A_WEEK = 604800000L;
    private static final long MILLIS_IN_A_DAY = 86400000L;
    private static final long MILLIS_IN_5_MINS = 300000L;
    private static final long MILLIS_IN_1_MIN = 60000L;
    private static final long MILLIS_IN_1_SEC = 1000L;
    

    @Test
    void testGetExpires() {
        assertEquals(MILLIS_IN_A_WEEK, ExpireConverter.getExpires("1w"));
        assertEquals(MILLIS_IN_A_DAY, ExpireConverter.getExpires("1d"));
        assertEquals(MILLIS_IN_5_MINS, ExpireConverter.getExpires("5m"));
    }

    @Test
    void testGetFriendlyTime() {
        assertEquals("7d-0h-0m-0s", ExpireConverter.getFriendlyTime(MILLIS_IN_A_WEEK));
        assertEquals("1d-0h-0m-0s", ExpireConverter.getFriendlyTime(MILLIS_IN_A_DAY));
        assertEquals("0d-0h-5m-0s", ExpireConverter.getFriendlyTime(MILLIS_IN_5_MINS));
        assertEquals("0d-0h-1m-0s", ExpireConverter.getFriendlyTime(MILLIS_IN_1_MIN));
        assertEquals("0d-0h-0m-1s", ExpireConverter.getFriendlyTime(MILLIS_IN_1_SEC));
    }

    @Test
    void testGetTimeStamp() {
        assertEquals("1970-01-07 19:00:00", ExpireConverter.getTimeStamp(MILLIS_IN_A_WEEK));
        assertEquals("1970-01-01 19:00:00", ExpireConverter.getTimeStamp(MILLIS_IN_A_DAY));
        assertEquals("1969-12-31 19:05:00", ExpireConverter.getTimeStamp(MILLIS_IN_5_MINS));
    }

}
