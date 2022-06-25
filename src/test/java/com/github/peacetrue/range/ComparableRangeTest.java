package com.github.peacetrue.range;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class ComparableRangeTest {

    public static final EasyRandom EASY_RANDOM = new EasyRandom();

    @Test
    void equals() {
        ComparableRange<Long> range = EASY_RANDOM.nextObject(LongRange.class);
        Assertions.assertEquals(new ComparableRange<>(range), new ComparableRange<>(range));
    }

    @Test
    void contains() {
        long lowerBound = 1L, upperBound = 3L;
        ComparableRange<Long> range = new ComparableRange<>(lowerBound, upperBound);
        Assertions.assertFalse(range.contains(0L));
        Assertions.assertTrue(range.contains(lowerBound));
        Assertions.assertTrue(range.contains(2L));
        Assertions.assertTrue(range.contains(upperBound));
        Assertions.assertFalse(range.contains(4L));

        range = new ComparableRange<>(lowerBound, upperBound, false, false);
        Assertions.assertFalse(range.contains(lowerBound));
        Assertions.assertTrue(range.contains(2L));
        Assertions.assertFalse(range.contains(upperBound));

        range = new ComparableRange<>(lowerBound, upperBound, true, false);
        Assertions.assertTrue(range.contains(lowerBound));
        Assertions.assertTrue(range.contains(2L));
        Assertions.assertFalse(range.contains(upperBound));

        range = new ComparableRange<>(lowerBound, upperBound, false, true);
        Assertions.assertFalse(range.contains(lowerBound));
        Assertions.assertTrue(range.contains(2L));
        Assertions.assertTrue(range.contains(upperBound));

        Assertions.assertTrue(new ComparableRange<Long>().contains(Long.MIN_VALUE));
        Assertions.assertTrue(new ComparableRange<Long>().contains(Long.MAX_VALUE));
    }
}
