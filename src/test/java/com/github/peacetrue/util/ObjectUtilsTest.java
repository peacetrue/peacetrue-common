package com.github.peacetrue.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class ObjectUtilsTest {

    @Test
    void defaultIfNull() {
        String originalValue = "2";
        String defaultValue = "1";
        Assertions.assertEquals(defaultValue, ObjectUtils.defaultIfNull(null, defaultValue));
        Assertions.assertEquals(originalValue, ObjectUtils.defaultIfNull(originalValue, defaultValue));
    }
}
