package com.github.peacetrue.bean;

import javax.annotation.Nullable;

/**
 * 带类型的属性路径构建者。
 *
 * @author peace
 **/
public class TypedPropertyPathBuilder implements PropertyPathBuilder {

    /** 默认实例。 */
    public static final TypedPropertyPathBuilder DEFAULT = new TypedPropertyPathBuilder();

    private static String toString(String path, Class<?> clazz) {
        return path + "(" + clazz.getSimpleName() + ")";
    }

    @Override
    public String build(@Nullable String path, @Nullable Object object) {
        if (path == null) return null;
        if (object == null) return path;
        return toString(path, object.getClass());
    }

    @Override
    public String build(@Nullable String path, @Nullable Class<?> elementType, int index, @Nullable Object element) {
        path = BeanUtils.concatSafely(path, index);
        if (element == null && elementType != null) path = toString(path, elementType);
        return path;
    }

    @Override
    public String build(@Nullable String path, @Nullable Class<?> propertyType, String propertyName, @Nullable Object propertyValue) {
        path = BeanUtils.concatSafely(path, propertyName);
        if (propertyValue == null && propertyType != null) path = toString(path, propertyType);
        return path;
    }
}
