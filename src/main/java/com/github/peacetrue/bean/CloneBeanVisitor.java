package com.github.peacetrue.bean;

import com.github.peacetrue.util.stream.StreamUtils;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 克隆 Bean 对象访问者。
 * <p>
 * 深度克隆一个原始对象，返回原始对象的副本，原始对象需满足一定条件：
 * <ul>
 *     <li>原始对象及其关联对象，需提供公开无参构造方法，用于反射实例化</li>
 *     <li>属性提供了读取方法，同时需提供写入方法；如果没有提供写入方法，无法写入对应属性值，最终忽略此属性</li>
 *     <li>集合提供了遍历元素的方法，同时需提供添加元素的方法；如果没有提供添加元素的方法，无法复制集合，最终忽略此属性</li>
 * </ul>
 * <p>
 * 如果不满足以上条件，有两种处理模式：
 * <ul>
 *     <li>严格模式，抛出异常并终止程序</li>
 *     <li>宽松模式，日志输出警告，并忽略此属性</li>
 * </ul>
 *
 * @author peace
 **/
@SuperBuilder
public class CloneBeanVisitor extends CoreBeanVisitor<Object> {

    /** 默认实例。 */
    public static final CloneBeanVisitor DEFAULT = CloneBeanVisitor.builder().build();

    /** 对象及其声明类型。 */
    private static final ThreadLocal<Map<Object, Class<?>>> DECLARE_CLASSES = new ThreadLocal<>();
    @Builder.Default
    private BeanTraverser<BeanProperty> beanTraverser = StandardBeanTraverser.NONNULL_WRITE_METHOD;
    @Builder.Default
    private InstanceFactory instanceFactory = DefaultInstanceFactory.DEFAULT;

    private static Class<?> getDeclareClass(Object object) {
        return DECLARE_CLASSES.get().getOrDefault(object, object.getClass());
    }

    @Override
    public Object visitRoot(Object object) {
        try {
            DECLARE_CLASSES.set(new HashMap<>());
            return super.visitRoot(object);
        } finally {
            DECLARE_CLASSES.remove();
        }
    }

    @Nullable
    @Override
    public Object visitLeaf(@Nullable String path, @Nullable Object object) {
        return object;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object visitCollection(@Nullable String path, Iterable<?> iterable) {
        // java.util.Arrays$ArrayList
        if (Collection.class.isAssignableFrom(iterable.getClass())) {
            int[] i = {0};
            return StreamUtils.iteratorAsStream(iterable.iterator())
                    .map(item -> this.visitAny(BeanUtils.concatSafely(path, i[0]++), item))
                    .collect(Collectors.toCollection(() ->
                            instanceFactory.createInstance(Collection.class, ((Collection) iterable))
                    ));
        }
        throw new IllegalStateException("the iterable source object must be Collection to add element");
    }

    @Override
    public Object visitCollection(@Nullable String path, Object[] array) {
        Object[] clone = array.clone();
        for (int i = 0; i < clone.length; i++) {
            clone[i] = this.visitAny(BeanUtils.concatSafely(path, i), clone[i]);
        }
        return clone;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object visitParent(@Nullable String path, Object object) {
        Map<Object, Class<?>> map = DECLARE_CLASSES.get();
        Class declareClass = map.getOrDefault(object, object.getClass());
        Object instance = instanceFactory.createInstance(declareClass, object);
        beanTraverser.buildPropertyStream(path, instance).forEach(beanProperty -> {
            Object propertyValue = beanProperty.getValue(object);
            if (propertyValue == null) return;
            map.put(propertyValue, beanProperty.getType());
            beanProperty.setValue(instance, this.visitAny(
                    BeanUtils.concatSafely(path, beanProperty.getName()), propertyValue
            ));
        });
        return instance;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object visitParent(@Nullable String path, Map<?, ?> object) {
        Map instance = instanceFactory.createInstance(Map.class, object);
        instance.putAll(object);
        return instance;
    }

}
