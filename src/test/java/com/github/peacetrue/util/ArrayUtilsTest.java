package com.github.peacetrue.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class ArrayUtilsTest {

    @Test
    void isEmpty() {
        Assertions.assertTrue(ArrayUtils.isEmpty(null));
        Assertions.assertTrue(ArrayUtils.isEmpty(new Object[]{}));
        Assertions.assertFalse(ArrayUtils.isEmpty(new Object[]{1}));
    }

    @Test
    void firstSafely() {
        Assertions.assertNull(ArrayUtils.firstSafely(null));
        Assertions.assertNull(ArrayUtils.firstSafely(new Object[]{}));
        Assertions.assertEquals(1, ArrayUtils.firstSafely(new Object[]{1}));
    }

    @Test
    void lastSafely() {
        Assertions.assertNull(ArrayUtils.lastSafely(null));
        Assertions.assertNull(ArrayUtils.lastSafely(new Object[]{}));
        Assertions.assertEquals(2, ArrayUtils.lastSafely(new Object[]{1, 2}));
    }

    @Test
    void replace() {
        Integer[] values = {1, 2, 3};
        ArrayUtils.replace(values, i -> i + 1);
        Assertions.assertArrayEquals(new Integer[]{2, 3, 4}, values);
    }
}
