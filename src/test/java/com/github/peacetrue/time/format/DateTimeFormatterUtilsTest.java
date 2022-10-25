package com.github.peacetrue.time.format;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.peacetrue.time.format.DateTimeFormatterUtils.FLEX_ISO_LOCAL_DATE_TIME;


/**
 * @author peace
 **/
@Slf4j
class DateTimeFormatterUtilsTest {

    @Test
    void flexible() {
        Stream.of(
                "2020",
                "2020-01",
                "2020-01-01",
                "2020-01-01T01",
                "2020-01-01T01:01",
                "2020-01-01T01:01:01",
                "2020-01-01T01:01:01.001"
        ).forEach(date -> Assertions.assertDoesNotThrow(() ->
                LocalDateTime.parse(date, FLEX_ISO_LOCAL_DATE_TIME)
        ));
    }

    void each() {
        Field[] fields = DateTimeFormatterUtils_.class.getDeclaredFields();
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

}
