package com.github.peacetrue.bean;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Bean 对象访问者，访问完整个原始对象后，得到目标对象。
 * <br><br>
 * 访问方式：<br>
 * 通过调用对象的无参有返回值方法，访问对象的内部信息。
 * 将对象视为一棵树，类比文件系统，有路径和节点。
 * <br><br>
 * 对象节点类型分为以下 3 种：
 * <ul>
 *     <li>叶子节点，不需要遍历对象内部信息，通常包括：原始类型及其包装类型、简单值类型</li>
 *     <li>集合节点，需要循环遍历集合内元素信息，通常包括：{@link Iterable}、{@code Object[]}</li>
 *     <li>父亲节点，需要通过调用对象的方法遍历其内部信息，通常包括 {@link java.util.Map} 和复合类型</li>
 * </ul>
 * 对象路径：<br>
 * <ul>
 *     <li>节点路径间串联使用点号，例如：user.name；</li>
 *     <li>集合节点路径使用 [{index}]，例如：roles[0]；</li>
 *     <li>组合以上两种方式，例如：user.roles[0].name。</li>
 * </ul>
 *
 * @param <T> 目标对象类型
 * @author peace
 * @see BeanUtils
 */
interface BeanVisitor<T> {

    /**
     * 访问根原始对象得到目标对象。
     * 此方法作为入口方法被调用。
     *
     * @param object 根原始对象
     * @return 目标对象
     */
    default T visitRoot(Object object) {
        return visitAny(null, object);
    }

    /**
     * 访问任意原始对象得到目标对象。
     * 此方法会根据对象类型，调用其他访问方法。
     *
     * @param path   对象路径
     * @param object 任意原始对象
     * @return 目标对象
     */
    @Nullable
    T visitAny(@Nullable String path, @Nullable Object object);

    /**
     * 访问叶子原始对象得到目标对象。
     *
     * @param path   对象路径
     * @param object 叶子原始对象
     * @return 目标对象
     */
    @Nullable
    T visitLeaf(@Nullable String path, @Nullable Object object);

    /**
     * 访问迭代器原始对象得到目标对象。
     *
     * @param path     对象路径
     * @param iterable 迭代器原始对象
     * @return 目标对象
     */
    T visitCollection(@Nullable String path, Iterable<?> iterable);

    /**
     * 访问数组原始对象得到目标对象。
     *
     * @param path  对象路径
     * @param array 数组原始对象
     * @return 目标对象
     */
    T visitCollection(@Nullable String path, Object[] array);

    /**
     * 访问 Map 原始对象得到目标对象。
     *
     * @param path 对象路径
     * @param map  Map 原始对象
     * @return 目标对象
     */
    T visitParent(@Nullable String path, Map<?, ?> map);

    /**
     * 访问复合原始对象得到目标对象。
     *
     * @param path   对象路径
     * @param object 复合原始对象
     * @return 目标对象
     */
    T visitParent(@Nullable String path, Object object);


}
