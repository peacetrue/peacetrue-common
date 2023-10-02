package com.github.peacetrue.time.format;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static com.github.peacetrue.time.format.DateTimeFormatterUtils.FLEX_ISO_LOCAL_DATE_TIME;


/**
 * @author peace
 **/
@Slf4j
class DateTimeFormatterUtilsTest {

    @Test
    void buildDateTimeFormatter() {
        log.info("now: {}", LocalDateTime.now());
        testDateTimeFormatter(FLEX_ISO_LOCAL_DATE_TIME);
    }

    @Test
    void buildDateTimeFormatterByPattern() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatterUtils.buildFlexibleISOLocalDateTimeByPattern(DateTimeFormatterUtils.ISO);
        testDateTimeFormatter(dateTimeFormatter);
    }

    private static void testDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        Stream<String> stream = Stream.of(
                "2020", "20",
                "2020-01", "20-1",
                "2020-01-01", "20-1-1",
                "2020-01-01T01", "20-1-1T1",
                "2020-01-01T01:01", "20-1-1T1:1",
                "2020-01-01T01:01:01", "20-1-1T1:1:1",
                "2020-01-01T01:01:01.001", "20-1-1T1:1:1.1",
                Year.now().toString(),
                YearMonth.now().toString(),
                LocalDate.now().toString(),
                LocalDateTime.now().toString()
        );
        stream.forEach(date -> Assertions.assertDoesNotThrow(() ->
                log.info("{} = {}", date, LocalDateTime.parse(date, dateTimeFormatter))
        ));
    }
}
