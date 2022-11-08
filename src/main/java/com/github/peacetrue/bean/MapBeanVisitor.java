package com.github.peacetrue.bean;

import com.github.peacetrue.beanmap.BeanMap;
import lombok.experimental.SuperBuilder;

import javax.annotation.Nullable;
import java.util.Collections;

/**
 * Map Bean 对象访问者，
 * 遍历 Bean 然后返回其 Map 表示。
 * //TODO 替换泛型参数 BeanMap 为 Map，@SuperBuilder 不支持泛型参数中使用泛型
 *
 * @author peace
 **/
@SuperBuilder
public class MapBeanVisitor extends ConcreteBeanVisitor<BeanMap> {

    /** 默认实例。 */
    public static final MapBeanVisitor DEFAULT = MapBeanVisitor.builder().build();

    @Nullable
    @Override
    public BeanMap visitLeaf(@Nullable String path, @Nullable Object object) {
        return new BeanMap(Collections.singletonMap(path, object));
    }

    @Nullable
    @Override
    protected BeanMap merge(@Nullable BeanMap one, @Nullable BeanMap two) {
        if (one == null) one = new BeanMap();
        one.putAll(two);
        return one;
    }

}
