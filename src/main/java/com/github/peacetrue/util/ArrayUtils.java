package com.github.peacetrue.util;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数组工具类。
 *
 * @author peace
 **/
public abstract class ArrayUtils {

    private ArrayUtils() {
    }

    /**
     * 判断数组是否为空。
     *
     * @param array 数组
     * @return true 如果为空。
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 安全地获取数组的首位元素。
     *
     * @param array 数组
     * @param <T>   数组中元素类型
     * @return 首位元素
     */
    @Nullable
    public static <T> T firstSafely(@Nullable T[] array) {
        return isEmpty(array) ? null : array[0];
    }

    /**
     * 安全地获取数组的末尾元素。
     *
     * @param array 数组
     * @param <T>   数组中元素类型
     * @return 末尾元素
     */
    @Nullable
    public static <T> T lastSafely(@Nullable T[] array) {
        return isEmpty(array) ? null : array[array.length - 1];
    }

    /**
     * 替换数组中的元素，修改原始数组，不重新生成数组。
     *
     * @param array    数组
     * @param replacer 替换者
     * @param <T>      数组中元素的类型
     * @return 输入时的数组
     */
    public static <T> T[] replace(T[] array, UnaryOperator<T> replacer) {
        for (int i = 0; i < array.length; i++) {
            array[i] = replacer.apply(array[i]);
        }
        return array;
    }


}
