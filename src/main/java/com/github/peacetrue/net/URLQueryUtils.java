package com.github.peacetrue.net;

import com.github.peacetrue.beanmap.BeanMapUtils;
import com.github.peacetrue.beanmap.FlatPropertyVisitor;
import com.github.peacetrue.lang.ObjectUtils;
import com.github.peacetrue.util.ArrayUtils;
import com.github.peacetrue.util.stream.StreamUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * URL 查询参数工具类。
 * <p>
 * 查询参数对象类型：{@code Map<String,List<String>>}。
 *
 * @author peace
 * @see java.net.URL
 **/
public class URLQueryUtils {

    private URLQueryUtils() {
    }

    /**
     * 解析 URL 查询参数字符串。
     * <p>
     * 转换规则如下：
     * <ul>
     *     <li>name -&gt; name: null</li>
     *     <li>name= -&gt; name: []</li>
     *     <li>name=xx -&gt; name: [xx]</li>
     *     <li>name=xx&amp;name=xx -&gt; name: [xx, xx]</li>
     * </ul>
     *
     * @param query URL 查询参数字符串
     * @return 扁平化查询参数对象
     */
    public static Map<String, List<String>> parseQuery(String query) {
        Map<String, List<String>> queryParams = Stream.of(query.split("&"))
                .filter(item -> !item.trim().isEmpty())//eg: name=x&&password=x
                .map(item -> item.split("=", 2))
                .map(item -> ArrayUtils.replace(item, URLCodecUtils::decode))
                .collect(Collectors.groupingBy(
                        item -> item[0], LinkedHashMap::new,
                        Collectors.mapping(item -> item.length == 1 ? null : item[1], Collectors.toList())
                ));

        queryParams.entrySet().stream()
                .filter(item -> item.getValue().get(0) == null)
                .forEach(item -> item.setValue(null));
        return queryParams;
    }

    /**
     * 转换为 URL 查询参数字符串，忽略为 {@code null} 的键和值。
     *
     * @param params 查询参数扁平化对象
     * @return 查询参数字符串
     */
    public static String toQuery(Map<String, ? extends Collection<?>> params) {
        return params.entrySet().stream()
                .filter(item -> Objects.nonNull(item.getKey()))
                .flatMap(item -> {
                    Collection<?> collection = item.getValue();
                    return collection == null || allNull(collection)
                            ? Stream.of(URLCodecUtils.encode(item.getKey()))
                            : collection.stream().map(value -> encode(item.getKey(), value));
                })
                .collect(Collectors.joining("&"));
    }

    private static String encode(String key, Object value) {
        String encodedKey = URLCodecUtils.encode(key);
        return encodedKey + "=" + URLCodecUtils.encode(Objects.toString(value, ""));
    }

    /**
     * 转换为查询参数。
     * <p>
     * 如果值已经是集合，直接转换为列表，否则转换为单个值列表；
     * 再将列表中的元素使用 {@link Object#toString()} 转换为字符串。
     *
     * @param flat 扁平化的 BeanMap
     * @return 查询参数
     */
    public static Map<String, List<String>> fromBeanMap(Map<String, ?> flat) {
        return flat.entrySet().stream().collect(
                LinkedHashMap::new,
                (c, e) -> c.put(e.getKey(), toList(e.getValue())),
                LinkedHashMap::putAll
        );
    }

    @Nullable
    private static List<String> toList(@Nullable Object value) {
        return ObjectUtils.invokeSafely(value,
                temp -> StreamUtils.toStream(temp, Stream::of)
                        .map(item -> Objects.toString(item, null))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 处理查询参数的集合值，将单参数集合转换为单个值。
     *
     * @param flat 扁平化查询参数对象
     * @return 值为对象的集合
     */
    public static Map<String, Object> toBeanMap(Map<String, ? extends List<?>> flat) {
        return flat.entrySet().stream().collect(
                LinkedHashMap::new,
                (c, e) -> c.put(e.getKey(), unwrapSingleListValue(e.getValue())),
                LinkedHashMap::putAll
        );
    }

    @Nullable
    private static Object unwrapSingleListValue(@Nullable List<?> value) {
        if (value == null || allNull(value)) return null;
        return value.size() > 1 ? value : value.get(0);
    }

    private static boolean allNull(Collection<?> collection) {
        return collection.stream().allMatch(Objects::isNull);
    }

    /**
     * 扁平化，将层级化对象转换为扁平化对象。
     *
     * @param tiered 层级化对象
     * @return 扁平化对象
     */
    public static Map<String, List<String>> flatten(Map<String, Object> tiered) {
        return BeanMapUtils.walkTree(tiered, new FlatPropertyVisitor<List<String>>(tiered.size()) {
            @Nullable
            @Override
            protected List<String> handleValue(@Nullable Object value) {
                return toList(value);
            }
        });
    }

}
