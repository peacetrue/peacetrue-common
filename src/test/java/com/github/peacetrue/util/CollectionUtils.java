package com.github.peacetrue.util;


import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

/**
 * 集合工具类。
 *
 * @author peace
 */
public abstract class CollectionUtils {

    /**
     * 安全地获取集合的首位元素。
     *
     * @param iterable 集合
     * @param <T>      集合中元素类型
     * @return 首位元素
     */
    @Nullable
    public static <T> T firstSafely(@Nullable Iterable<T> iterable) {
        if (iterable == null) return null;
        Iterator<T> iterator = iterable.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
     * 安全地获取集合的首位元素。
     *
     * @param list 集合
     * @param <T>  集合中元素类型
     * @return 首位元素
     */
    @Nullable
    public static <T> T firstSafely(@Nullable List<T> list) {
        if (list == null) return null;
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 安全地获取集合的末尾元素。
     *
     * @param list 集合
     * @param <T>  集合中元素类型
     * @return 末尾元素
     */
    @Nullable
    public static <T> T lastSafely(@Nullable List<T> list) {
        if (list == null || list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }

}
