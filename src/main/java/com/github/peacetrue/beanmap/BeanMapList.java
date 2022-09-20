package com.github.peacetrue.beanmap;

import java.util.Collection;
import java.util.LinkedList;

/**
 * BeanMap 列表。
 * <p>
 * 类型转换时，避免泛型。例如：用作 {@code RestTemplate} 获取返回结果的类型。
 *
 * @author peace
 **/
public class BeanMapList extends LinkedList<BeanMap> {

    /** 构造一个空列表。 */
    public BeanMapList() {
        super();
    }

    /**
     * 构造一个包含初始值的列表。
     *
     * @param c 初始列表
     */
    public BeanMapList(Collection<? extends BeanMap> c) {
        super(c);
    }
}
