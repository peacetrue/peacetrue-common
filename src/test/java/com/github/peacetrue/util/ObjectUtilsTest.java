package com.github.peacetrue.util;

import com.github.peacetrue.util.ObjectUtils;
import lombok.Data;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
class ObjectUtilsTest {


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


    @Data
    private static class User {
        private String name;
    }
}
