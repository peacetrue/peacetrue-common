package com.github.peacetrue.bean;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Bean 对象遍历者，决定遍历 Bean 中的哪些属性。
 *
 * @author peace
 */
public interface BeanTraverser<T extends ReadonlyBeanProperty> {

    /**
     * 构造属性数据流。
     *
     * @param path   路径
     * @param object 对象
     * @return 遍历数据流
     */
    Stream<T> buildPropertyStream(@Nullable String path, Object object);

}
