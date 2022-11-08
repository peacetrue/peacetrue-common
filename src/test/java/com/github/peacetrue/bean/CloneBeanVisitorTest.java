package com.github.peacetrue.bean;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 深度克隆。
 *
 * @author peace
 **/
@Slf4j
class CloneBeanVisitorTest {

    @Test
    void basic() {
        Object root = CloneBeanVisitor.DEFAULT.visitRoot(BeanCommon.USER);
        Assertions.assertNotSame(BeanCommon.USER, root);
        Assertions.assertEquals(BeanCommon.USER, root);
    }
}
