package com.github.peacetrue.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

/**
 * @author peace
 **/
class RegexUtilsTest {

    @Test
    void extractValue() {
        //提取符合正则表达式匹配的值
        Pattern pattern = Pattern.compile("Hi, (\\w+), I'm (\\w+)");
        String[] values = RegexUtils.extractValues(pattern, "balabala Hi, Jone, I'm san balabala");
        Assertions.assertArrayEquals(new String[]{"Jone", "san"}, values);
        values = RegexUtils.extractValues(pattern, "Hi, 'Jone', I'm 'san'");
        Assertions.assertArrayEquals(new String[0], values);
    }

    @Test
    void replaceAll() {
        //替换为驼峰命名
        Pattern pattern = Pattern.compile("_(.)");
        String value = RegexUtils.replaceAll(pattern, "user_first_name", matcher -> matcher.group(1).toUpperCase());
        Assertions.assertEquals("userFirstName", value);
    }

}
