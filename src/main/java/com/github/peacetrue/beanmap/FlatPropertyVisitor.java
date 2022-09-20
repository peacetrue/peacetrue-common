package com.github.peacetrue.beanmap;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 扁平化属性访问器，可将层级式对象转换为扁平式对象。
 *
 * @author peace
 **/
public class FlatPropertyVisitor<T> implements SupplierPropertyVisitor<Map<String, T>> {

    /** 扁平的 BeanMap */
    protected final Map<String, T> flat;

    /**
     * 指定扁平的 BeanMap 容量。
     *
     * @param size 扁平的 BeanMap 容量
     */
    public FlatPropertyVisitor(int size) {
        this(new LinkedHashMap<>(size));
    }

    /**
     * 指定扁平的 BeanMap 初始值。
     *
     * @param flat 扁平的 BeanMap 初始值
     */
    public FlatPropertyVisitor(Map<String, T> flat) {
        this.flat = Objects.requireNonNull(flat);
    }

    @Override
    public void visitPrimitive(@Nullable String path, @Nullable Object value) {
        flat.put(path, handleValue(value));
    }

    /**
     * 处理属性值。属性值为对象类型，可转换为指定值后返回。
     *
     * @param value 属性值
     * @return 处理后的属性值。
     */
    @Nullable
    @SuppressWarnings("unchecked")
    protected T handleValue(@Nullable Object value) {
        return (T) value;
    }

    @Override
    public Map<String, T> get() {
        return flat;
    }


}
