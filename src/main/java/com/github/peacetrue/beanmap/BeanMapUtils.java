package com.github.peacetrue.beanmap;

import com.github.peacetrue.util.RegexUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * 对于类型不确定的 Bean，可以使用 Map 来表示。
 * Map 具有 2 种表示 Bean 的形式：层级式(tiered)和扁平式(flat)。
 *
 * @author peace
 **/
public abstract class BeanMapUtils {

    /** 嵌套对象分割符 */
    public static final char BEAN_SEPARATOR = '.';
    /** 列表下标起始分割符 */
    public static final char LIST_START_SEPARATOR = '[';
    /** 列表下标结束分割符 */
    public static final char LIST_END_SEPARATOR = ']';
    /** 列表标识符正则表达式，eg. users[1].name */
    public static final Pattern LIST_IDENTIFIER_PATTERN = Pattern.compile("(\\w+)\\[( *\\d+ *)]");
    /** 列表允许的最大容量，可使用 {@link #setListMaxSize(int)} 方法更改 */
    private static int listMaxSize = 1_00;

    private BeanMapUtils() {
    }

    /**
     * 设置列表允许的最大容量。
     * 防止异常 java.lang.OutOfMemoryError: Requested array size exceeds VM limit。
     *
     * @param listMaxSize 列表最大容量
     */
    public static void setListMaxSize(int listMaxSize) {
        BeanMapUtils.listMaxSize = listMaxSize;
    }

    /**
     * 遍历层级化对象，并在遍历过程中构造出一个提取物，最终返回该提取物。
     *
     * @param tiered  层级化对象
     * @param visitor 属性访问者
     * @param <T>     提取物类型
     * @return 提取物
     */
    public static <T> T walkTree(Map<String, Object> tiered, SupplierPropertyVisitor<T> visitor) {
        walkTree(tiered, (PropertyVisitor) visitor);
        return visitor.get();
    }

    /**
     * 遍历层级化对象。
     *
     * @param tiered  层级化对象
     * @param visitor 属性访问者
     */
    @SuppressWarnings("unchecked")
    public static void walkTree(Map<String, Object> tiered, PropertyVisitor visitor) {
        for (Map.Entry<String, Object> entry : tiered.entrySet()) {
            String propertyName = entry.getKey();
            Object propertyValue = entry.getValue();
            if (isBean(propertyValue)) {
                visitor.visit(propertyName, (Map<String, Object>) propertyValue);
            } else if (isBeans(propertyValue)) {
                visitor.visit(propertyName, (Collection<Map<String, Object>>) propertyValue);
            } else {
                visitor.visit(propertyName, propertyValue);
            }
        }
    }

    private static boolean isBean(Object propertyValue) {
        return propertyValue instanceof Map;
    }

    private static boolean isBeans(Object propertyValue) {
        return propertyValue instanceof Collection<?>
                && isElementTypeMatched((Collection<?>) propertyValue);
    }

    /** 集合中允许元素值为 null，但至少有一个不是 null */
    private static boolean isElementTypeMatched(Collection<?> collection) {
        boolean beanExists = false;
        for (Object item : collection) {
            if (item == null) continue;
            if (!Map.class.isAssignableFrom(item.getClass())) return false;
            beanExists = true;
        }
        return beanExists;
    }

    /**
     * 扁平化，将层级化对象转换为扁平化对象。
     *
     * @param tiered 层级化对象
     * @return 扁平化对象
     */
    public static Map<String, Object> flatten(Map<String, Object> tiered) {
        return BeanMapUtils.walkTree(tiered, new FlatPropertyVisitor<>(tiered.size()));
    }

    /**
     * 层级化，将扁平化对象转换为层级化对象。
     *
     * @param flat 扁平化对象
     * @return 层级化对象
     */
    public static Map<String, Object> tier(Map<String, ?> flat) {
        Map<String, Object> tiered = new LinkedHashMap<>(flat.size());
        tier(flat, new HashSet<>(), tiered);
        return tiered;
    }

    /**
     * 层级化，将扁平化对象转换为层级化对象。
     *
     * @param flat    扁平化对象
     * @param handled 已处理过的属性名集合
     * @param tiered  层级化对象
     */
    @SuppressWarnings("unchecked")
    private static <T> void tier(Map<String, T> flat, Set<String> handled, Map<String, Object> tiered) {
        for (Map.Entry<String, ?> property : flat.entrySet()) {
            String propertyPath = property.getKey();
            if (handled.contains(propertyPath)) continue;

            int beanSeparateIndex = propertyPath.indexOf(BEAN_SEPARATOR);
            if (beanSeparateIndex == -1) {
                String[] nameIndex = RegexUtils.extractValues(LIST_IDENTIFIER_PATTERN, propertyPath);
                if (nameIndex.length == 0) {
                    tiered.put(propertyPath, property.getValue());
                } else {
                    throw new IllegalStateException("tail list expression([?]) is not supported: " + propertyPath);
                }
            } else {
                String headPropertyPath = propertyPath.substring(0, beanSeparateIndex);
                String headPrefix = headPropertyPath + BEAN_SEPARATOR;
                Map<String, T> subFlat = filterByKeyPrefix(flat, headPrefix);
                handled.addAll(subFlat.keySet());
                subFlat = subByKeyPrefix(subFlat, headPrefix);
                String[] nameIndex = RegexUtils.extractValues(LIST_IDENTIFIER_PATTERN, headPropertyPath);
                if (nameIndex.length == 0) {
                    Map<String, Object> subTiered = (Map<String, Object>) tiered.computeIfAbsent(headPropertyPath, item -> new LinkedHashMap<>());
                    tier(subFlat, new HashSet<>(), subTiered);
                } else {
                    List<Map<String, Object>> subTiered = (List<Map<String, Object>>) tiered.computeIfAbsent(nameIndex[0], item -> new LinkedList<>());
                    Map<String, Object> element = setListElement(subTiered, Integer.parseInt(nameIndex[1].trim()), new LinkedHashMap<>());
                    tier(subFlat, new HashSet<>(), element);
                }
            }
        }
    }

    private static <T> T setListElement(List<T> elements, int index, T element) {
        if (index > listMaxSize) {
            throw new IllegalArgumentException("Index " + index + " exceeds upper limit: " + listMaxSize);
        }
        //直接设置超过 size 的 index 会报错，需要先填充 null
        if (index >= elements.size()) {
            IntStream.rangeClosed(elements.size(), index).forEach(i -> elements.add(null));
        }
        elements.set(index, element);
        return element;
    }

    private static <T> Map<String, T> filterByKeyPrefix(Map<String, T> map, String keyPrefix) {
        // https://stackoverflow.com/questions/24630963/nullpointerexception-in-collectors-tomap-with-null-entry-values
        return map.entrySet().stream()
                .filter(item -> item.getKey().startsWith(keyPrefix))
                .collect(LinkedHashMap::new, (c, e) -> c.put(e.getKey(), e.getValue()), LinkedHashMap::putAll);
    }

    private static <T> Map<String, T> subByKeyPrefix(Map<String, T> subFlat, String keyPrefix) {
        int length = keyPrefix.length();
        return subFlat.entrySet().stream().collect(
                LinkedHashMap::new, (c, e) -> c.put(e.getKey().substring(length), e.getValue()), LinkedHashMap::putAll
        );
    }


}
