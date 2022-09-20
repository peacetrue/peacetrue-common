//package com.github.peacetrue.beanmap;
//
//import javax.annotation.Nullable;
//import java.util.*;
//
///**
// * 复制的属性访问者，该访问者可深度复制一个层级式对象。
// *
// * @author peace
// **/
//public class DuplicatePropertyVisitor implements SupplierPropertyVisitor<Map<String, Object>> {
//
//    /** 复制的 BeanMap */
//    protected Map<String, Object> duplicate;
//
//    /**
//     * 指定复制的 BeanMap 容量。
//     *
//     * @param size 复制的 BeanMap 容量
//     */
//    public DuplicatePropertyVisitor(int size) {
//        this(new LinkedHashMap<>(size));
//    }
//
//    /**
//     * 指定复制的 BeanMap。
//     *
//     * @param duplicate 复制的 BeanMap
//     */
//    public DuplicatePropertyVisitor(Map<String, Object> duplicate) {
//        this.duplicate = Objects.requireNonNull(duplicate);
//    }
//
//    @Override
//    public void visit(@Nullable String path, Object value) {
//        SupplierPropertyVisitor.super.visit(path, value);
//    }
//
//    @Override
//    public void visitBean(@Nullable String path, Map<String, Object> bean) {
//        SupplierPropertyVisitor.super.visitBean(path, bean);
//    }
//
//    @Override
//    public void visitBeanProperty(@Nullable String path, String name, Object value) {
//        SupplierPropertyVisitor.super.visitBeanProperty(path, name, value);
//    }
//
//    @Override
//    public void visitCollection(@Nullable String path, Collection<?> value) {
//        SupplierPropertyVisitor.super.visitCollection(path, value);
//    }
//
//    @Override
//    public void visitCollectionElement(@Nullable String path, int index, Object value) {
//        SupplierPropertyVisitor.super.visitCollectionElement(path, index, value);
//    }
//
//    @Override
//    public void visitPrimitive(@Nullable String path, @Nullable Object value) {
//
//    }
//
//    @Override
//    public void visitCollection(@Nullable String path, String name, @Nullable Object value) {
//        duplicate.put(name, value);
//    }
//
//    @Override
//    public void visit(@Nullable String path, String name, Map<String, Object> bean) {
//        Map<String, Object> temp = this.duplicate;
//        this.duplicate = prepareBean(name, bean.size());
//        BeanMapUtils.walkTree(bean, this);
//        this.duplicate = temp;
//    }
//
//    private Map<String, Object> prepareBean(String name, int size) {
//        Map<String, Object> bean = new LinkedHashMap<>(size);
//        this.duplicate.put(name, bean);
//        return bean;
//    }
//
//    @Override
//    public void visit(@Nullable String path, String name, Collection<Map<String, Object>> beans) {
//        Map<String, Object> temp = duplicate;
//        int i = 0;
//        List<Map<String, Object>> duplicates = prepareBeans(name, beans.size());
//        for (Map<String, Object> bean : beans) {
//            this.visit(path, name, i++, bean);
//            duplicates.add(this.duplicate);
//        }
//        this.duplicate = temp;
//    }
//
//    private List<Map<String, Object>> prepareBeans(String name, int size) {
//        List<Map<String, Object>> beans = new ArrayList<>(size);
//        this.duplicate.put(name, beans);
//        return beans;
//    }
//
//    @Override
//    public void visit(@Nullable String path, String name, int index, @Nullable Map<String, Object> bean) {
//        if (bean == null) {
//            this.duplicate = null;
//        } else {
//            this.duplicate = new LinkedHashMap<>(bean.size());
//            BeanMapUtils.walkTree(bean, this);
//        }
//    }
//
//    @Override
//    public Map<String, Object> get() {
//        return duplicate;
//    }
//}
