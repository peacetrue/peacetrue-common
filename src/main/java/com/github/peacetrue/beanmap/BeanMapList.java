package com.github.peacetrue.beanmap;

import java.util.ArrayList;
import java.util.Collection;

/**
 * BeanMap 列表。
 * <p>
 * 类型转换时，避免泛型。例如：用于作为 RestTemplate 获取返回结果的类型。
 *
 * @author peace
 **/
public class BeanMapList extends ArrayList<BeanMap> {

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public BeanMapList() {
        super();
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *                                  is negative
     */
    public BeanMapList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public BeanMapList(Collection<? extends BeanMap> c) {
        super(c);
    }
}
