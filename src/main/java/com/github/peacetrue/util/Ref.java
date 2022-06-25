package com.github.peacetrue.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 引用对象。
 * <p>
 * 可用于解决 lambda 表达式、匿名对象从外层向内层传值问题：
 * <pre>
 * void anonymous() {
 *     //以下写法无法通过编译：
 *     int index = 0;
 *     new Object() {
 *         {
 *             System.out.println(index);
 *         }
 *     };
 *     index = 1;
 *
 *     //使用 Ref 改写：
 *     Ref&lt;Integer&gt; index = new Ref&lt;&gt;(0);
 *     new Object() {
 *         {
 *             System.out.println(index.value);
 *         }
 *     };
 *     index.value = 1;
 * }
 * </pre>
 *
 * @author peace
 **/
@NoArgsConstructor
@AllArgsConstructor
public class Ref<T> {

    /** 引用值 */
    public T value;
}
