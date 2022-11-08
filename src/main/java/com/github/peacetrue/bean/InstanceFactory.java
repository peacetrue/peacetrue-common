package com.github.peacetrue.bean;

/**
 * 实例工厂。
 *
 * @author peace
 **/
public interface InstanceFactory {

    /**
     * 根据声明类型和实例对象创建副本实例。
     *
     * @param declareClass 声明类
     * @param implement    实现对象
     * @param <T>          声明类型
     * @param <S>          实现类型
     * @return 实例对象
     */
    <T, S extends T> T createInstance(Class<T> declareClass, S implement);

}
