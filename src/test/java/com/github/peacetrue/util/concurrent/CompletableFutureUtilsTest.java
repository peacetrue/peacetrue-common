package com.github.peacetrue.util.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author peace
 **/
@Slf4j
class CompletableFutureUtilsTest {

    @Test
    void allOf() throws ExecutionException, InterruptedException {
        List<String> values = CompletableFutureUtils.allOf(
                CompletableFuture.completedFuture("1"),
                CompletableFuture.completedFuture("2")
        ).get();
        log.info("values: {}", values);
        Assertions.assertTrue(Arrays.asList("1", "2").containsAll(values));

        CompletableFutureUtils.allOfGenerically(
                CompletableFuture.completedFuture("1"),
                CompletableFuture.runAsync(() -> {
                    throw new IllegalArgumentException();
                })
        ).whenComplete((values2, throwable) ->
                Assertions.assertTrue(throwable instanceof IllegalArgumentException)
        );
    }

}
