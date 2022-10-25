package com.github.peacetrue.lang;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 对象工具类。
 *
 * @author peace
 **/
public class ObjectUtils {

    private ObjectUtils() {
    }

    /**
     * 获取默认值，适用于方法内本地变量赋值。
     * <p>
     * 使用示例如下：
     * <pre>
     *     params = defaultIfNull(params, new Params());
     * </pre>
     *
     * @param object       原值
     * @param defaultValue 默认值
     * @param <T>          值类型
     * @return 如果原值为 {@code null} 返回默认值，否则返回原值
     */
    public static <T> T defaultIfNull(T object, T defaultValue) {
        return object == null ? defaultValue : object;
    }

    /**
     * 获取默认值，延迟提供默认值，适用于方法内本地变量赋值。
     * <p>
     * 使用示例如下：
     * <pre>
     *     params = defaultIfNull(params, Params::new);
     * </pre>
     *
     * @param object       原值
     * @param defaultValue 默认值提供者
     * @param <T>          值类型
     * @return 如果原值为 {@code null} 返回默认值，否则返回原值
     */
    public static <T> T defaultIfNullLazily(T object, Supplier<T> defaultValue) {
        return object == null ? defaultValue.get() : object;
    }

    /**
     * 设置默认值，适用于引用对象赋值。
     * <p>
     * 使用示例如下：
     * <pre>
     *   if(user.getName() == null) user.setName("");
     *   // equals to
     *   setDefault(user::getName, user::setName, "")
     * </pre>
     *
     * @param getter       获取属性值
     * @param setter       设置属性值
     * @param defaultValue 默认值
     * @param <T>          默认值类型
     */
    public static <T> void setDefault(Supplier<T> getter, Consumer<T> setter, T defaultValue) {
        if (getter.get() == null) setter.accept(defaultValue);
    }

    /**
     * 设置默认值，适用于引用对象赋值。
     *
     * @param getter       获取属性值
     * @param setter       设置属性值
     * @param defaultValue 默认值提供者
     * @param <T>          默认值类型
     */
    public static <T> void setDefaultLazily(Supplier<T> getter, Consumer<T> setter, Supplier<T> defaultValue) {
        if (getter.get() == null) setter.accept(defaultValue.get());
    }

    /**
     * 安全地调用，避免空指针异常。
     *
     * @param value   输入值
     * @param invoker 输入值为非 {@code null} 时的调用者
     * @param <T>     输入值类型
     */
    public static <T> void acceptSafely(T value, Consumer<T> invoker) {
        if (value != null) invoker.accept(value);
    }

    /**
     * 安全地调用，避免空指针异常。
     *
     * @param value   输入值
     * @param invoker 输入值为非 {@code null} 时的调用者
     * @param <T>     输入值类型
     * @param <S>     返回值类型
     * @return {@code null} 或者调用返回值
     */
    @Nullable
    public static <T, S> S invokeSafely(T value, Function<T, S> invoker) {
        return invokeSafely(value, null, invoker);
    }

    /**
     * 安全地调用，避免空指针异常。
     *
     * @param value    输入值
     * @param defaults 输入值为 {@code null} 时的默认值
     * @param invoker  输入值为非 {@code null} 时的调用者
     * @param <T>      输入值类型
     * @param <S>      返回值类型
     * @return 默认值或者调用返回值
     */
    public static <T, S> S invokeSafely(T value, S defaults, Function<T, S> invoker) {
        return value == null ? defaults : invoker.apply(value);
    }

    /**
     * 安全地调用，避免空指针异常。
     *
     * @param value    输入值
     * @param defaults 输入值为 {@code null} 时的默认值
     * @param invoker  输入值为非 {@code null} 时的调用者
     * @param <T>      输入值类型
     * @param <S>      返回值类型
     * @return 默认值或者调用返回值
     */
    public static <T, S> S invokeSafelyLazily(T value, Supplier<S> defaults, Function<T, S> invoker) {
        return value == null ? defaults.get() : invoker.apply(value);
    }

}
