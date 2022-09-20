package com.github.peacetrue.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class ElapsedTimeUtilsTest {

    @Test
    void setClock() {
        Assertions.assertDoesNotThrow(() -> ElapsedTimeUtils.setClock(() -> System.currentTimeMillis() - 1));
    }

    @Test
    void evaluate() {
        long elapsedTime = ElapsedTimeUtils.evaluate(() -> System.out.println("hi"));
        Assertions.assertTrue(elapsedTime >= 0);
    }
}
