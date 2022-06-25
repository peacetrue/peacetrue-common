package com.github.peacetrue.range;

import com.github.peacetrue.util.ObjectUtils;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 本地日期范围。
 *
 * @author peace
 */
@NoArgsConstructor
public class LocalDateRange extends ComparableRange<LocalDate> implements Serializable {

    private static final long serialVersionUID = 0L;

    public static final LocalDateRange DEFAULT = new LocalDateRange();

    public LocalDateRange(LocalDate bound) {
        super(bound);
    }

    public LocalDateRange(LocalDate lowerBound, LocalDate upperBound) {
        super(lowerBound, upperBound);
    }

    public LocalDateRange(LocalDate lowerBound, LocalDate upperBound, Boolean lowerInclusive, Boolean upperInclusive) {
        super(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    public LocalDateRange(Range<LocalDate> range) {
        super(range);
    }

    /**
     * 转换为本地日期时间范围。
     *
     * @return 本地日期时间范围
     */
    public LocalDateTimeRange toLocalDateTimeRange() {
        LocalDateTimeRange range = new LocalDateTimeRange();
        range.setLowerInclusive(getLowerInclusive());
        range.setUpperInclusive(getUpperInclusive());
        ObjectUtils.acceptSafely(getLowerBound(), bound -> range.setLowerBound(bound.atTime(LocalTime.MIN)));
        ObjectUtils.acceptSafely(getUpperBound(), bound -> range.setUpperBound(bound.atTime(LocalTime.MAX)));
        return range;
    }


}
