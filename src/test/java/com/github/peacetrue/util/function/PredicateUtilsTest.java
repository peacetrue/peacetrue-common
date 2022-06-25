package com.github.peacetrue.util.function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author peace
 **/
class PredicateUtilsTest {

    private static boolean isSuccess(Object anything) {
        return true;
    }

    void negateSample() {
        //想对一个 Predicate 取反，直接这么写是不支持的：PredicateUtilsTest::isSuccess.negate()
        //得先声明一个 Predicate 的变量，再调用变量的方法，因为 lambda 函数的类型是不确定的
        Predicate<Object> predicate = PredicateUtilsTest::isSuccess;
        Predicate<Object> negatePredicate = predicate.negate();
        //你可以使用 negate 方法实现单行式写法
        Predicate<Object> oneLineCode = PredicateUtils.negate(PredicateUtilsTest::isSuccess);
        //PredicateUtilsTest::isSuccess 同时也可以赋值给 Function
        Function<Object, Boolean> function = PredicateUtilsTest::isSuccess;
    }


    @Test
    void alwaysTrue() {
        Assertions.assertTrue(PredicateUtils.alwaysTrue(null));
    }

    @Test
    void alwaysTrue2() {
        Assertions.assertTrue(PredicateUtils.alwaysTrue(null, null));
    }

    @Test
    void negate() {
        Assertions.assertFalse(PredicateUtils.negate(PredicateUtils::alwaysTrue).test(null));
    }

    @Test
    void headConvert() {
        Assertions.assertTrue(PredicateUtils.headConvert(PredicateUtils::alwaysTrue).test(null, null));
    }

    @Test
    void tailConvert() {
        Assertions.assertTrue(PredicateUtils.tailConvert(PredicateUtils::alwaysTrue).test(null, null));
    }

}
