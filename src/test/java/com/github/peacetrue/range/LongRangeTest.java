package com.github.peacetrue.range;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * @author peace
 **/
class LongRangeTest {

    @Test
    void constructor() {
        LongRange range = new LongRange(1L, 2L);
        Assertions.assertNotEquals(range, new LongRange(1L));
        Assertions.assertEquals(range, new LongRange(1L, 2L));
        Assertions.assertEquals(range, new LongRange(range));
        Assertions.assertNotEquals(range, new LongRange(1L, 2L, false, false));
        Assertions.assertEquals(new LongRange(2L, 3L), range.increase(1L));
    }

    @Test
    void getOffset() {
        LongRange range = new LongRange();
        Assertions.assertThrows(NullPointerException.class, () -> LongRange.getOffset(range));
        Assertions.assertEquals(10, LongRange.getOffset(new LongRange(-5L, 5L)));
    }

    @Test
    void toLocalDateTimeRange() {
        long millis = System.currentTimeMillis();
        LocalDateTimeRange range = new LongRange(millis - 1000, millis + 1000).toLocalDateTimeRange();
        Assertions.assertTrue(range.contains(LocalDateTime.now()));
    }
}
