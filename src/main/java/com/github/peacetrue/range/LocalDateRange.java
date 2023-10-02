package com.github.peacetrue.range;

import com.github.peacetrue.lang.ObjectUtils;
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

    /** 默认实例 */
    public static final LocalDateRange DEFAULT = new LocalDateRange();
    private static final long serialVersionUID = 0L;

    /** {@inheritDoc} */
    public LocalDateRange(LocalDate bound) {
        super(bound);
    }

    /** {@inheritDoc} */
    public LocalDateRange(LocalDate lowerBound, LocalDate upperBound) {
        super(lowerBound, upperBound);
    }

    /** {@inheritDoc} */
    public LocalDateRange(LocalDate lowerBound, LocalDate upperBound, Boolean lowerInclusive, Boolean upperInclusive) {
        super(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    /** {@inheritDoc} */
    public LocalDateRange(Range<LocalDate> range) {
        super(range);
    }

    /**
     * 转换为本地日期时间范围。
     *
     * @return 本地日期时间范围
     */
    public LocalDateTimeRange toLocalDateTimeRange() {
        LocalDateTimeRange localDateTimeRange = new LocalDateTimeRange();
        localDateTimeRange.setLowerInclusive(getLowerInclusive());
        localDateTimeRange.setUpperInclusive(getUpperInclusive());
        ObjectUtils.acceptSafely(getLowerBound(), bound -> localDateTimeRange.setLowerBound(bound.atTime(LocalTime.MIN)));
        ObjectUtils.acceptSafely(getUpperBound(), bound -> localDateTimeRange.setUpperBound(bound.atTime(LocalTime.MAX)));
        return localDateTimeRange;
    }


}
