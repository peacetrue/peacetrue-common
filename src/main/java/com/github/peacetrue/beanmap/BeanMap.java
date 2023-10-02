package com.github.peacetrue.beanmap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 对于类型不确定的 Bean，可以使用 Map 来表示，称之为 BeanMap。
 * <p>
 * 类型转换时，避免泛型。可用作 Spring RestTemplate 获取返回结果的类型。
 *
 * @author peace
 **/
public class BeanMap extends LinkedHashMap<String, Object> {

    /**
     * Constructs an empty insertion-ordered <code>LinkedHashMap</code> instance
     * with the default initial capacity (16) and load factor (0.75).
     */
    public BeanMap() {
        super();
    }

    /**
     * Constructs an insertion-ordered <code>LinkedHashMap</code> instance with
     * the same mappings as the specified map.  The <code>LinkedHashMap</code>
     * instance is created with a default load factor (0.75) and an initial
     * capacity sufficient to hold the mappings in the specified map.
     *
     * @param m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public BeanMap(Map<String, ?> m) {
        super(m);
    }

    /**
     * Constructs an empty insertion-ordered <code>LinkedHashMap</code> instance
     * with the specified initial capacity and a default load factor (0.75).
     *
     * @param initialCapacity the initial capacity
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    public BeanMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty insertion-ordered <code>LinkedHashMap</code> instance
     * with the specified initial capacity and load factor.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  or the load factor is nonpositive
     */
    public BeanMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructs an empty <code>LinkedHashMap</code> instance with the
     * specified initial capacity, load factor and ordering mode.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor      the load factor
     * @param accessOrder     the ordering mode - <code>true</code> for
     *                        access-order, <code>false</code> for insertion-order
     * @throws IllegalArgumentException if the initial capacity is negative
     *                                  or the load factor is nonpositive
     */
    public BeanMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }
}
