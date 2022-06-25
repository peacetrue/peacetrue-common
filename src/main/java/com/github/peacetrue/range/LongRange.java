package com.github.peacetrue.range;

import com.github.peacetrue.util.ObjectUtils;
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

    private static final long serialVersionUID = 0L;

    public static final LongRange DEFAULT = new LongRange();

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
     * @param increment 增量值
     * @return 递增边界后的范围对象，返回新对象不改变源对象
     */
    public LongRange increase(long increment) {
        LongRange range = new LongRange(this);
        increase(range, increment);
        return range;
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
     * @return 上下边界差值
     * @throws NullPointerException 如果上下边界中存在 {@code null} 值
     */
    public Long getOffset() {
        return getUpperBound() - getLowerBound();
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
        LocalDateTimeRange range = new LocalDateTimeRange();
        range.setLowerInclusive(getLowerInclusive());
        range.setUpperInclusive(getUpperInclusive());
        ObjectUtils.acceptSafely(getLowerBound(), bound -> range.setLowerBound(toLocalDateTime(bound, zoneId)));
        ObjectUtils.acceptSafely(getUpperBound(), bound -> range.setUpperBound(toLocalDateTime(bound, zoneId)));
        return range;
    }

    private static LocalDateTime toLocalDateTime(Long bound, ZoneId zoneId) {
        return Instant.ofEpochMilli(bound).atZone(zoneId).toLocalDateTime();
    }

}
