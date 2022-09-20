package com.github.peacetrue.util.concurrent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link CompletableFuture} 工具类。
 *
 * @author peace
 **/
public class CompletableFutureUtils {

    private CompletableFutureUtils() {
    }

    /**
     * 所有任务执行完后，获取到总的返回结果。
     *
     * @param cfs 任务集合
     * @param <T> 任务返回结果类型
     * @return 返回结果集合
     * @see CompletableFuture#allOf(CompletableFuture[])
     */
    @SafeVarargs
    public static <T> CompletableFuture<List<T>> allOf(CompletableFuture<T>... cfs) {
        return CompletableFuture
                .allOf(cfs)
                .thenApply(voids ->
                        Stream.of(cfs)
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList())
                );
    }

    /**
     * 所有任务执行完后，获取到总的返回结果。
     *
     * @param cfs 任务集合
     * @return 返回结果集合
     * @see #allOf(CompletableFuture[])
     */
    @SuppressWarnings("unchecked")
    public static CompletableFuture<List<Object>> allOfGenerically(CompletableFuture<?>... cfs) {
        return allOf((CompletableFuture<Object>[]) cfs);
    }

}
