package com.github.peacetrue.time.format;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.peacetrue.time.format.DateTimeFormatterUtils.FLEX_D_Y_M_D_H_M_S;


/**
 * @author peace
 **/
class DateTimeFormatterUtilsTest {

    @Test
    void each() {
        Field[] fields = DateTimeFormatterUtils.class.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields)
                .filter(item -> item.getType().equals(DateTimeFormatter.class))
                .collect(Collectors.toList());
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        for (Field field : fieldList) {
            DateTimeFormatter formatter = Assertions.assertDoesNotThrow(() -> (DateTimeFormatter) field.get(null));
            System.out.printf("%s: %s", field.getName(), zonedDateTime.format(formatter));
            System.out.println();
        }
    }


    public void p(Object m) {
        System.out.println(m);
    }

    private LocalDateTime flex(String dateString) {
        return LocalDateTime.parse(dateString, FLEX_D_Y_M_D_H_M_S);
    }

    @Test
    void flexible() {
        p(flex("2016")); // works as intended
        p(flex("2016-05")); // Text '201605' could not be parsed at index 0
        p(flex("2016-05-04")); // Text '20160504' could not be parsed at index 0
        p(flex("2016-05-04 16")); // Text '2016050416' could not be parsed at index 0
        p(flex("2016-05-04 16-36"));
        Assertions.assertDoesNotThrow(() -> p(flex("2016-05-04 16-36-55")));
    }

}
