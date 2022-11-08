package com.github.peacetrue.bean;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author peace
 **/
@Slf4j
class GenericBeanVisitorTest {

    @Test
    @SneakyThrows
    void visitRoot() {
        BeanCommon.User user = BeanCommon.USER;
        Object genericRoot = GenericBeanVisitor.DEFAULT.visitRoot(user);
        log.info("genericRoot: \n{}", genericRoot);
        Object mapRoot = MapBeanVisitor.DEFAULT.visitRoot(user);
        log.info("mapRoot: \n{}", mapRoot);
        Assertions.assertNotEquals(genericRoot, mapRoot);
        Assertions.assertNotEquals(
                BeanCommon.writeValueAsJsonString(genericRoot),
                BeanCommon.writeValueAsJsonString(mapRoot)
        );
        Assertions.assertEquals(
                BeanCommon.writeValueAsPropsString(genericRoot),
                BeanCommon.writeValueAsPropsString(mapRoot)
        );
    }
}
