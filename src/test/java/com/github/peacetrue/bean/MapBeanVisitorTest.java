package com.github.peacetrue.bean;

import com.github.peacetrue.BeanCommon;
import com.github.peacetrue.util.MapUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author peace
 **/
@Slf4j
class MapBeanVisitorTest {

    @Test
    @SneakyThrows
    void basic() {
        BeanCommon.User user = BeanCommon.USER;
        Map<String, Object> root = MapBeanVisitor.DEFAULT.visitRoot(user);
        log.info("root:\n {}", MapUtils.prettify(root));

        Assertions.assertEquals(
                root.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("\n")),
                StringBeanVisitor.DEFAULT.visitRoot(user)
        );
    }
}
