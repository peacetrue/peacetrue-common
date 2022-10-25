package com.github.peacetrue.lang;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class UncheckedExceptionTest {

    @Test
    void construct() {
        IllegalStateException illegalStateException = new IllegalStateException();
        Assertions.assertThrows(UncheckedException.class, () -> {
            throw new UncheckedException(illegalStateException);
        });
    }

}
