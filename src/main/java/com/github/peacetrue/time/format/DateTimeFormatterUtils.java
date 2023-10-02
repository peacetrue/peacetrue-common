package com.github.peacetrue.time.format;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.text.MessageFormat;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;

import static java.time.temporal.ChronoField.*;


/**
 * 日期时间格式化工具。提供常用格式化器常量。
 *
 * @author peace
 */
public class DateTimeFormatterUtils {

    /** ISO 格式分隔符 */
    public static final Separators ISO = new Separators().setYear("-").setMonth("-").setDate("T").setHour(":").setMinute(":").setSecond(".");

    /**
     * 支持解析 ISO 本地日期时间格式，从年到纳秒：
     * <pre>
     * 格式：u,    u-M,     u-M-d,      u-M-d'T'H,     u-M-d'T'H:m,      yyyy-M-d'T'H:m:s,    yyyy-M-d'T'H:m:s.n
     * 示例：2020, 2020-01, 2020-01-01, 2020-01-01T01, 2020-01-01T01:01, 2020-01-01T01:01:01, 2020-01-01T01:01:01.001
     * </pre>
     * Spring 中自定义日期解析器时使用。
     *
     * @see DateTimeFormatter#ISO_LOCAL_DATE_TIME
     */
    public static final DateTimeFormatter FLEX_ISO_LOCAL_DATE_TIME = buildFlexibleISOLocalDateTime(ISO);

    private DateTimeFormatterUtils() {
    }

    /** 日期时间单位之间的分隔符。 */
    @Setter
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class Separators {
        private String year;
        private String month;
        private String date;
        private String hour;
        private String minute;
        private String second;
    }

    /**
     * 构建日期时间格式化器。
     *
     * @param separators 分隔符
     * @return 日期时间格式化器
     */
    public static DateTimeFormatter buildFlexibleISOLocalDateTime(Separators separators) {
        return new DateTimeFormatterBuilder()
                .appendValue(YEAR, 1, 9, SignStyle.NORMAL)

//                .parseDefaulting(MONTH_OF_YEAR, Month.JANUARY.getValue())
                .optionalStart()
                .appendLiteral(separators.year)
                .appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
                .optionalEnd()

//                .parseDefaulting(DAY_OF_MONTH, 1)
                .optionalStart()
                .appendLiteral(separators.month)
                .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                .optionalEnd()

//                .parseDefaulting(HOUR_OF_DAY, 0)
                .optionalStart()
                .appendLiteral(separators.date)
                .appendValue(HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)
                .optionalEnd()

//                .parseDefaulting(MINUTE_OF_HOUR, 0)
                .optionalStart()
                .appendLiteral(separators.hour)
                .appendValue(MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE)
                .optionalEnd()

//                .parseDefaulting(SECOND_OF_MINUTE, 0)
                .optionalStart()
                .appendLiteral(separators.minute)
                .appendValue(SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE)
                .optionalEnd()

//                .parseDefaulting(NANO_OF_SECOND, 0)
                .optionalStart()
                .appendLiteral(separators.second)
                .appendFraction(NANO_OF_SECOND, 0, 9, false)
                .optionalEnd()

                // parseDefaulting 需放置在末尾处
                .parseDefaulting(MONTH_OF_YEAR, Month.JANUARY.getValue())
                .parseDefaulting(DAY_OF_MONTH, 1)
                .parseDefaulting(HOUR_OF_DAY, 0)
                .parseDefaulting(MINUTE_OF_HOUR, 0)
                .parseDefaulting(SECOND_OF_MINUTE, 0)
                .parseDefaulting(NANO_OF_SECOND, 0)

                .toFormatter();
    }

    /**
     * 构建日期时间格式化器。
     *
     * @param separators 分隔符
     * @return 日期时间格式化器
     * @see #buildFlexibleISOLocalDateTime(Separators)
     */
    public static DateTimeFormatter buildFlexibleISOLocalDateTimeByPattern(Separators separators) {
        return new DateTimeFormatterBuilder()
                .appendPattern(MessageFormat.format(
                        "u[''{0}''M[''{1}''d[''{2}''H[''{3}''m[''{4}''s[''{5}''n]]]]]]",
                        separators.year, separators.month, separators.date,
                        separators.hour, separators.minute, separators.second
                ))
                .parseDefaulting(MONTH_OF_YEAR, Month.JANUARY.getValue())
                .parseDefaulting(DAY_OF_MONTH, 1)
                .parseDefaulting(HOUR_OF_DAY, 0)
                .parseDefaulting(MINUTE_OF_HOUR, 0)
                .parseDefaulting(SECOND_OF_MINUTE, 0)
                .parseDefaulting(NANO_OF_SECOND, 0)
                .toFormatter();
    }

}
