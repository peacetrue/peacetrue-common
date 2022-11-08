package com.github.peacetrue.bean;

import javax.annotation.Nullable;

/**
 * 属性路径构建者。
 *
 * @author peace
 **/
public interface PropertyPathBuilder {

    /**
     * 构建对象路径。
     *
     * @param path   路径
     * @param object 对象
     * @return 当前对象对应的路径
     */
    String build(@Nullable String path, @Nullable Object object);

    /**
     * 构建集合元素路径。
     *
     * @param path        路径
     * @param elementType 元素类型
     * @param index       索引
     * @param element     元素对象
     * @return 当前元素对应的路径
     */
    String build(@Nullable String path, @Nullable Class<?> elementType, int index, @Nullable Object element);

    /**
     * 构建对象属性路径。
     *
     * @param path          路径
     * @param propertyType  属性类型
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     * @return 当前属性对应的路径
     */
    String build(@Nullable String path, @Nullable Class<?> propertyType, String propertyName, @Nullable Object propertyValue);

}
