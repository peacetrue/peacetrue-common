package com.github.peacetrue.time.format;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

/**
 * 日期时间格式化工具。提供常用的格式化器常量。
 *
 * @author peace
 */
public class DateTimeFormatterUtils {

    private DateTimeFormatterUtils() {
    }

    /*------ 单个单位 -------*/

    /** formatter: yyyy */
    public static final DateTimeFormatter YEAR = DateTimeFormatter.ofPattern("yyyy");

//    /** formatter: yy */
//    public static final DateTimeFormatter YEAR_SHORT = DateTimeFormatter.ofPattern("yy");

    /** formatter: MM */
    public static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MM");

    /** formatter: dd */
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd");

    /** formatter: HH */
    public static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH");

    /** formatter: mm */
    public static final DateTimeFormatter MINUTE = DateTimeFormatter.ofPattern("mm");

    /** formatter: ss */
    public static final DateTimeFormatter SECOND = DateTimeFormatter.ofPattern("ss");

    /*------无分割符串联-------*/

    /** formatter: yyyyMM */
    public static final DateTimeFormatter Y_M = DateTimeFormatter.ofPattern("yyyyMM");

//    /** formatter: MMdd */
//    public static final DateTimeFormatter M_D = DateTimeFormatter.ofPattern("MMdd");

    /** formatter: yyyyMMdd */
    public static final DateTimeFormatter Y_M_D = DateTimeFormatter.ofPattern("yyyyMMdd");

//    /** formatter: yyMMdd */
//    public static final DateTimeFormatter S_Y_M_D = DateTimeFormatter.ofPattern("yyMMdd");

    /** formatter: HHmmss */
    public static final DateTimeFormatter H_M_S = DateTimeFormatter.ofPattern("HHmmss");

    /** formatter: yyyyMMddHHmmss */
    public static final DateTimeFormatter Y_M_D_H_M_S = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /*-------中划线分隔符串联-----------------------*/

    /** formatter: yyyy-MM */
    public static final DateTimeFormatter DASH_Y_M = DateTimeFormatter.ofPattern("yyyy-MM");

    /** formatter: yyyy-MM-dd */
    public static final DateTimeFormatter DASH_Y_M_D = DateTimeFormatter.ISO_LOCAL_DATE;

    /** formatter: yyyy-MM-dd HH:mm:ss */
    public static final DateTimeFormatter DASH_Y_M_D_H_M_S = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /*-------左划线分隔符串联-----------------------*/

    /** formatter: yyyy/MM */
    public static final DateTimeFormatter SLASH_Y_M = DateTimeFormatter.ofPattern("yyyy/MM");
    /** formatter: yyyy/MM/dd */
    public static final DateTimeFormatter SLASH_Y_M_D = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    /** formatter: yyyy/MM/dd/HH:mm:ss */
    public static final DateTimeFormatter SLASH_Y_M_D_H_M_S = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss");

    /*------- 弹性中划线分隔符串联 -----------------------*/

    /** formatter: yyyy['-'MM['-'dd[ HH['-'mm['-'ss]]]]] */
    public static final DateTimeFormatter FLEX_D_Y_M_D_H_M_S = getFlexibleDateTimeFormatter('-');

    private static DateTimeFormatter getFlexibleDateTimeFormatter(char separator) {
        return new DateTimeFormatterBuilder()
                .appendPattern(MessageFormat.format("yyyy[''{0}''MM[''{0}''dd[' 'HH[''{0}''mm[''{0}''ss]]]]]", separator))
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
    }

    public static String humanReadableFormat(Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

}
