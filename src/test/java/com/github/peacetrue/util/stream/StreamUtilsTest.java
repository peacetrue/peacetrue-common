package com.github.peacetrue.util.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

/**
 * @author peace
 **/
class StreamUtilsTest {

    @Test
    void toStreamSafely() {
        Assertions.assertNotNull(StreamUtils.toStreamSafely(StreamUtils.class));
    }

    @Test
    void toStreamSafelyLazily() {
        Assertions.assertNotNull(StreamUtils.toStreamSafelyLazily(null, Stream::empty, Stream::of));
        Assertions.assertNotNull(StreamUtils.toStreamSafelyLazily(new Object(), Stream::empty, Stream::of));
    }

    @Test
    void toStream() {
        Assertions.assertThrows(NullPointerException.class, () -> StreamUtils.toStream(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> StreamUtils.toStream(StreamUtils.class));
        Assertions.assertNotNull(StreamUtils.toStream(Stream.empty()));
        Assertions.assertNotNull(StreamUtils.toStream(Collections.emptyList()));
        Assertions.assertNotNull(StreamUtils.toStream(Paths.get("")));
        Assertions.assertNotNull(StreamUtils.toStream(Collections.emptyIterator()));
        Assertions.assertNotNull(StreamUtils.toStream(new Object[]{}));
    }
}
