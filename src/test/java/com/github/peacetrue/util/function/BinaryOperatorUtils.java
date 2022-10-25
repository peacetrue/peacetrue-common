package com.github.peacetrue.util.function;

/**
 * 二元操作器工具类。
 *
 * @author peace
 **/
public class BinaryOperatorUtils {

    private BinaryOperatorUtils() {
    }

    /**
     * 获取始终返回首部参数的二元操作器。
     *
     * @param head 首部参数
     * @param tail 尾部参数
     * @param <T>  参数类型
     * @return 合并值
     */
    public static <T> T headIdentity(T head, T tail) {
        return head;
    }

    /**
     * 获取始终返回尾部参数的二元操作器。
     *
     * @param head 首部参数
     * @param tail 尾部参数
     * @param <T>  参数类型
     * @return 合并值
     */
    public static <T> T tailIdentity(T head, T tail) {
        return tail;
    }

    /**
     * 如果 Map 中键重复，需要合并，则抛出异常。
     * <p>
     * 参考 Collectors#throwingMerger()。
     *
     * @param head 首部参数
     * @param tail 尾部参数
     * @param <T>  参数类型
     * @return 合并值
     */
    public static <T> T throwingMerger(T head, T tail) {
        throw new IllegalStateException(String.format("Duplicate key %s", head));
    }

}
