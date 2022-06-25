package com.github.peacetrue.beanmap;

import java.util.function.Supplier;

/**
 * 在遍历过程中构造出一个提取物，最终返回该提取物。
 *
 * @param <T> 提取物类型
 * @author peace
 */
public interface SupplierPropertyVisitor<T> extends PropertyVisitor, Supplier<T> {
}
