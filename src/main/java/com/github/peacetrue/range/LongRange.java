package com.github.peacetrue.range;

import com.github.peacetrue.lang.ObjectUtils;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 整形范围值。
 *
 * @author peace
 **/
@NoArgsConstructor
public class LongRange extends ComparableRange<Long> implements Serializable {

    public static final LongRange DEFAULT = new LongRange();
    private static final long serialVersionUID = 0L;

    public LongRange(Long bound) {
        super(bound);
    }

    public LongRange(Long lowerBound, Long upperBound) {
        super(lowerBound, upperBound);
    }

    public LongRange(Long lowerBound, Long upperBound, Boolean lowerInclusive, Boolean upperInclusive) {
        super(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    public LongRange(Range<Long> range) {
        super(range);
    }

    /**
     * 递增范围对象边界值。
     *
     * @param range     范围对象
     * @param increment 增量值
     */
    public static void increase(LongRange range, long increment) {
        ObjectUtils.acceptSafely(range.getLowerBound(), value -> range.setLowerBound(value + increment));
        ObjectUtils.acceptSafely(range.getUpperBound(), value -> range.setUpperBound(value + increment));
    }

    /**
     * 获取上下边界差值。
     *
     * @param range 范围
     * @return 上下边界差值
     * @throws NullPointerException 如果上下边界中存在 {@code null} 值
     */
    public static Long getOffset(Range<Long> range) {
        return range.getUpperBound() - range.getLowerBound();
    }

    private static LocalDateTime toLocalDateTime(Long bound, ZoneId zoneId) {
        return Instant.ofEpochMilli(bound).atZone(zoneId).toLocalDateTime();
    }

    /**
     * 递增范围对象边界值。
     *
     * @param increment 增量值
     * @return 递增边界后的范围对象，返回新对象不改变源对象
     */
    public LongRange increase(long increment) {
        LongRange range = new LongRange(this);
        increase(range, increment);
        return range;
    }

    /**
     * 时间戳转换为默认时区的本地日期时间范围。
     *
     * @return 转换为本地日期时间时间范围
     */
    public LocalDateTimeRange toLocalDateTimeRange() {
        return toLocalDateTimeRange(ZoneId.systemDefault());
    }

    /**
     * 时间戳转换为指定时区的本地日期时间范围。
     *
     * @param zoneId 时区
     * @return 转换为本地日期时间时间范围
     */
    public LocalDateTimeRange toLocalDateTimeRange(ZoneId zoneId) {
        LocalDateTimeRange localDateTimeRange = new LocalDateTimeRange();
        localDateTimeRange.setLowerInclusive(getLowerInclusive());
        localDateTimeRange.setUpperInclusive(getUpperInclusive());
        ObjectUtils.acceptSafely(getLowerBound(), bound -> localDateTimeRange.setLowerBound(toLocalDateTime(bound, zoneId)));
        ObjectUtils.acceptSafely(getUpperBound(), bound -> localDateTimeRange.setUpperBound(toLocalDateTime(bound, zoneId)));
        return localDateTimeRange;
    }

}
