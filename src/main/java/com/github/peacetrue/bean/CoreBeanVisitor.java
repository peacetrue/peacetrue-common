package com.github.peacetrue.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * 核心 Bean 对象访问者，确定 {@link #visitAny(String, Object)} 核心逻辑。
 *
 * @author peace
 **/
@Getter
@SuperBuilder
public abstract class CoreBeanVisitor<T> implements BeanVisitor<T> {

    /** 是否叶子节点类型，默认为简单值类型。 */
    @Builder.Default
    private BiPredicate<String, Class<?>> isLeafType = CoreBeanVisitor::isSimpleValueType;

    @Override
    public T visitAny(@Nullable String path, @Nullable Object object) {
        if (object == null || isLeaf(path, object)) {
            return this.visitLeaf(path, object);
        } else {
            Class<?> clazz = object.getClass();
            if (Iterable.class.isAssignableFrom(clazz)) {
                return this.visitCollection(path, (Iterable<?>) object);
            } else if (clazz.isArray()) {
                return this.visitCollection(path, (Object[]) object);
            } else if (Map.class.isAssignableFrom(clazz)) {
                return this.visitParent(path, (Map<?, ?>) object);
            } else {
                return this.visitParent(path, object);
            }
        }
    }

    /**
     * 对象是否叶子节点。
     *
     * @param path   路径
     * @param object 对象
     * @return true 如果是叶子节点，否则 false
     */
    protected boolean isLeaf(@Nullable String path, Object object) {
        return this.isLeafType.test(path, object.getClass());
    }

    /**
     * 是否简单值类型。
     *
     * @param path  路径
     * @param clazz 类
     * @return true 如果是简单值类型，否则 false
     */
    public static boolean isSimpleValueType(@Nullable String path, Class<?> clazz) {
        return BeanUtils.isSimpleValueType(clazz);
    }

    /**
     * clazz 是否 classes 其中之一。
     *
     * @param classes 类数组
     * @return true 如果是子类，否则 false
     */
    public static BiPredicate<String, Class<?>> in(Class<?>... classes) {
        // path 为 null，此时 Bean 为根节点，根节点默认为非叶子节点，如果根节点作为叶子节点，失去遍历的意义了
        return (path, clazz) -> path != null && Arrays.stream(classes).anyMatch(item -> item.isAssignableFrom(clazz));
    }

    /**
     * clazz 是否 superclasses 其中之一的子类。
     *
     * @param superclasses 父类数组
     * @return true 如果是子类，否则 false
     */
    public static BiPredicate<String, Class<?>> isSubclassOf(Class<?>... superclasses) {
        return (path, clazz) -> BeanUtils.isSubclassOf(clazz, superclasses);
    }

}
