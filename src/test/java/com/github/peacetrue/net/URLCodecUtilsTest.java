package com.github.peacetrue.net;

import com.github.peacetrue.lang.UncheckedException;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class URLCodecUtilsTest {

    @Test
    void encode() {
        String original = "name=" + RandomStringUtils.random(10);
        String encoded = URLCodecUtils.encode(original);
        Assertions.assertEquals(original, URLCodecUtils.decode(encoded));

        Assertions.assertThrows(UncheckedException.class, () -> URLCodecUtils.encode(original, "charsetNotExists"));
        Assertions.assertThrows(UncheckedException.class, () -> URLCodecUtils.decode(encoded, "charsetNotExists"));
    }
}
