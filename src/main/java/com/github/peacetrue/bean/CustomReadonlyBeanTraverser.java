package com.github.peacetrue.bean;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 自定义只读 Bean 对象遍历者。
 *
 * @author peace
 **/
@RequiredArgsConstructor
public class CustomReadonlyBeanTraverser implements BeanTraverser<ReadonlyBeanProperty> {

    /** 默认实例。 */
    public static final CustomReadonlyBeanTraverser DEFAULT = new CustomReadonlyBeanTraverser(method -> true);

    /** 方法过滤器，查找出哪些方法是有效的。 */
    private final Predicate<Method> isValidMethod;

    @Override
    public Stream<ReadonlyBeanProperty> buildPropertyStream(@Nullable String path, Object object) {
        Class<?> type = object.getClass();
        BeanInfo beanInfo = BeanUtils.getBeanInfo(type);
        return Stream.of(beanInfo.getMethodDescriptors())
                .map(MethodDescriptor::getMethod)
                .filter(BeanUtils::isNoArgsHasReturn)
                .filter(isValidMethod)
                .map(ReadMethodBeanProperty::new)
                ;
    }

    /**
     * 根据方法名排除指定方法。
     *
     * @param methodNames 方法名
     * @return 方法过滤器
     */
    public static Predicate<Method> exclude(String... methodNames) {
        return method -> {
            String name = method.getName();
            return Stream.of(methodNames).noneMatch(item -> item.equals(name));
        };
    }

}
