package com.github.peacetrue.range;

import com.github.peacetrue.lang.ObjectUtils;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 本地日期时间范围。
 *
 * @author peace
 */
@NoArgsConstructor
public class LocalDateTimeRange extends ComparableRange<LocalDateTime> implements Serializable {

    public static final LocalDateTimeRange DEFAULT = new LocalDateTimeRange();
    private static final long serialVersionUID = 0L;

    public LocalDateTimeRange(LocalDateTime bound) {
        super(bound);
    }

    public LocalDateTimeRange(LocalDateTime lowerBound, LocalDateTime upperBound) {
        super(lowerBound, upperBound);
    }

    public LocalDateTimeRange(LocalDateTime lowerBound, LocalDateTime upperBound, Boolean lowerInclusive, Boolean upperInclusive) {
        super(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    public LocalDateTimeRange(Range<LocalDateTime> range) {
        super(range);
    }

    /**
     * 截断本地日期时间范围对象到日期，上边界到日期最小值，下边界到日期最大值。
     * <p>
     * 示例：
     * <pre>
     * [2020-01-01 12:00:00, 2020-01-02 12:00:00].truncatedToDays() = [2020-01-01 00:00:00.000000000, 2020-01-02 23:59:59.999999999]
     * </pre>
     *
     * @param range 本地日期时间范围对象
     */
    public static void truncatedToDays(LocalDateTimeRange range) {
        ObjectUtils.acceptSafely(range.getLowerBound(), bound -> range.setLowerBound(bound.with(LocalTime.MIN)));
        ObjectUtils.acceptSafely(range.getUpperBound(), bound -> range.setUpperBound(bound.with(LocalTime.MAX)));
    }

    /**
     * 类似于 {@link #truncatedToDays(LocalDateTimeRange)}，此方法返回新对象，不改变源对象。
     *
     * @return 截断后的本地日期时间范围对象
     */
    public LocalDateTimeRange truncatedToDays() {
        LocalDateTimeRange range = new LocalDateTimeRange(this);
        truncatedToDays(range);
        return range;
    }
}
