package com.github.peacetrue.bean;

import lombok.RequiredArgsConstructor;

import java.beans.PropertyDescriptor;

/**
 * {@link  PropertyDescriptor} Bean 属性。
 *
 * @author peace
 **/
@RequiredArgsConstructor
public class DescriptorBeanProperty implements BeanProperty {

    /** 属性描述信息。 */
    private final PropertyDescriptor propertyDescriptor;

    @Override
    public String getName() {
        return propertyDescriptor.getName();
    }

    @Override
    public Class<?> getType() {
        return propertyDescriptor.getPropertyType();
    }

    @Override
    public Object getValue(Object object) {
        return BeanUtils.invoke(object, propertyDescriptor.getReadMethod());
    }

    @Override
    public void setValue(Object object, Object propertyValue) {
        BeanUtils.invoke(object, propertyDescriptor.getWriteMethod(), propertyValue);
    }
}
