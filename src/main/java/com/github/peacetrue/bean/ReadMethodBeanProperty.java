package com.github.peacetrue.bean;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

/**
 * 读取方法 Bean 属性。
 *
 * @author peace
 **/
@RequiredArgsConstructor
public class ReadMethodBeanProperty implements ReadonlyBeanProperty {

    /** 属性值的读取方法 */
    private final Method readMethod;

    @Override
    public String getName() {
        return BeanUtils.formatGetter(readMethod.getName());
    }

    @Override
    public Class<?> getType() {
        return readMethod.getReturnType();
    }

    @Override
    public Object getValue(Object object) {
        return BeanUtils.invoke(object, readMethod);
    }
}
