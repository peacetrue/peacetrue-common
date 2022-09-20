package com.github.peacetrue.util;

import javax.annotation.Nullable;

/**
 * 字符串工具类。
 *
 * @author peace
 **/
public class StringUtils {

    private StringUtils() {
    }

    /**
     * 指定字符串是否为空。
     *
     * @param string 字符串
     * @return {@code true} 如果字符串为空；否则 {@code false}。
     */
    public static boolean isEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }
}
