package com.github.peacetrue.beanmap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * 属性访问者，访问对象中的所有属性。
 * <p>
 * 属性分为 3 种类型：
 * <ul>
 *     <li>1. 简单类型，除以下两种类型外的其他类型</li>
 *     <li>2. 对象类型（Map&lt;String, Object&gt;）</li>
 *     <li>3. 集合对象类型（Collection&lt;Map&lt;String, Object&gt;&gt;）</li>
 * </ul>
 *
 * @author peace
 **/
public interface PropertyVisitor {

    /**
     * 访问一个简单属性，位于叶子节点。
     *
     * @param name  属性名
     * @param value 属性值
     */
    void visit(String name, @Nullable Object value);

    /**
     * 访问一个对象属性。
     *
     * @param name 属性名
     * @param bean 对象属性值
     */
    default void visit(String name, Map<String, Object> bean) {
        BeanMapUtils.walkTree(bean, this);
    }

    /**
     * 访问一个集合对象属性。
     *
     * @param name  属性名
     * @param beans 集合对象属性值
     */
    default void visit(String name, Collection<Map<String, Object>> beans) {
        int i = 0;
        for (Map<String, Object> bean : beans) {
            this.visit(name, i++, bean);
        }
    }

    /**
     * 访问一个集合对象中的对象元素。
     *
     * @param name  属性名
     * @param index 索引
     * @param bean  对象属性值
     */
    default void visit(String name, int index, @Nullable Map<String, Object> bean) {
        if (bean != null) BeanMapUtils.walkTree(bean, this);
    }
}
