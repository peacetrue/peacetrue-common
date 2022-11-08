package com.github.peacetrue.bean;

import com.github.peacetrue.util.stream.StreamUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 基本 Bean 对象访问者。
 * <p>
 * 合并访问集合类型到 {@link  #visitCollection(String, Object, Stream)}；
 * 合并访问父亲类型到 {@link  #visitParent(String, java.util.Map)}。
 *
 * @author peace
 **/
@Getter
@SuperBuilder
public abstract class BaseBeanVisitor<T> extends CoreBeanVisitor<T> {

    /** Bean 遍历者。 */
    @Builder.Default
    private BeanTraverser<? extends ReadonlyBeanProperty> beanTraverser = StandardBeanTraverser.NULLABLE_WRITE_METHOD;

    @Override
    public T visitCollection(@Nullable String path, Iterable<?> iterable) {
        return this.visitCollection(path, iterable, StreamUtils.iteratorAsStream(iterable.iterator()));
    }

    @Override
    public T visitCollection(@Nullable String path, Object[] array) {
        return this.visitCollection(path, array, Stream.of(array));
    }

    /**
     * 访问数据流对象得到目标对象。
     * <p>
     * 注意：数据流对象并非原始对象，而是为了统一集合调用入口，由原始对象转换而成的。
     *
     * @param path   对象路径
     * @param object 对象
     * @param stream 数据流对象
     * @return 目标对象
     */
    public abstract T visitCollection(@Nullable String path, Object object, Stream<?> stream);

    @Override
    public T visitParent(@Nullable String path, Object object) {
        return this.visitParent(path, object,
                beanTraverser.buildPropertyStream(path, object).collect(
                        LinkedHashMap::new,
                        (c, e) -> c.put(e.getName(), e.getValue(object)),
                        HashMap::putAll
                )
        );
    }

    @Override
    public T visitParent(@Nullable String path, Map<?, ?> map) {
        return this.visitParent(path, map, map);
    }

    /**
     * 访问原始对象得到目标对象。
     *
     * @param path   对象路径
     * @param object 原始对象
     * @param map    Map 对象
     * @return 目标对象
     */
    public abstract T visitParent(@Nullable String path, Object object, Map<?, ?> map);

}
