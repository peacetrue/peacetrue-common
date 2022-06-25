package com.github.peacetrue.range;

import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 日期范围。
 *
 * @author peace
 */
@NoArgsConstructor
public class DateRange extends ComparableRange<Date> implements Serializable {

    private static final long serialVersionUID = 0L;

    public static final DateRange DEFAULT = new DateRange();

    public DateRange(Date bound) {
        super(bound);
    }

    public DateRange(Date lowerBound, Date upperBound) {
        super(lowerBound, upperBound);
    }

    public DateRange(Date lowerBound, Date upperBound, Boolean lowerInclusive, Boolean upperInclusive) {
        super(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    public DateRange(Range<Date> range) {
        super(range);
    }
}
