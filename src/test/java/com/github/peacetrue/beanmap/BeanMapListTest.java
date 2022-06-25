package com.github.peacetrue.beanmap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author peace
 **/
class BeanMapListTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow(() -> new BeanMapList());
        Assertions.assertDoesNotThrow(() -> new BeanMapList(Collections.emptyList()));
        Assertions.assertDoesNotThrow(() -> new BeanMapList(1));
    }

}
