package com.github.peacetrue.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * {@link Map} 工具类。
 *
 * @author peace
 **/
public class MapUtils {

    private MapUtils() {
    }

    /**
     * 通过数组键和值构造 Map。长度为键、值数组中的较小者，重复的键后者覆盖前者。
     *
     * @param keys   键数组
     * @param values 值数组
     * @param <K>    键类型
     * @param <V>    值类型
     * @return 通过数组键和值构造的 Map
     */
    public static <K, V> Map<K, V> from(K[] keys, V[] values) {
        int length = Math.min(keys.length, values.length);
        Map<K, V> map = new LinkedHashMap<>(length);
        IntStream.range(0, length).forEach(i -> map.put(keys[i], values[i]));
        return map;
    }

    /**
     * 从 Map 中获取指定键的值，值的排列顺序和键相同。
     * 用于集合 Map 转集合列表，构造二维数组表格，导出 Excel。
     *
     * @param maps 待取值的 Map 集合
     * @param keys 键数组
     * @param <K>  键类型
     * @param <V>  值类型
     * @return 值集合
     */
    @SafeVarargs
    public static <K, V> List<List<V>> values(Collection<Map<K, V>> maps, K... keys) {
        return maps.stream().map(bean -> values(bean, keys)).collect(Collectors.toList());
    }

    /**
     * 从 Map 中获取指定键的值，值的排列顺序和键相同。
     *
     * @param map  待取值的 Map
     * @param keys 键数组
     * @param <K>  键类型
     * @param <V>  值类型
     * @return 值集合
     */
    @SafeVarargs
    public static <K, V> List<V> values(Map<K, V> map, K... keys) {
        return Stream.of(keys).map(map::get).collect(Collectors.toList());
    }

}
