package com.github.peacetrue.net;

import com.github.peacetrue.lang.UncheckedException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL 编解码工具类。
 *
 * @author peace
 * @see URLEncoder
 * @see URLDecoder
 **/
public class URLCodecUtils {

    private URLCodecUtils() {
    }

    /**
     * 编码，使用 UTF8 字符集。
     *
     * @param original 原始值
     * @return 编码值
     */
    public static String encode(String original) {
        return encode(original, StandardCharsets.UTF_8.name());
    }

    /**
     * 编码。
     *
     * @param original 原始值
     * @param charset  字符集
     * @return 编码值
     */
    public static String encode(String original, String charset) {
        try {
            return URLEncoder.encode(original, charset);
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedException(e);
        }
    }

    /**
     * 解码，使用 UTF8 字符集。
     *
     * @param encoded 编码值
     * @return 原始值
     */
    public static String decode(String encoded) {
        return decode(encoded, StandardCharsets.UTF_8.name());
    }

    /**
     * 解码。
     *
     * @param encoded 编码值
     * @param charset 字符集
     * @return 原始值
     */
    public static String decode(String encoded, String charset) {
        try {
            return URLDecoder.decode(encoded, charset);
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedException(e);
        }
    }
}
