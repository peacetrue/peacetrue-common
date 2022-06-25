package com.github.peacetrue.util;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.ArrayUtils;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author peace
 **/
class MapUtilsTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    @Test
    void from() {
        int length = 10;
        String[] keys = EASY_RANDOM.objects(String.class, length).toArray(String[]::new);
        Object[] values = EASY_RANDOM.objects(Object.class, length).toArray(Object[]::new);
        Map<String, Object> map = MapUtils.from(keys, values);
        Assertions.assertTrue(map.size() <= length);
        map.forEach((key, value) -> {
            Assertions.assertEquals(value, values[ArrayUtils.indexOf(keys, key)]);
        });
    }

    @Test
    void values() {
        ImmutableMap<String, Serializable> map = ImmutableMap.of("id", 1L, "name", "zhang");
        List<Serializable> values = MapUtils.values(map, "name", "id");
        Assertions.assertEquals(2, values.size());
        Assertions.assertEquals(map.get("id"), values.get(1));
        Assertions.assertEquals(map.get("name"), values.get(0));
    }

    @Test
    void values2() {
        ImmutableMap<String, Serializable> map = ImmutableMap.of("id", 1L, "name", "zhang");
        List<List<Serializable>> values = MapUtils.values(Arrays.asList(map, map, map), "name", "id");
        Assertions.assertEquals(3, values.size());
        List<Serializable> values2 = values.get(0);
        Assertions.assertEquals(map.get("id"), values2.get(1));
        Assertions.assertEquals(map.get("name"), values2.get(0));
    }

    public static <K, V> void remove(Map<K, V> map, BiPredicate<K, V> matched) {
        map.entrySet().removeIf(entry -> matched.test(entry.getKey(), entry.getValue()));
    }

    @Test
    void remove() {
        int length = 10;
        String[] keys = EASY_RANDOM.objects(String.class, length).toArray(String[]::new);
        Object[] values = EASY_RANDOM.objects(Object.class, length).toArray(Object[]::new);
        Map<String, Object> map = MapUtils.from(keys, values);
        int size = map.size();
        remove(map, (key, value) -> key.equals(keys[0]));
        Assertions.assertEquals(size - 1, map.size());
    }
}
