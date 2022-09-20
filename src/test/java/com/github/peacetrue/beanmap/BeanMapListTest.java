package com.github.peacetrue.beanmap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.util.Collections;

/**
 * @author peace
 **/
class BeanMapListTest {

    @Test
    void constructor() {
        Assertions.assertDoesNotThrow((ThrowingSupplier<BeanMapList>) BeanMapList::new);
        Assertions.assertDoesNotThrow(() -> new BeanMapList(Collections.emptyList()));
    }

}
