package com.github.peacetrue.bean;

import javax.annotation.Nullable;

/**
 * 标准属性路径构建者。
 *
 * @author peace
 **/
public class StandardPropertyPathBuilder implements PropertyPathBuilder {

    /** 默认实例。 */
    public static final StandardPropertyPathBuilder DEFAULT = new StandardPropertyPathBuilder();

    @Override
    public String build(@Nullable String path, Object object) {
        return path;
    }

    @Override
    public String build(@Nullable String path, Class<?> elementType, int index, @Nullable Object element) {
        return BeanUtils.concatSafely(path, index);
    }

    @Override
    public String build(@Nullable String path, Class<?> propertyType, String propertyName, @Nullable Object propertyValue) {
        return BeanUtils.concatSafely(path, propertyName);
    }
}
