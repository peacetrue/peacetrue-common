package com.github.peacetrue.lang.reflect;

import com.github.peacetrue.test.JacocoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author peace
 **/
class FieldUtilsTest {

    @Test
    void getDeclaredFields() {
        List<Field> fields = JacocoUtils.exclude(FieldUtils.getDeclaredFields(User.class).stream());
        Assertions.assertEquals(
                Arrays.asList("id", "name", "age", "nation"),
                fields.stream().map(Field::getName).collect(Collectors.toList())
        );

        List<Field> fields2 = JacocoUtils.exclude(FieldUtils.getDeclaredFields(EmployeeUser.class).stream());
        Assertions.assertEquals(
                Arrays.asList("id", "name", "age", "nation", "job", "pay"),
                fields2.stream().map(Field::getName).collect(Collectors.toList())
        );
    }
}
