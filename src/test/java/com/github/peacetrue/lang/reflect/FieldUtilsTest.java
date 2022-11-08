package com.github.peacetrue.lang.reflect;

import com.github.peacetrue.test.JacocoUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author peace
 **/
class FieldUtilsTest {

    private static List<Field> exclude(Stream<Field> fields) {
        return fields
                .filter(field -> !field.getName().startsWith("$jacoco"))
                .collect(Collectors.toList());
    }

    @Test
    void getDeclaredFields() {
        List<Field> fields = exclude(FieldUtils.getDeclaredFields(User.class).stream());
        Assertions.assertEquals(
                Arrays.asList("id", "name", "age", "nation"),
                fields.stream().map(Field::getName).collect(Collectors.toList())
        );

        List<Field> fields2 = exclude(FieldUtils.getDeclaredFields(EmployeeUser.class).stream());
        Assertions.assertEquals(
                Arrays.asList("id", "name", "age", "nation", "job", "pay"),
                fields2.stream().map(Field::getName).collect(Collectors.toList())
        );
    }
}
