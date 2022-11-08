package com.github.peacetrue.bean;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 泛化的 Bean 对象访问者。
 * 返回嵌套的 Map 对象表示。
 *
 * @author peace
 **/
@Getter
@SuperBuilder
public class GenericBeanVisitor extends BaseBeanVisitor<Object> {

    /** 默认实例。 */
    public static final GenericBeanVisitor DEFAULT = GenericBeanVisitor.builder().build();

    /** Bean 对象遍历者。 */
    @Builder.Default
    private BeanTraverser<? extends ReadonlyBeanProperty> beanTraverser = StandardBeanTraverser.NULLABLE_WRITE_METHOD;

    @Nullable
    @Override
    public Object visitLeaf(@Nullable String path, @Nullable Object object) {
        return object;
    }

    @Override
    public Object visitCollection(@Nullable String path, Object object, Stream<?> stream) {
        int[] i = {0};
        return stream
                .map(item -> visitAny(BeanUtils.concatSafely(path, i[0]++), item))
                .collect(Collectors.toList());
    }

    @Override
    public Object visitParent(@Nullable String path, Object object, Map<?, ?> map) {
        return map.entrySet().stream()
                .collect(LinkedHashMap::new,
                        (c, e) -> c.put(e.getKey(), visitAny(
                                BeanUtils.concatSafely(path, String.valueOf(e.getKey())),
                                e.getValue()
                        )),
                        HashMap::putAll
                );
    }

}
