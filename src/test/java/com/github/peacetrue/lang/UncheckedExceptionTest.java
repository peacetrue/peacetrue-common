package com.github.peacetrue.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class UncheckedExceptionTest {

    @Test
    void construct() {
        Assertions.assertThrows(UncheckedException.class, () -> {
            throw new UncheckedException(new IllegalStateException());
        });
    }

}
