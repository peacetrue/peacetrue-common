package com.github.peacetrue.bean;

import javax.annotation.Nullable;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Bean 工具类。
 *
 * @author peace
 **/
public class BeanUtils {

    /** 嵌套对象分割符 */
    public static final char BEAN_SEPARATOR = '.';
    /** 列表下标起始分割符 */
    public static final char LIST_START_SEPARATOR = '[';
    /** 列表下标结束分割符 */
    public static final char LIST_END_SEPARATOR = ']';
    /** 原始类型的包装类型 */
    private static final Collection<Class<?>> PRIMITIVE_WRAPPERS = Collections.unmodifiableList(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class
    ));
    /** 简单值类型 */
    private static final List<Class<?>> SIMPLE_VALUE_TYPES = Collections.unmodifiableList(Arrays.asList(
            CharSequence.class, Date.class, Enum.class, URI.class, URL.class, Locale.class,
            TemporalField.class, ValueRange.class, TemporalAccessor.class // Java 8 时间
    ));

    private BeanUtils() {
    }

    /**
     * 是否原始类型的包装类型。
     *
     * @param clazz 类型
     * @return true 如果是原始类型的包装类型，否则 false
     */
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return PRIMITIVE_WRAPPERS.contains(clazz);
    }

    /**
     * 是否原始类型或包装类型。
     *
     * @param clazz 类型
     * @return true 如果是原始类型或包装类型，否则 false
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz);
    }

    /**
     * 是否简单值类型。原始类型和 {@link #SIMPLE_VALUE_TYPES} 中类型及其子类型都属于简单值类型。
     * <p>
     * 通过 {@link Object#toString()} 方法即可得知对象的核心信息，而无需访问对象的属性。
     *
     * @param clazz 类型
     * @return true 如果是简单值类型，否则 false
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return isPrimitiveOrWrapper(clazz) || isSubclassOf(clazz, SIMPLE_VALUE_TYPES.stream());
    }

    /**
     * subclass 是否 superclasses 其中之一的子类。
     *
     * @param subclass     子类
     * @param superclasses 父类数组
     * @return true 如果是其子类，否则 false
     */
    public static boolean isSubclassOf(Class<?> subclass, Class<?>... superclasses) {
        return isSubclassOf(subclass, Stream.of(superclasses));
    }

    /**
     * subclass 是否 superclasses 其中之一的子类。
     *
     * @param subclass     子类
     * @param superclasses 父类数组
     * @return true 如果是其子类，否则 false
     */
    public static boolean isSubclassOf(Class<?> subclass, Stream<Class<?>> superclasses) {
        return superclasses.anyMatch(clazz -> clazz.isAssignableFrom(subclass));
    }


    /**
     * 获取对象 Bean 信息。
     *
     * @param clazz 对象
     * @return Bean 信息
     * @throws IllegalStateException 如果发生内省异常，转换为此运行时异常
     */
    static BeanInfo getBeanInfo(Class<?> clazz) {
        try {
            return Introspector.getBeanInfo(clazz, Object.class);
        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 实例化类。
     *
     * @param clazz 类
     * @param <T>   类型
     * @return 实例
     */
    public static <T> T instance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 调用对象无参方法。
     *
     * @param object 对象
     * @param method 方法
     * @return 返回值
     * @throws IllegalStateException 如果发生反射调用异常，转换为此运行时异常
     */
    public static Object invoke(Object object, Method method) {
        try {
            return method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 调用对象单参数方法。
     *
     * @param object        对象
     * @param method        方法
     * @param propertyValue 属性值
     * @throws IllegalStateException 如果发生反射调用异常，转换为此运行时异常
     */
    public static void invoke(Object object, Method method, Object propertyValue) {
        try {
            method.invoke(object, propertyValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 是否无参有返回值的方法。
     *
     * @param method 方法
     * @return true 如果是无参有返回值的方法，否则 false
     */
    public static boolean isNoArgsHasReturn(Method method) {
        return method.getParameterCount() == 0 && method.getReturnType() != Void.class;
    }

    /**
     * 根据 getter 方法名获取属性名。
     *
     * @param methodName 方法名
     * @return 属性名
     */
    public static String formatGetter(String methodName) {
        return Introspector.decapitalize(methodName.replaceFirst("^(get|is)(?=[A-Z])", ""));
    }

    /**
     * 连接路径和名称。
     *
     * @param path 路径
     * @param name 名称
     * @return 连接后的路径
     */
    public static String concat(String path, String name) {
        // eg: user.employee
        return path + BEAN_SEPARATOR + name;
    }

    /**
     * 安全地连接路径和名称。
     *
     * @param path 路径
     * @param name 名称
     * @return 连接后的路径
     */
    static String concatSafely(@Nullable String path, String name) {
        return path == null ? name : concat(path, name);
    }

    /**
     * 构造索引下标路径。
     *
     * @param index 索引下标
     * @return 索引下标路径
     */
    public static String concat(int index) {
        // eg: [0]
        return LIST_START_SEPARATOR + String.valueOf(index) + LIST_END_SEPARATOR;
    }

    /**
     * 连接路径和索引下标。
     *
     * @param path  路径
     * @param index 索引下标
     * @return 连接后的路径
     */
    public static String concat(String path, int index) {
        // eg: roles[0]
        return path + concat(index);
    }

    /**
     * 安全地连接路径和索引下标。
     *
     * @param path  路径
     * @param index 索引下标
     * @return 连接后的路径
     */
    public static String concatSafely(@Nullable String path, int index) {
        return path == null ? concat(index) : concat(path, index);
    }

    static List<String> getFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
    }
}
