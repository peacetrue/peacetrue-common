package com.github.peacetrue.range;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author peace
 **/
class LocalDateTimeRangeTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @Test
    void constructor() {
        LocalDateTime now = LocalDateTime.now();
        Assertions.assertEquals(new LocalDateTimeRange(now), new LocalDateTimeRange(now, now, true, true));
        Assertions.assertEquals(new LocalDateTimeRange(now, now), new LocalDateTimeRange(now, now, true, true));
    }

    @Test
    void truncatedToDays() {
        LocalDateTimeRange range = EASY_RANDOM.nextObject(LocalDateTimeRange.class);
        LocalDateTimeRange truncatedTo = range.truncatedToDays();
        Assertions.assertEquals(range.getLowerBound().truncatedTo(ChronoUnit.DAYS), truncatedTo.getLowerBound());
        Assertions.assertEquals(range.getUpperBound().truncatedTo(ChronoUnit.DAYS).plusDays(1).minusNanos(1), truncatedTo.getUpperBound());

        range = new LocalDateTimeRange();
        truncatedTo = range.truncatedToDays();
        Assertions.assertNull(truncatedTo.getLowerBound());
        Assertions.assertNull(truncatedTo.getUpperBound());
    }


}
