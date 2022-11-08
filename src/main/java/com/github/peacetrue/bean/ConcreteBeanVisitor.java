package com.github.peacetrue.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 具体的 Bean 对象访问者。
 *
 * @author peace
 **/
@Getter
@SuperBuilder
public abstract class ConcreteBeanVisitor<T> extends BaseBeanVisitor<T> {

    @Builder.Default
    private PropertyPathBuilder propertyPathBuilder = StandardPropertyPathBuilder.DEFAULT;

    @Override
    public T visitCollection(@Nullable String path, Object object, Stream<?> stream) {
        int[] i = {0};
        String objectPath = propertyPathBuilder.build(path, object);
        T target = stream
                .map(item -> this.visitAny(propertyPathBuilder.build(objectPath, null, i[0]++, item), item))
                .reduce(null, this::merge);
        // 如果 object 内没有任何元素，target 返回 null，此时将其作为叶子节点处理
        return target == null ? this.visitLeaf(path, null) : target;
    }

    @Override
    public T visitParent(@Nullable String path, Object object) {
        String objectPath = propertyPathBuilder.build(path, object);
        T target = getBeanTraverser()
                .buildPropertyStream(path, object)
                .map(property -> {
                    Object propertyValue = property.getValue(object);
                    String propertyPath = propertyPathBuilder.build(objectPath, property.getType(), property.getName(), propertyValue);
                    return this.visitAny(propertyPath, propertyValue);
                })
                .reduce(null, this::merge);
        // 如果 object 内没有任何属性，target 返回 null，此时将其作为叶子节点处理
        return target == null ? this.visitLeaf(path, object) : target;
    }

    @Override
    public T visitParent(@Nullable String path, Object object, Map<?, ?> map) {
        String objectPath = propertyPathBuilder.build(path, object);
        T target = map.entrySet().stream()
                .map(entry -> {
                    String propertyPath = propertyPathBuilder.build(objectPath, null, String.valueOf(entry.getKey()), entry.getValue());
                    return this.visitAny(propertyPath, entry.getValue());
                })
                .reduce(null, this::merge);
        // 如果 object 内没有任何属性，target 返回 null，此时将其作为叶子节点处理
        return target == null ? this.visitLeaf(path, map) : target;
    }

    /**
     * 合并目标对象。
     *
     * @param one 目标对象 1
     * @param two 目标对象 2
     * @return 合并后的目标对象
     */
    @Nullable
    protected abstract T merge(@Nullable T one, @Nullable T two);

}
