package com.github.peacetrue.range;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * @author peace
 **/
class LocalDateRangeTest {

    @Test
    void constructor() {
        LocalDate now = LocalDate.now();
        LocalDateRange range = new LocalDateRange(now, now, true, true);
        Assertions.assertEquals(new LocalDateRange(now), range);
        Assertions.assertEquals(new LocalDateRange(now, now), range);
        Assertions.assertEquals(new LocalDateRange(range), range);
    }


    @Test
    void toLocalDateTimeRange() {
        Assertions.assertEquals(
                new LocalDateRange(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-02-01")).toLocalDateTimeRange(),
                new LocalDateTimeRange(LocalDateTime.parse("2020-01-01T00:00:00.000000000"), LocalDateTime.parse("2020-02-01T23:59:59.999999999"))
        );
    }
}
