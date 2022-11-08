package com.github.peacetrue.bean;

import com.github.peacetrue.lang.reflect.FieldUtils;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 有序的 Bean 遍历器。
 *
 * @author peace
 **/
@RequiredArgsConstructor
public class OrderedBeanTraverser<T extends ReadonlyBeanProperty> implements BeanTraverser<T> {

    private final BeanTraverser<T> beanTraverser;

    @Override
    public Stream<T> buildPropertyStream(@Nullable String path, Object object) {
        List<String> fieldNames = FieldUtils.getDeclaredFields(object.getClass()).stream().map(Field::getName).collect(Collectors.toList());
        return beanTraverser.buildPropertyStream(path, object)
                .sorted(Comparator.comparingInt(property -> fieldNames.indexOf(property.getName())));
    }
}
