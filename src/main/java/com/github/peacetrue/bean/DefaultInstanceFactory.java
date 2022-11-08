package com.github.peacetrue.bean;

import com.github.peacetrue.util.MapUtils;

import java.util.*;
import java.util.function.Supplier;

/**
 * 默认实例工厂，使用实现类的无参构造方法创建实例对象。
 *
 * @author peace
 **/
public class DefaultInstanceFactory implements InstanceFactory {

    /** 默认实例。 */
    public static final DefaultInstanceFactory DEFAULT = new DefaultInstanceFactory();
    private static final Map<Class<?>, Supplier<?>> DECLARE_CLASS_SUPPLIER = MapUtils.from(
            new Class<?>[]{Collection.class, List.class, Set.class, Map.class},
            new Supplier<?>[]{ArrayList::new, ArrayList::new, HashSet::new, LinkedHashMap::new}
    );

    @Override
    @SuppressWarnings("unchecked")
    public <T, S extends T> T createInstance(Class<T> declareClass, S implement) {
        Supplier<?> supplier = DECLARE_CLASS_SUPPLIER.getOrDefault(
                declareClass, () -> BeanUtils.instance(implement.getClass())
        );
        return (T) supplier.get();
    }
}
