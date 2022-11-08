package com.github.peacetrue.bean;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.github.peacetrue.HelpAntora;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;

import static com.github.peacetrue.bean.CustomReadonlyBeanTraverser.exclude;

/**
 * @author peace
 **/
@Slf4j
class PosixFileAttributeViewTest {

    //tag::jackson[]

    /** 使用 jackson 序列化 {@link PosixFileAttributeView} */
    @Test
    @SneakyThrows
    void jackson() {
        new JavaPropsMapper().writeValue(
                Files.newOutputStream(HelpAntora.POSIX_FILE_ATTRIBUTE_VIEW_DIR.resolve("jackson.properties")),
                BeanCommon.POSIX_FILE_ATTRIBUTE_VIEW
        );
    }
    //end::jackson[]

    //tag::beanVisitorDefault[]

    /** 使用默认的 {@link StringBeanVisitor} 序列化 {@link PosixFileAttributeView} */
    @Test
    @SneakyThrows
    void beanVisitorDefault() {
        String root = StringBeanVisitor.DEFAULT
                .visitRoot(BeanCommon.POSIX_FILE_ATTRIBUTE_VIEW);
        Files.write(
                HelpAntora.POSIX_FILE_ATTRIBUTE_VIEW_DIR.resolve("visitor.properties"),
                root.getBytes(StandardCharsets.UTF_8)
        );
    }
    //end::beanVisitorDefault[]


    //tag::beanVisitorCustomTraverser[]

    /** 使用自定义的 {@link BeanTraverser} 序列化 {@link PosixFileAttributeView} */
    @Test
    @SneakyThrows
    void beanVisitorCustomTraverser() {
        String root = StringBeanVisitor.builder()
                // 设置 Bean 遍历器，获取无参有返回值的方法
                .beanTraverser(CustomReadonlyBeanTraverser.DEFAULT)
                .build()
                .visitRoot(BeanCommon.POSIX_FILE_ATTRIBUTE_VIEW);
        Files.write(
                HelpAntora.POSIX_FILE_ATTRIBUTE_VIEW_DIR.resolve("visitor-custom-traverser.properties"),
                root.getBytes(StandardCharsets.UTF_8)
        );
    }
    //end::beanVisitorCustomTraverser[]

    //tag::beanVisitorCustomAll[]

    /** 使用自定义的 {@link BeanVisitor} 序列化 {@link PosixFileAttributeView} */
    @Test
    @SneakyThrows
    void beanVisitorCustomAll() {
        String root = StringBeanVisitor.builder()
                // FileTime 和 fileKey 作为叶子节点，toString 方法看着就很舒服
                .isLeafType(CoreBeanVisitor.in(FileTime.class)
                        .or(CoreBeanVisitor::isSimpleValueType)
                        .or((path, clazz) -> path != null && path.endsWith("fileKey"))
                )
                // 不知道 fileKey 是什么类型，使用带类型的属性路径
                .propertyPathBuilder(TypedPropertyPathBuilder.DEFAULT)
                // 设置 Bean 遍历器，获取无参有返回值的方法，然后根据方法名排除指定方法
                .beanTraverser(new CustomReadonlyBeanTraverser(exclude("hashCode", "toString")))
                .build()
                .visitRoot(BeanCommon.POSIX_FILE_ATTRIBUTE_VIEW);
        log.info("root:\n{}", root);
        Files.write(
                HelpAntora.POSIX_FILE_ATTRIBUTE_VIEW_DIR.resolve("visitor-custom-all.properties"),
                root.getBytes(StandardCharsets.UTF_8)
        );
    }
    //end::beanVisitorCustomAll[]

}
