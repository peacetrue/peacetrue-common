package com.github.peacetrue.bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.github.peacetrue.HelpAntora;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.*;

import static com.github.peacetrue.bean.CustomReadonlyBeanTraverser.exclude;

/**
 * @author peace
 **/
@Slf4j
public class BeanCommon {

    //tag::writeProps[]

    public static final JavaPropsSchema SCHEMA = JavaPropsSchema.emptySchema()
            // 处理数组下标时，使用中括号([])代替点号(.)
            .withWriteIndexUsingMarkers(true)
            // 数组下标从 0 开始，默认从 1 开始
            .withFirstArrayOffset(0);

    public static String writeValueAsPropsString(Object object) throws JsonProcessingException {
        JavaPropsMapper javaPropsMapper = new JavaPropsMapper();
        return javaPropsMapper.writer(SCHEMA)
                .writeValueAsString(object)
                // 移除尾部的换行
                .replaceFirst("\n$", "");
    }
    //end::writeProps[]


    //tag::writeJson[]
    public static String writeValueAsJsonString(Object object) throws JsonProcessingException {
        JsonMapper jsonMapper = new JsonMapper();
        return jsonMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object)
                // 移除尾部的换行
                .replaceFirst("\n$", "");
    }
    //end::writeJson[]

    public static Map<String, Object> clearFlatNull(Map<String, Object> flat) {
        return flat.entrySet().stream().filter(item -> item.getValue() != null).collect(
                LinkedHashMap::new, (c, e) -> c.put(e.getKey(), e.getValue()), LinkedHashMap::putAll
        );
    }

    //tag::buildStringBeanVisitor[]
    public static StringBeanVisitor buildStringBeanVisitor() {
        return StringBeanVisitor.builder()
                // 设置 Bean 遍历器，支持无写入方法的属性，然后按代码书写顺序排序属性
                .beanTraverser(new OrderedBeanTraverser<>(StandardBeanTraverser.NULLABLE_WRITE_METHOD))
                // 转换叶子节点对象为字符串，null 对象默认输出为 null，这里改为空字符串
                .toString(object -> Objects.toString(object, ""))
                .build();
    }
    //end::buildStringBeanVisitor[]

    //tag::buildCustomStringBeanVisitor[]
    public static StringBeanVisitor buildCustomStringBeanVisitor() {
        return StringBeanVisitor.builder()
                // 设置 Bean 遍历器，获取无参有返回值得方法，然后根据方法名排除指定方法
                .beanTraverser(new CustomReadonlyBeanTraverser(exclude("hashCode", "toString")))
                .isLeafType(CoreBeanVisitor.in(FileTime.class)
                        .or(CoreBeanVisitor::isSimpleValueType)
                        .or((path, clazz) -> path != null && path.endsWith("fileKey"))
                )
                .build();
    }

    //end::buildCustomStringBeanVisitor[]

    //tag::PosixFileAttributeView[]
    public static final PosixFileAttributeView POSIX_FILE_ATTRIBUTE_VIEW = getPosixFileAttributeView();

    public static PosixFileAttributeView getPosixFileAttributeView() {
        return Files.getFileAttributeView(
                HelpAntora.PROJECT_DIR.resolve("gradlew"),
                PosixFileAttributeView.class
        );
    }
    //end::PosixFileAttributeView[]


    //tag::user[]
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User implements Serializable {
        private Long id;
        private String name;
        private String password;
        private List<Role> roles;
        private Employee employee;
    }
    //end::user[]

    //tag::role[]
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Role implements Serializable {
        private Long id;
        private String name;
        private List<String> tags;
    }
    //end::role[]

    //tag::employee[]
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Employee implements Serializable {
        private Long id;
        private String name;
        private List<String> tags;
    }
    //end::employee[]


    //tag::user0[]
    public static final User USER = getUser();

    public static User getUser() {
        return User.builder().id(null).name("admin").password("")
                .roles(Arrays.asList(
                        Role.builder().id(1L).name("admin").tags(Arrays.asList("read", "write")).build(),
                        null,
                        Role.builder().name("user").build(),
                        Role.builder().id(3L).build()
                ))
                .employee(Employee.builder().id(1L).name("Jone").tags(Arrays.asList("good", "better")).build())
                .build();
    }
    //end::user0[]

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

    public static Map<String, Object> getFlat() {
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

}
