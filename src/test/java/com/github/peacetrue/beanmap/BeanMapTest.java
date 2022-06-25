package com.github.peacetrue.beanmap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author peace
 **/
class BeanMapTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(() -> new BeanMap());
        Assertions.assertDoesNotThrow(() -> new BeanMap(Collections.emptyMap()));
        Assertions.assertDoesNotThrow(() -> new BeanMap(1));
        Assertions.assertDoesNotThrow(() -> new BeanMap(1, 0.75F));
        Assertions.assertDoesNotThrow(() -> new BeanMap(1, 0.75F, true));
    }
}
