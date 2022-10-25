package com.github.peacetrue.time.format;

import java.text.MessageFormat;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 日期时间格式化工具。提供常用格式化器常量。
 *
 * @author peace
 */
public class DateTimeFormatterUtils {

    /**
     * 支持解析 ISO 本地日期格式，从年到毫秒，
     * 包括：yyyy、yyyy-MM、yyyy-MM-dd、...、yyyy-MM-dd'T'HH:mm:ss.SSS。
     * <p>
     * Spring 中自定义日期解析器时使用。
     *
     * @see DateTimeFormatter#ISO_LOCAL_DATE_TIME
     */
    public static final DateTimeFormatter FLEX_ISO_LOCAL_DATE_TIME = getFlexibleDateTimeFormatter('-');

    private DateTimeFormatterUtils() {
    }

    private static DateTimeFormatter getFlexibleDateTimeFormatter(char separator) {
        return new DateTimeFormatterBuilder()
                .appendPattern(MessageFormat.format("yyyy[''{0}''MM[''{0}''dd[''T''HH['':''mm['':''ss[''.''SSS]]]]]]", separator))
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
    }

}
