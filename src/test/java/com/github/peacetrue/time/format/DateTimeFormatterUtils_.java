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
public class DateTimeFormatterUtils_ {

    /** formatter: yyyy */
    public static final DateTimeFormatter YEAR = DateTimeFormatter.ofPattern("yyyy");

    /*------ 单个单位 -------*/
    /** formatter: MM */
    public static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MM");

//    /** formatter: yy */
//    public static final DateTimeFormatter YEAR_SHORT = DateTimeFormatter.ofPattern("yy");
    /** formatter: dd */
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd");
    /** formatter: HH */
    public static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH");
    /** formatter: mm */
    public static final DateTimeFormatter MINUTE = DateTimeFormatter.ofPattern("mm");
    /** formatter: ss */
    public static final DateTimeFormatter SECOND = DateTimeFormatter.ofPattern("ss");
    /** formatter: yyyyMM */
    public static final DateTimeFormatter Y_M = DateTimeFormatter.ofPattern("yyyyMM");

    /*------无分割符串联-------*/
    /** formatter: yyyyMMdd */
    public static final DateTimeFormatter Y_M_D = DateTimeFormatter.ofPattern("yyyyMMdd");

//    /** formatter: MMdd */
//    public static final DateTimeFormatter M_D = DateTimeFormatter.ofPattern("MMdd");
    /** formatter: HHmmss */
    public static final DateTimeFormatter H_M_S = DateTimeFormatter.ofPattern("HHmmss");

//    /** formatter: yyMMdd */
//    public static final DateTimeFormatter S_Y_M_D = DateTimeFormatter.ofPattern("yyMMdd");
    /** formatter: yyyyMMddHHmmss */
    public static final DateTimeFormatter Y_M_D_H_M_S = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    /** formatter: yyyy-MM */
    public static final DateTimeFormatter DASH_Y_M = DateTimeFormatter.ofPattern("yyyy-MM");

    /*-------中划线分隔符串联-----------------------*/
    /** formatter: yyyy-MM-dd */
    public static final DateTimeFormatter DASH_Y_M_D = DateTimeFormatter.ISO_LOCAL_DATE;
    /** formatter: yyyy-MM-dd HH:mm:ss */
    public static final DateTimeFormatter DASH_Y_M_D_H_M_S = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** formatter: yyyy/MM */
    public static final DateTimeFormatter SLASH_Y_M = DateTimeFormatter.ofPattern("yyyy/MM");

    /*-------左划线分隔符串联-----------------------*/
    /** formatter: yyyy/MM/dd */
    public static final DateTimeFormatter SLASH_Y_M_D = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    /** formatter: yyyy/MM/dd/HH:mm:ss */
    public static final DateTimeFormatter SLASH_Y_M_D_H_M_S = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss");
    /** formatter: yyyy['-'MM['-'dd['T'HH[':'mm[':'ss]]]]] */
    public static final DateTimeFormatter FLEX_D_Y_M_D_H_M_S = getFlexibleDateTimeFormatter('-');

    /*------- 弹性中划线分隔符串联 -----------------------*/

    private DateTimeFormatterUtils_() {
    }

    private static DateTimeFormatter getFlexibleDateTimeFormatter(char separator) {
        return new DateTimeFormatterBuilder()
                .appendPattern(MessageFormat.format("yyyy[''{0}''MM[''{0}''dd[[''T'']['' '']HH['':''mm['':''ss[''.''SSS]]]]]]", separator))
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, Month.JANUARY.getValue())
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
//                .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
//                .parseDefaulting(ChronoField.MICRO_OF_SECOND, 0)
//                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .toFormatter();
    }

}
