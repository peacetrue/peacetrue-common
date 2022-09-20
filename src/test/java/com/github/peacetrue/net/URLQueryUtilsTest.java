package com.github.peacetrue.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.peacetrue.beanmap.BeanMapUtils;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.peacetrue.test.SourcePathUtils.getTestResourceAbsolutePath;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * @author peace
 **/
@Slf4j
class URLQueryUtilsTest {

    @Test
    void flow() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        String query = "requestId&id=&name=admin&password=123456" +
                "&roles[0].id=1&roles[0].name=admin&roles[0].tag[0]=read&roles[0].tag[1]=write&roles[1].id=3&roles[2].name=user" +
                "&employee.id=1&employee.name=Jone&employee.tag[0]=good&employee.tag[1]=better";
        log.info("    query: {}", query);
        Files.write(Paths.get(getTestResourceAbsolutePath("/100-query.txt")), query.getBytes(StandardCharsets.UTF_8), CREATE, TRUNCATE_EXISTING);
        Map<String, List<String>> form = URLQueryUtils.parseQuery(query + "&  &");
        log.info("formParam: {}", form);
        Files.write(Paths.get(getTestResourceAbsolutePath("/110-form.json")), objectWriter.writeValueAsBytes(form), CREATE, TRUNCATE_EXISTING);

        Map<String, Object> flat = URLQueryUtils.toBeanMap(form);
        log.info("     flat: {}", flat);
        Files.write(Paths.get(getTestResourceAbsolutePath("/120-flat.json")), objectWriter.writeValueAsBytes(flat), CREATE, TRUNCATE_EXISTING);

        Map<String, Object> tiered = BeanMapUtils.tier(flat);
        log.info("   tiered: {}", tiered);
        Files.write(Paths.get(getTestResourceAbsolutePath("/130-tiered.json")), objectWriter.writeValueAsBytes(tiered), CREATE, TRUNCATE_EXISTING);

        Map<String, Object> backFlat = BeanMapUtils.flatten(tiered);
        log.info(" backFlat: {}", backFlat);
        Assertions.assertEquals(flat, backFlat);

        Map<String, List<String>> backForm = URLQueryUtils.fromBeanMap(backFlat);
        log.info(" backForm: {}", backForm);
        Assertions.assertEquals(form, backForm);

        String backQuery = URLQueryUtils.toQuery(backForm);
        log.info(" backQuery: {}", backQuery);
        Assertions.assertEquals(query.length(), URLCodecUtils.decode(backQuery).length());
    }

    @Test
    void tailArraySupported() {
        String query = "tag[0]=good&tag[0]=ok&tag[2]=better";
        log.info("         query: {}", query);
        Map<String, List<String>> flat = URLQueryUtils.parseQuery(query);
        log.info("          flat: {}", flat);
        Map<String, Object> tier = BeanMapUtils.tier(URLQueryUtils.toBeanMap(flat));
        log.info("          tier: {}", tier);
        Assertions.assertTrue(tier.get("tag") instanceof List);
    }

    @Test
    void flattenHandleValue() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Map<String, List<String>> flat = URLQueryUtils.flatten(ImmutableMap.of(
                "list", list,
                "element", 1
        ));
        Assertions.assertEquals("1", flat.get("list[0]").get(0));
        Assertions.assertEquals("1", flat.get("element").get(0));
    }

    @Test
    void toQuery() {
        Assertions.assertEquals(
                "name",
                URLQueryUtils.toQuery(Collections.singletonMap("name", Arrays.asList(null, null)))
        );
        Assertions.assertEquals(
                "name",
                URLQueryUtils.toQuery(Collections.singletonMap("name", null))
        );
    }

    @Test
    void toBeanMap() {
        Assertions.assertEquals(
                Collections.singletonMap("name", null),
                URLQueryUtils.toBeanMap(Collections.singletonMap("name", Arrays.asList(null, null)))
        );
        Assertions.assertEquals(
                Collections.singletonMap("name", null),
                URLQueryUtils.toBeanMap(Collections.singletonMap("name", null))
        );
    }
}
