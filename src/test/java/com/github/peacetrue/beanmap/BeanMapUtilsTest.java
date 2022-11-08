package com.github.peacetrue.beanmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.github.peacetrue.bean.BeanCommon;
import com.github.peacetrue.test.SourcePathUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author peace
 **/
@Slf4j
public class BeanMapUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void walkTreeLog() throws IOException {
        BeanCommon.User user = BeanCommon.USER;
        Map<String, Object> tiered = objectMapper.convertValue(user, BeanMap.class);
        Files.write(
                Paths.get(SourcePathUtils.getTestResourceAbsolutePath("/200-tiered.json")),
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(user)
        );
        //tag::log[]
        BeanMapUtils.walkTree(tiered, (path, value) -> log.info("path: {}, value: {}", path, value));
        //end::log[]

        Map<String, Object> flatten = BeanMapUtils.flatten(tiered);
        Files.write(
                Paths.get(SourcePathUtils.getTestResourceAbsolutePath("/210-flatten.json")),
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(flatten)
        );
    }

    @Test
    void walkTree() {
        BeanCommon.User user = BeanCommon.USER;
        //这个转换，保留了 Bean 中的 null 属性
        Map<String, Object> tiered = objectMapper.convertValue(user, BeanMap.class);

//        Assertions.assertEquals(tiered.toString(), BeanMapUtils.walkTree(tiered, new DuplicatePropertyVisitor(tiered.size())).toString());

        Map<String, Object> flat = BeanMapUtils.flatten(tiered);
        Assertions.assertEquals(BeanCommon.clearFlatNull(BeanCommon.getFlat()).toString(), BeanCommon.clearFlatNull(flat).toString());

        Map<String, Object> backTiered = BeanMapUtils.tier(flat);
        Assertions.assertEquals(tiered, backTiered);

        BeanCommon.User backUser = objectMapper.convertValue(backTiered, BeanCommon.User.class);
        Assertions.assertEquals(user, backUser);
    }

    @Test
    void setListElement() {
        List<Object> elements = new ArrayList<>();
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> elements.set(1, 1));
        Assertions.assertDoesNotThrow(() -> BeanMapUtils.setListElement(elements, 1, 1));
        Assertions.assertDoesNotThrow(() -> BeanMapUtils.setListElement(elements, 1, 1));

        BeanMapUtils.setListMaxSize(10);
        Assertions.assertDoesNotThrow(() -> BeanMapUtils.setListElement(elements, 9, 1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> BeanMapUtils.setListElement(elements, 10, 1));
    }

    @Test
    void concatSafely() {
        Assertions.assertEquals("[0]", BeanMapUtils.concatSafely(null, 0));
    }

    //end::user[]

    @Test
    void basic() throws IOException {
        BeanCommon.User user = BeanCommon.USER;
        JavaPropsMapper objectMapper = new JavaPropsMapper();
        Map<String, Object> beanMap = objectMapper.convertValue(user, BeanMap.class);
        log.info("beanMap: {}", beanMap);
        beanMap = BeanMapUtils.flatten(beanMap);
        log.info("beanMap: {}", beanMap);

        JavaPropsSchema schema = JavaPropsSchema.emptySchema().withFirstArrayOffset(0).withWriteIndexUsingMarkers(true);
        Map<String, String> stringBeanMap = objectMapper.writeValueAsMap(user, schema);
        log.info("beanMap: {}", stringBeanMap);
        beanMap = BeanMapUtils.tier(stringBeanMap);
        log.info("beanMap: {}", beanMap);
    }


}
