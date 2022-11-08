package com.github.peacetrue.bean;

import javax.annotation.Nullable;

/**
 * 只读 Bean 属性。
 *
 * @author peace
 */
public interface ReadonlyBeanProperty {

    /**
     * 获取属性名。
     *
     * @return 属性名
     */
    String getName();

    /**
     * 获取属性类型。
     *
     * @return 属性类型
     */
    Class<?> getType();

    /**
     * 获取属性值。
     *
     * @param object 对象
     * @return 属性值
     */
    @Nullable
    Object getValue(Object object);

}
