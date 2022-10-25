package com.github.peacetrue.range;

import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author peace
 **/
@Slf4j
class RangeTest {

    public static final EasyRandom EASY_RANDOM = new EasyRandom();

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<Range<Object>>) Range::new);
        Assertions.assertDoesNotThrow(() -> new Range<>(1L, 2L));
        Assertions.assertDoesNotThrow(() -> new Range<>(1L, 2L, false, false));
        Assertions.assertDoesNotThrow(() -> new Range<>(new Range<>()));
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(Range.isEmpty(new Range<>()));
        Assertions.assertFalse(Range.isEmpty(new Range<>(1, null)));
        Assertions.assertFalse(Range.isEmpty(new Range<>(null, 1)));
    }

    @Test
    void isFull() {
        Assertions.assertFalse(Range.isFull(new Range<>()));
        Assertions.assertFalse(Range.isFull(new Range<>(1, null)));
        Assertions.assertFalse(Range.isFull(new Range<>(null, 1)));
        Assertions.assertTrue(Range.isFull(new Range<>(1, 2)));
    }

    @Test
    void isLowerInclusive() {
        Assertions.assertTrue(Range.isLowerInclusive(new Range<>()));
        Assertions.assertFalse(Range.isLowerInclusive(new Range<>(null, null, false, null)));
    }

    @Test
    void isUpperInclusive() {
        Assertions.assertTrue(Range.isUpperInclusive(new Range<>()));
        Assertions.assertFalse(Range.isUpperInclusive(new Range<>(null, null, null, false)));
    }

    @Test
    void testEquals() {
        Range<Long> range = new Range<>(-1L, 1L);

        Assertions.assertEquals(range, range);
        Assertions.assertNotEquals(range, new Object());
        Assertions.assertNotEquals(range, new Range<>());
        Assertions.assertNotEquals(new Range<>(range.getLowerBound(), 2L), range);
        Assertions.assertEquals(new Range<>(range.getLowerBound(), range.getUpperBound()), range);
        Assertions.assertNotEquals(new Range<>(range.getLowerBound(), range.getUpperBound(), false, true), range);
        Assertions.assertNotEquals(new Range<>(range.getLowerBound(), range.getUpperBound(), true, false), range);
    }

    @Test
    void testToString() {
        Assertions.assertEquals(
                new LongRange(1L, 1L).toString(),
                new LongRange(1L, 1L).toString()
        );
        Assertions.assertEquals(
                new LongRange(1L, 1L, false, false).toString(),
                new LongRange(1L, 1L, false, false).toString()
        );
    }

    @Test
    void testHashCode() {
        Range<Long> range = EASY_RANDOM.nextObject(LongRange.class);
        Set<Range<Long>> set = new HashSet<>(Arrays.asList(
                range, range, new Range<>(range), new Range<>(range)
        ));
        Assertions.assertEquals(2, set.size());
    }

}
