package com.github.peacetrue.beanmap;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.peacetrue.beanmap.BeanMapUtils.*;

/**
 * 扁平化属性访问器，可将层级式对象转换为扁平式对象。
 *
 * @author peace
 **/
public class FlatPropertyVisitor<T> implements SupplierPropertyVisitor<Map<String, T>> {

    /** 扁平的 BeanMap */
    protected final Map<String, T> flat;
    /** 属性路径前缀 */
    protected String propertyPathPrefix = "";

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
    public void visit(String name, @Nullable Object value) {
        flat.put(propertyPathPrefix + name, handleValue(value));
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
    public void visit(String name, Map<String, Object> bean) {
        String temp = this.propertyPathPrefix;
        this.propertyPathPrefix = buildBeanPropertyPathPrefix(this.propertyPathPrefix, name);
        SupplierPropertyVisitor.super.visit(name, bean);
        this.propertyPathPrefix = temp;
    }

    @Override
    public void visit(String name, int index, @Nullable Map<String, Object> bean) {
        String temp = this.propertyPathPrefix;
        this.propertyPathPrefix = buildBeansPropertyPathPrefix(this.propertyPathPrefix, name, index);
        SupplierPropertyVisitor.super.visit(name, index, bean);
        this.propertyPathPrefix = temp;
    }

    @Override
    public Map<String, T> get() {
        return flat;
    }

    private static String buildBeanPropertyPathPrefix(String propertyPathPrefix, String name) {
        //xxx.    user   .
        //prefix  name
        return propertyPathPrefix + name + BEAN_SEPARATOR;
    }

    private static String buildBeansPropertyPathPrefix(String propertyPathPrefix, String name, int index) {
        //xxx.    roles[   0   ] .
        //prefix  name   index
        return propertyPathPrefix + name + LIST_START_SEPARATOR + index + LIST_END_SEPARATOR + BEAN_SEPARATOR;
    }

}
