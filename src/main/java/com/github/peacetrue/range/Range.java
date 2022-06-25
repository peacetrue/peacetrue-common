package com.github.peacetrue.range;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * 一个表示范围的值对象。
 * <p>
 * 示例：
 * <pre>
 * [1,2]：范围 1~2，包含边界值
 * (1,2)：范围 1~2，不包含边界值
 * [-∞,+∞]：范围不限（值为 {@code null}），包含边界值
 * </pre>
 *
 * @param <T> 范围类型，必须是可序列化的
 * @author peace
 */
@Getter
@Setter
public class Range<T> {

    /** 下边界 */
    private T lowerBound;
    /** 上边界 */
    private T upperBound;
    /** 是否包含下边界 */
    private Boolean lowerInclusive;
    /** 是否包含上边界 */
    private Boolean upperInclusive;

    /**
     * 构造一个范围对象，不设置任何属性，随后通过 Getter/Setter 方法设置。
     * 通过反射构造对象时，需要此构造器，例如：Spring 框架自动注入请求对象。
     */
    public Range() {
    }

    /**
     * 类似于 {@link #Range(Object, Object)}，
     * {@link #lowerBound} 和 {@link #upperBound} 都设置为相同值。
     *
     * @param bound 边界
     */
    public Range(T bound) {
        this(bound, bound);
    }

    /**
     * 类似于 {@link #Range(Object, Object, Boolean, Boolean)}，
     * {@link #lowerInclusive} 和 {@link #upperInclusive} 都设置为 {@code true}。
     *
     * @param lowerBound 下边界
     * @param upperBound 上边界
     */
    public Range(T lowerBound, T upperBound) {
        this(lowerBound, upperBound, true, true);
    }

    /**
     * 根据指定的参数，构造范围对象。完全自定义，手动设置全部参数。
     *
     * @param lowerBound     下边界
     * @param upperBound     上边界
     * @param lowerInclusive 是否包含下边界
     * @param upperInclusive 是否包含上边界
     */
    public Range(T lowerBound, T upperBound, Boolean lowerInclusive, Boolean upperInclusive) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.lowerInclusive = lowerInclusive;
        this.upperInclusive = upperInclusive;
    }

    /**
     * 构造一个与指定范围对象相同的范围对象，即拷贝。
     *
     * @param range 范围对象
     */
    public Range(Range<T> range) {
        this(range.lowerBound, range.upperBound, range.lowerInclusive, range.upperInclusive);
    }

    /**
     * 范围对象是否为空，上下边界都为 {@code null}。
     *
     * @return {@code true} 如果为空，否则 {@code false}
     */
    public boolean isEmpty() {
        return lowerBound == null && upperBound == null;
    }

    /**
     * 范围对象是否满的，上下边界都不为 {@code null}。
     *
     * @return {@code true} 如果为满，否则 {@code false}
     */
    public boolean isFull() {
        return lowerBound != null && upperBound != null;
    }

    /**
     * 是否包含下边界，{@link #lowerInclusive} 不是 false 即为包含。
     *
     * @return true 如果包含，否则 false
     */
    public boolean isLowerInclusive() {
        return !Boolean.FALSE.equals(lowerInclusive);
    }

    /**
     * 是否包含上边界，{@link #upperInclusive} 不是 false 即为包含。
     *
     * @return true 如果包含，否则 false
     */
    public boolean isUpperInclusive() {
        return !Boolean.FALSE.equals(upperInclusive);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range<?> range = (Range<?>) o;
        return Objects.equals(lowerBound, range.lowerBound)
                && Objects.equals(upperBound, range.upperBound)
                && Objects.equals(lowerInclusive, range.lowerInclusive)
                && Objects.equals(upperInclusive, range.upperInclusive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound, lowerInclusive, upperInclusive);
    }

    @Override
    public String toString() {
        return String.format("%s%s, %s%s",
                isLowerInclusive() ? '[' : '(',
                Objects.toString(lowerBound, "-∞"),
                Objects.toString(upperBound, "+∞"),
                isUpperInclusive() ? ']' : ')'
        );
    }
}
