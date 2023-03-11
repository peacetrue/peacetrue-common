package com.github.peacetrue.lang;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
@Slf4j
class ObjectUtilsTest {

    @Data
    private static class User {
        private String name;
    }

    @Test
    void defaultIfNull() {
        String originalValue = RandomStringUtils.random(10), defaultValue = RandomStringUtils.random(10);
        Assertions.assertEquals(defaultValue, ObjectUtils.defaultIfNull(null, defaultValue));
        Assertions.assertEquals(originalValue, ObjectUtils.defaultIfNull(originalValue, defaultValue));
    }

    @Test
    void defaultIfNullLazily() {
        String originalValue = RandomStringUtils.random(10), defaultValue = RandomStringUtils.random(10);
        Assertions.assertEquals(defaultValue, ObjectUtils.defaultIfNullLazily(null, () -> defaultValue));
        Assertions.assertEquals(originalValue, ObjectUtils.defaultIfNullLazily(originalValue, () -> defaultValue));
    }

    @Test
    void acceptSafelyReturnly() {
        Object value1 = new Object();
        Assertions.assertEquals(value1, ObjectUtils.acceptSafelyReturnly(value1, value -> log.info("i: {}", value)));
    }

    @Test
    void setDefault() {
        User user = new User();
        Assertions.assertNull(user.getName());

        String defaultValue = RandomStringUtils.random(10);
        ObjectUtils.setDefault(user::getName, user::setName, defaultValue);
        Assertions.assertEquals(defaultValue, user.getName());

        defaultValue += "1";
        ObjectUtils.setDefault(user::getName, user::setName, defaultValue);
        Assertions.assertNotEquals(defaultValue, user.getName());
    }

    @Test
    void setDefaultLazily() {
        User user = new User();
        Assertions.assertNull(user.getName());

        String defaultValue = RandomStringUtils.random(10);
        ObjectUtils.setDefaultLazily(user::getName, user::setName, () -> defaultValue);
        Assertions.assertEquals(defaultValue, user.getName());

        String changedDefaultValue = defaultValue + "1";
        ObjectUtils.setDefaultLazily(user::getName, user::setName, () -> changedDefaultValue);
        Assertions.assertNotEquals(changedDefaultValue, user.getName());
    }



}
