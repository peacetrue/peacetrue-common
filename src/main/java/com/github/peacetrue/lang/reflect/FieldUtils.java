package com.github.peacetrue.lang.reflect;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * 字段工具类。
 *
 * @author peace
 **/
public class FieldUtils {

    private FieldUtils() {
    }

    /**
     * 同 {@link #getDeclaredFields(Class, Class)}，
     * superclass 设置为 {@link Object}。
     *
     * @param clazz 指定类
     * @return 字段集合
     */
    public static List<Field> getDeclaredFields(Class<?> clazz) {
        return getDeclaredFields(clazz, Object.class);
    }

    /**
     * 获取从指定类 clazz 开始直至其父类 superclass（不包含）中声明的所有字段。
     * 父类中字段排在前面，子类中字段排在后面。
     * <p>
     * java docs 中描述 {@link Class#getDeclaredFields()} 返回的字段无特别顺序，
     * 实际使用时，和代码中书写的顺序一致。
     * 此方法的初衷即用于获取与代码中书写顺序一致的字段。
     *
     * @param clazz      指定类
     * @param superclass 父类
     * @return 字段列表
     */
    public static List<Field> getDeclaredFields(Class<?> clazz, Class<?> superclass) {
        LinkedList<Field> fieldList = new LinkedList<>();
        while (clazz != superclass) {
            Field[] fields = clazz.getDeclaredFields();
            for (int i = fields.length - 1; i >= 0; i--) {
                fieldList.addFirst(fields[i]);
            }
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

}
