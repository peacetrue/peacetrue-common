package com.github.peacetrue.beanmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.github.peacetrue.test.SourcePathUtils;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
class BeanMapUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String, Object> clearFlatNull(Map<String, Object> flat) {
        return flat.entrySet().stream().filter(item -> item.getValue() != null).collect(
                LinkedHashMap::new, (c, e) -> c.put(e.getKey(), e.getValue()), LinkedHashMap::putAll
        );
    }

    private static User getUser() {
        return User.builder().id(null).name("admin").password("")
                .roles(Arrays.asList(
                        Role.builder().id(1L).name("admin").tags(Arrays.asList("read", "write")).build(),
                        null,
                        Role.builder().name("user").build(),
                        Role.builder().id(3L).build()
                ))
                .employee(getEmployee())
                .build();
    }

    private static Employee getEmployee() {
        return Employee.builder().id(1L).name("Jone").tags(Arrays.asList("good", "better")).build();
    }

    private static Map<String, Object> getTiered() {
        Map<String, Object> tiered = new LinkedHashMap<>();
        tiered.put("id", null);
        tiered.put("name", "admin");
        tiered.put("password", "");
        tiered.put("roles", Arrays.asList(
                ImmutableMap.of("id", 1, "name", "admin", "tags", Arrays.asList("read", "write")),
                null,
                ImmutableMap.of("name", "user"),
                ImmutableMap.of("id", 3)
        ));
        tiered.put("employee", ImmutableMap.of("id", 1, "name", "Jone", "tags", Arrays.asList("good", "better")));
        return tiered;
    }

    private static Map<String, Object> getFlat() {
        Map<String, Object> flat = new LinkedHashMap<>();
        flat.put("id", null);
        flat.put("name", "admin");
        flat.put("password", "");
        flat.put("roles[0].id", 1);
        flat.put("roles[0].name", "admin");
        flat.put("roles[0].tags[0]", "read");
        flat.put("roles[0].tags[1]", "write");
        flat.put("roles[2].name", "user");
        flat.put("roles[3].id", 3);
        flat.put("employee.id", 1);
        flat.put("employee.name", "Jone");
        flat.put("employee.tags[0]", "good");
        flat.put("employee.tags[1]", "better");
        return flat;
    }

    @Test
    void walkTreeLog() throws IOException {
        User user = getUser();
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
        User user = getUser();
        //这个转换，保留了 Bean 中的 null 属性
        Map<String, Object> tiered = objectMapper.convertValue(user, BeanMap.class);

//        Assertions.assertEquals(tiered.toString(), BeanMapUtils.walkTree(tiered, new DuplicatePropertyVisitor(tiered.size())).toString());

        Map<String, Object> flat = BeanMapUtils.flatten(tiered);
        Assertions.assertEquals(clearFlatNull(getFlat()).toString(), clearFlatNull(flat).toString());

        Map<String, Object> backTiered = BeanMapUtils.tier(flat);
        Assertions.assertEquals(tiered, backTiered);

        User backUser = objectMapper.convertValue(backTiered, User.class);
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
        User user = getUser();
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

    //tag::user[]
    @Data
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    private static class User {
        private Long id;
        private String name;
        private String password;
        private List<Role> roles;
        private Employee employee;
    }

    @Data
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Role {
        private Long id;
        private String name;
        private List<String> tags;
    }

    @Data
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Employee {
        private Long id;
        private String name;
        private List<String> tags;
    }


}
