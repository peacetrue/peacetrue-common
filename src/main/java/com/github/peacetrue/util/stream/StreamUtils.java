package com.github.peacetrue.util.stream;

import com.github.peacetrue.lang.ObjectUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 数据流工具类。
 * <p>
 * 以 {@link Stream} 为基础，提供一个集合编程的统一接口，将所有可流化数据转换为 {@link Stream} 数据流。
 * 可流化数据类型及其转换规则：
 * <ul>
 *     <li>{@link Iterable}   ： {@link Iterable#iterator()} {@link #iteratorAsStream(Iterator)}</li>
 *     <li>{@link Object[]}   ： {@link Stream#of(Object[])}</li>
 *     <li>{@link Iterator}   ： {@link #iteratorAsStream(Iterator)}</li>
 *     <li>{@link Enumeration}： {@link #enumerationAsStream(Enumeration)}</li>
 *     <li>{@link Stream}     ： {@link Stream}</li>
 * </ul>
 *
 * @author peace
 **/
public class StreamUtils {

    private StreamUtils() {
    }

    /**
     * 转换输入数据为数据流。
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 如果输入为 {@code null}，返回空数据流；如果输入不是一个可流化数据，转换此输入数据为单元素数据流；其他参考可流化数据转换规则。
     */
    @SuppressWarnings("unchecked")
    public static <T> Stream<T> toStreamSafely(@Nullable Object data) {
        return toStreamSafely(data, Stream.empty(), temp -> Stream.of((T) temp));
    }

    /**
     * 转换输入数据为数据流。
     *
     * @param data      数据
     * @param defaults  数据为 {@code null} 时，使用的默认值
     * @param converter 数据不是可流化数据时，使用的转换器
     * @param <T>       数据类型
     * @return 数据流
     */
    public static <T> Stream<T> toStreamSafely(@Nullable Object data, Stream<T> defaults,
                                               Function<Object, Stream<T>> converter) {
        return ObjectUtils.invokeSafely(data, defaults, temp -> toStream(temp, converter));
    }

    /**
     * 转换输入数据为数据流。
     *
     * @param data      数据
     * @param defaults  数据为 {@code null} 时，使用的延迟默认值，
     * @param converter 数据不是可流化数据时，使用的转换器
     * @param <T>       数据类型
     * @return 数据流
     */
    public static <T> Stream<T> toStreamSafelyLazily(@Nullable Object data, Supplier<Stream<T>> defaults,
                                                     Function<Object, Stream<T>> converter) {
        return ObjectUtils.invokeSafelyLazily(data, defaults, temp -> toStream(temp, converter));
    }

    /**
     * 转换一个对象为数据流，如果不是可流化数据，返回 {@code null}。
     *
     * @param object 对象
     * @param <T>    数据类型
     * @return 数据流
     */
    @Nullable
    public static <T> Stream<T> toStreamNullable(Object object) {
        return toStream(object, temp -> null);
    }

    /**
     * 转换一个可流化数据为数据流。
     *
     * @param streamable 可流化数据
     * @param <T>        数据类型
     * @return 数据流
     * @throws UnsupportedOperationException 如果入参不是一个可流化数据，抛出此异常
     */
    public static <T> Stream<T> toStream(Object streamable) throws UnsupportedOperationException {
        return toStream(streamable, temp -> {
            String message = "Parameter streamable must be one of Stream, Object[], Collection, Iterator, Iterable," +
                    " Type " + streamable.getClass().getName() + " is not supported";
            throw new UnsupportedOperationException(message);
        });
    }

    /**
     * 转换输入数据为数据流。
     *
     * @param data      数据
     * @param converter 数据为非流化数据时，使用的转换器
     * @param <T>       数据类型
     * @return 数据流
     */
    @SuppressWarnings("unchecked")
    public static <T> Stream<T> toStream(Object data, Function<Object, Stream<T>> converter) {
        Objects.requireNonNull(data, "data must not be null");
        if (data instanceof Iterable) return (Stream<T>) iteratorAsStream(((Iterable<?>) data).iterator());
        if (data instanceof Object[]) return (Stream<T>) Stream.of((Object[]) data);
        if (data instanceof Enumeration) return (Stream<T>) enumerationAsStream((Enumeration<?>) data);
        if (data instanceof Iterator) return (Stream<T>) iteratorAsStream((Iterator<?>) data);
        if (data instanceof Stream) return (Stream<T>) data;
        return converter.apply(data);
    }

    /**
     * 转换迭代器为数据流。
     *
     * @param iterator 迭代器
     * @param <T>      数据类型
     * @return 数据流
     */
    public static <T> Stream<T> iteratorAsStream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    /**
     * 转换列举对象为数据流。
     *
     * @param enumeration 列举对象
     * @param <T>         元素类型
     * @return 数据流
     */
    public static <T> Stream<T> enumerationAsStream(Enumeration<T> enumeration) {
        return iteratorAsStream(enumerationAsIterator(enumeration));
    }

    /**
     * 列举对象转换为迭代器。
     *
     * @param enumeration 列举对象
     * @param <T>         元素对象
     * @return 迭代器
     */
    public static <T> Iterator<T> enumerationAsIterator(Enumeration<T> enumeration) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                return enumeration.nextElement();
            }
        };
    }

    /**
     * 转换输入数据为数据流。
     *
     * @param data          数据
     * @param streamInvoker 数据流调用者
     * @param dataInvoker   非数据流调用者
     * @param <T>           数据类型
     * @param <E>           流中元素类型
     * @param <R>           返回值类型
     * @return 数据流
     */
    @SuppressWarnings("unchecked")
    public static <T, E, R> R invoke(T data,
                                     Function<Stream<E>, R> streamInvoker,
                                     Function<T, R> dataInvoker) {
        return ObjectUtils.invokeSafelyLazily(
                toStreamNullable(data),
                () -> dataInvoker.apply(data),
                stream -> streamInvoker.apply((Stream<E>) stream)
        );
    }


}
