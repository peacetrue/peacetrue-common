package com.github.peacetrue.bean;

import com.github.peacetrue.HelpAntora;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.Collections;
import java.util.Date;

import static com.github.peacetrue.bean.BeanCommon.*;

/**
 * @author peace
 **/
@Slf4j
class StringBeanVisitorTest {

    //tag::visitStandardBean[]
    @Test
    @SneakyThrows
    void visitStandardBean() {
        String root = buildStringBeanVisitor().visitRoot(USER);
        Assertions.assertEquals(
                // 使用 jackson-dataformat-properties 转换为 properties 作为对比
                writeValueAsPropsString(USER),
                root
        );
        log.info("root: \n{}\n", root);
        Files.write(
                HelpAntora.BEAN_DIR.resolve("user.properties"),
                root.getBytes(StandardCharsets.UTF_8)
        );
    }
    //end::visitStandardBean[]


    //tag::visitCustomBean[]
    @Test
    @SneakyThrows
    void visitCustomBean() {
        PosixFileAttributeView bean = getPosixFileAttributeView();
        String root = buildStringBeanVisitor().visitRoot(bean);
        Assertions.assertEquals(writeValueAsPropsString(bean), root);
        log.info("root: \n{}\n", root);
        Files.write(
                HelpAntora.BEAN_DIR.resolve("PosixFileAttributeView.standard.properties"),
                root.getBytes(StandardCharsets.UTF_8)
        );

        root = buildCustomStringBeanVisitor().visitRoot(bean);
        Assertions.assertNotEquals(writeValueAsPropsString(bean), root);
        log.info("root: \n{}\n", root);
        Files.write(
                HelpAntora.BEAN_DIR.resolve("PosixFileAttributeView.custom.properties"),
                root.getBytes(StandardCharsets.UTF_8)
        );
    }

    //end::visitCustomBean[]


    @Test
    @SneakyThrows
    void visitWithEmptyCollection() {
        Assertions.assertEquals(
                StringBeanVisitor.DEFAULT.visitRoot(new BeanCommon.User()),
                StringBeanVisitor.DEFAULT.visitRoot(BeanCommon.User.builder()
                        // 空集合等效于 null
                        .roles(Collections.emptyList())
                        .build()
                )
        );
    }

    @Test
    void visitLeaf() {
        Date now = new Date();
        Assertions.assertEquals(now.toString(), StringBeanVisitor.DEFAULT.visitRoot(now));
    }
}
