package com.github.peacetrue.beanmap;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.peacetrue.util.MapUtils;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peace
 **/
@Slf4j
class BeanMapUtilsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JavaType BEAN_MAP = TypeFactory.defaultInstance().constructMapLikeType(LinkedHashMap.class, String.class, Object.class);

    @Test
    void walkTree() {
        User user = getUser();
        //这个转换，保留了 Bean 中的 null 属性
        Map<String, Object> tiered = objectMapper.convertValue(user, BEAN_MAP);
        Assertions.assertEquals(tiered.toString(), BeanMapUtils.walkTree(tiered, new DuplicatePropertyVisitor(tiered.size())).toString());

        Map<String, Object> flat = BeanMapUtils.flatten(tiered);
        Assertions.assertEquals(clearFlatNull(getFlat()).toString(), clearFlatNull(flat).toString());

        Map<String, Object> backTiered = BeanMapUtils.tier(flat);
        Assertions.assertEquals(tiered, backTiered);

        User backUser = objectMapper.convertValue(backTiered, User.class);
        Assertions.assertEquals(user, backUser);
    }

    @Test
    void exceedListMaxSize() {
        BeanMapUtils.setListMaxSize(100);
        Map<String, String> flat = MapUtils.from(new String[]{"users[10000].name"}, new String[]{"1"});
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> BeanMapUtils.tier(flat));
        Assertions.assertEquals("Index 10000 exceeds upper limit: 100", exception.getMessage());
    }

    private static Map<String, Object> clearFlatNull(Map<String, Object> flat) {
        return flat.entrySet().stream().filter(item -> item.getValue() != null).collect(
                LinkedHashMap::new, (c, e) -> c.put(e.getKey(), e.getValue()), LinkedHashMap::putAll
        );
    }

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

    private static User getUser() {
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
        flat.put("roles[0].tags", Arrays.asList("read", "write"));
        flat.put("roles[2].name", "user");
        flat.put("roles[3].id", 3);
        flat.put("employee.id", 1);
        flat.put("employee.name", "Jone");
        flat.put("employee.tags", Arrays.asList("good", "better"));
        return flat;
    }


}
