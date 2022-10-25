package com.github.peacetrue.util;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * @author peace
 **/
@Slf4j
class MapUtilsTest {

    private static final EasyRandom EASY_RANDOM = new EasyRandom();

    public static <K, V> void remove(Map<K, V> map, BiPredicate<K, V> matched) {
        map.entrySet().removeIf(entry -> matched.test(entry.getKey(), entry.getValue()));
    }

    @Test
    void from() {
        int length = 10;
        String[] keys = EASY_RANDOM.objects(String.class, length).toArray(String[]::new);
        Object[] values = EASY_RANDOM.objects(Object.class, length).toArray(Object[]::new);
        Map<String, Object> map = MapUtils.from(keys, values);
        Assertions.assertTrue(map.size() <= length);
        map.forEach((key, value) ->
                Assertions.assertEquals(value, values[ArrayUtils.indexOf(keys, key)])
        );
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

    @Test
    void prettify() {
        Assertions.assertEquals("\n\t", MapUtils.prettify(Collections.emptyMap()));

        String prettify = MapUtils.prettify(MapUtils.from(
                new String[]{"templateLocation", "optionsLocation", "variablesLocation", "resultPath"},
                new String[]{"file:template", "file:antora-options.properties", "file:antora-variables.properties", "result"}
        ));
        log.info("prettify args: {}", prettify);
        Assertions.assertEquals(
                "\n" +
                        "\ttemplateLocation : file:template\n" +
                        "\toptionsLocation  : file:antora-options.properties\n" +
                        "\tvariablesLocation: file:antora-variables.properties\n" +
                        "\tresultPath       : result",
                prettify);
    }

}
