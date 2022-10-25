package com.github.peacetrue.util.function;

import java.util.Collection;
import java.util.function.*;

/**
 * 函数式编程工具类。
 *
 * @author peace
 */
public class FunctionUtils {

    private FunctionUtils() {
    }

    /**
     * 一元消费者转二元消费者。
     *
     * @param consumer 一元消费者
     * @param <T>      参数一类型
     * @param <U>      参数二类型
     * @return 二元消费者
     */
    public static <T, U> BiConsumer<T, U> toBiConsumer(Consumer<T> consumer) {
        return (t, o) -> consumer.accept(t);
    }

    /**
     * 一元判断者转二元判断者。
     *
     * @param predicate 一元判断者
     * @param <T>       参数一类型
     * @param <U>       参数二类型
     * @return 二元判断者
     */
    public static <T, U> BiPredicate<T, U> toBiPredicate(Predicate<T> predicate) {
        return (t, o) -> predicate.test(t);
    }

    /**
     * 获取返回参数二的二元操作者。
     *
     * @param <T> 参数类型
     * @return 二元操作者
     */
    public static <T> BinaryOperator<T> rightBinaryOperator() {
        return (t, t2) -> t2;
    }


    /**
     * 一元消费者转二元操作者。
     *
     * @param consumer 一元消费者
     * @param <T>      参数类型
     * @return 二元操作者
     */
    public static <T> UnaryOperator<T> fromConsumer(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }

    /**
     * 二元消费者转二元函数，返回参数一。
     *
     * @param biConsumer 二元消费者
     * @param <T>        参数一类型
     * @param <U>        参数二类型
     * @return 二元函数
     */
    public static <T, U> BiFunction<T, U, T> toBiFunctionLeft(BiConsumer<T, U> biConsumer) {
        return (t, u) -> {
            biConsumer.accept(t, u);
            return t;
        };
    }

    /**
     * 二元消费者转二元函数，返回参数二。
     *
     * @param biConsumer 二元消费者
     * @param <T>        参数一类型
     * @param <U>        参数二类型
     * @return 二元函数
     */
    public static <T, U> BiFunction<T, U, U> toBiFunctionRight(BiConsumer<T, U> biConsumer) {
        return (t, u) -> {
            biConsumer.accept(t, u);
            return u;
        };
    }

    /**
     * 向集合中添加元素。
     *
     * @param <T> 集合类型
     * @param <U> 集合中元素类型
     * @return 集合元素添加器
     */
    public static <T extends Collection<U>, U> BiFunction<T, U, T> reduceToCollection() {
        return toBiFunctionLeft(Collection::add);
    }

}
