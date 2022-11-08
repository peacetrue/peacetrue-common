package com.github.peacetrue.util.stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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
        Assertions.assertNotNull(StreamUtils.toStream(Collections.emptyEnumeration()));
    }

    @Test
    void invoke() {
        Object object = new Object();
        Assertions.assertEquals(object, StreamUtils.invoke(object, null, Function.identity()));
        object = Arrays.asList(1, 2);
        Assertions.assertEquals(object, StreamUtils.invoke(object, stream -> stream.collect(Collectors.toList()), null));
    }

    @Test
    void toStreamNullable() {
        Assertions.assertNull(StreamUtils.toStreamNullable(new Object()));
    }

    @Test
    void enumerationAsStream() {
        List<Integer> integers = Arrays.asList(1, 2);
        Assertions.assertEquals(integers, StreamUtils.enumerationAsStream(Collections.enumeration(integers)).collect(Collectors.toList()));
    }
}
