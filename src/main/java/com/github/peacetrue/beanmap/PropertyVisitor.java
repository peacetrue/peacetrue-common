package com.github.peacetrue.beanmap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

import static com.github.peacetrue.beanmap.BeanMapUtils.*;

/**
 * 属性访问者，访问对象中的所有属性。
 * <p>
 * 属性分为 3 种类型：
 * <ul>
 *     <li>1. 原始类型，除以下两种类型外的其他类型</li>
 *     <li>2. 对象类型（Map&lt;String, Object&gt;）</li>
 *     <li>3. 集合类型（Collection&lt;?&gt;）</li>
 * </ul>
 *
 * @author peace
 **/
public interface PropertyVisitor {

    /**
     * 访问任意类型属性。
     *
     * @param path  属性路径
     * @param value 属性值
     */
    @SuppressWarnings("unchecked")
    default void visit(@Nullable String path, Object value) {
        if (isBean(value)) {
            visitBean(path, (Map<String, Object>) value);
        } else if (isCollection(value)) {
            visitCollection(path, (Collection<?>) value);
        } else {
            visitPrimitive(path, value);
        }
    }

    /**
     * 访问 Bean 属性。
     *
     * @param path 属性路径
     * @param bean 泛化对象属性值
     */
    default void visitBean(@Nullable String path, Map<String, Object> bean) {
        for (Map.Entry<String, Object> entry : bean.entrySet()) {
            visitBeanProperty(path, entry.getKey(), entry.getValue());
        }
    }

    /**
     * 访问 Bean 属性的属性。
     *
     * @param path  属性路径
     * @param name  属性名
     * @param value 属性值
     */
    default void visitBeanProperty(@Nullable String path, String name, Object value) {
        visit(concatSafely(path, name), value);
    }

    /**
     * 访问集合属性。
     *
     * @param path  属性路径
     * @param value 集合属性值
     */
    default void visitCollection(@Nullable String path, Collection<?> value) {
        int i = 0;
        for (Object element : value) {
            visitCollectionElement(path, i++, element);
        }
    }

    /**
     * 访问集合属性的元素。
     *
     * @param path  属性路径
     * @param index 索引
     * @param value 属性值
     */
    default void visitCollectionElement(@Nullable String path, int index, Object value) {
        visit(concatSafely(path, index), value);
    }


    /**
     * 访问原始属性。
     *
     * @param path  属性路径
     * @param value 属性值
     */
    void visitPrimitive(@Nullable String path, @Nullable Object value);

}
