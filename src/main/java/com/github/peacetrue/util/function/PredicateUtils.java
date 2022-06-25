package com.github.peacetrue.util.function;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * 断言工具类。
 *
 * @author peace
 **/
public abstract class PredicateUtils {

    private PredicateUtils() {
    }

    /**
     * 返回值始终为 {@code true} 的单参数断言。
     *
     * @param head 首部参数
     * @param <T>  首部参数类型
     * @return {@code true}
     */
    public static <T> boolean alwaysTrue(T head) {
        return true;
    }

    /**
     * 返回值始终为 {@code true} 的双参数断言。
     *
     * @param head 首部参数
     * @param tail 尾部参数
     * @param <T>  首部参数类型
     * @param <U>  尾部参数类型
     * @return {@code true}
     */
    public static <T, U> boolean alwaysTrue(T head, U tail) {
        return true;
    }

    /**
     * 取反断言。
     * <p>
     * 使用场景：
     * <pre>
     *     private static boolean isSuccess(Object anything) {
     *         return true;
     *     }
     *     //想对一个 Predicate 取反，直接这么写是不支持的：PredicateUtilsTest::isSuccess.negate()
     *     //得先声明一个 Predicate 的变量，再调用变量的方法，因为 lambda 函数的类型是不确定的
     *     Predicate&lt;Object&gt; predicate = PredicateUtilsTest::isSuccess;
     *     Predicate&lt;Object&gt; negatePredicate = predicate.negate();
     *     //你可以使用 negate 方法实现单行式写法
     *     Predicate&lt;Object&gt; oneLineCode = PredicateUtils.negate(PredicateUtilsTest::isSuccess);
     *     //PredicateUtilsTest::isSuccess 同时也可以赋值给 Function
     *     Function&lt;Object, Boolean&gt; function = PredicateUtilsTest::isSuccess;
     * </pre>
     *
     * @param predicate 断言
     * @param <T>       断言参数类型
     * @return 反向断言
     */
    public static <T> Predicate<T> negate(Predicate<T> predicate) {
        return predicate.negate();
    }

    /**
     * 将单参数断言扩展为双参数断言，实际使用首位参数。
     *
     * @param predicate 断言
     * @param <T>       断言首部参数的类型
     * @param <U>       断言尾部参数的类型
     * @return 二个参数的断言
     */
    public static <T, U> BiPredicate<T, U> headConvert(Predicate<T> predicate) {
        return (head, tail) -> predicate.test(head);
    }

    /**
     * 将单参数断言扩展为双参数断言，实际使用尾部参数。
     *
     * @param predicate 断言
     * @param <T>       断言第一个参数的类型
     * @param <U>       断言第二个参数的类型
     * @return 二个参数的断言
     */
    public static <T, U> BiPredicate<T, U> tailConvert(Predicate<U> predicate) {
        return (head, tail) -> predicate.test(tail);
    }

}
