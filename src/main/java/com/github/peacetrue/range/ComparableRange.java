package com.github.peacetrue.range;

import lombok.NoArgsConstructor;

/**
 * 可比较的范围。
 *
 * @author peace
 */
@NoArgsConstructor
public class ComparableRange<T extends Comparable<? super T>> extends Range<T> {

    /** {@inheritDoc} */
    public ComparableRange(T bound) {
        super(bound);
    }

    /** {@inheritDoc} */
    public ComparableRange(T lowerBound, T upperBound) {
        super(lowerBound, upperBound);
    }

    /** {@inheritDoc} */
    public ComparableRange(T lowerBound, T upperBound, Boolean lowerInclusive, Boolean upperInclusive) {
        super(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    /** {@inheritDoc} */
    public ComparableRange(Range<T> range) {
        super(range);
    }

    private static <T extends Comparable<? super T>> boolean compareTo(
            T value, T bound, boolean isLower, boolean isInclusive) {
        if (bound == null) return true;
        int compareTo = value.compareTo(bound);
        if (isLower) return isInclusive ? compareTo >= 0 : compareTo > 0;
        return isInclusive ? compareTo <= 0 : compareTo < 0;
    }

    /**
     * 在此范围中是否包含指定值。
     * <p>
     * {@code null} bound 表示不限，
     * <p>
     * {@code null} inclusive 表示包含。
     * <p>
     * 示例：
     * <pre>
     * [1,3].contains(1)=true
     * [1,3].contains(3)=true
     * (1,3).contains(1)=false
     * (1,3).contains(3)=false
     * [-∞,+∞].contains(anything)=true
     * (-∞,+∞).contains(anything)=true
     * </pre>
     *
     * @param value 用来判断是否在此范围中的值
     * @return true 如果在此范围中，否则 false
     */
    public boolean contains(T value) {
        return compareTo(value, getLowerBound(), true, isLowerInclusive(this))
                && compareTo(value, getUpperBound(), false, isUpperInclusive(this));
    }

}
