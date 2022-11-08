package com.github.peacetrue.bean;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.beans.BeanInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 标准 Bean 对象遍历者。
 * 遵循 getter/setter 规范。
 *
 * @author peace
 **/
@RequiredArgsConstructor
public class StandardBeanTraverser implements BeanTraverser<BeanProperty> {

    /** 不允许写入方法为 null。 */
    public static final StandardBeanTraverser NONNULL_WRITE_METHOD = new StandardBeanTraverser(false);
    /** 允许写入方法为 null。 */
    public static final StandardBeanTraverser NULLABLE_WRITE_METHOD = new StandardBeanTraverser(true);

    /** 是否允许写入方法为 null。 */
    private final boolean nullableWriteMethod;
    private final Map<Class<?>, List<BeanProperty>> beanProperties = new HashMap<>();

    @Override
    public Stream<BeanProperty> buildPropertyStream(@Nullable String path, Object object) {
        return beanProperties.computeIfAbsent(object.getClass(), clazz -> {
            BeanInfo beanInfo = BeanUtils.getBeanInfo(object.getClass());
            return Stream.of(beanInfo.getPropertyDescriptors())
                    .filter(descriptor -> descriptor.getReadMethod() != null && (nullableWriteMethod || descriptor.getWriteMethod() != null))
                    .map(DescriptorBeanProperty::new)
                    .collect(Collectors.toList());
        }).stream();
    }
}
