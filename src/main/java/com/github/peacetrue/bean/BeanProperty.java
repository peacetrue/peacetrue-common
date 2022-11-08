package com.github.peacetrue.bean;

import javax.annotation.Nullable;

/**
 * 可读可写 Bean 属性。
 *
 * @author peace
 **/
public interface BeanProperty extends ReadonlyBeanProperty {

    /**
     * 设置属性值。
     *
     * @param object        对象
     * @param propertyValue 属性值
     */
    void setValue(Object object, @Nullable Object propertyValue);
}
