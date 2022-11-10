package com.github.peacetrue.bean;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.peacetrue.BeanCommon;
import com.github.peacetrue.HelpAntora;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author peace
 **/
class BeanHelpAntoraTest {

    @Test
    @SneakyThrows
    void userJson() {
        Files.write(
                HelpAntora.BEAN_DIR.resolve("user.json"),
                BeanCommon.writeValueAsJsonString(BeanCommon.USER).getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    @SneakyThrows
    void userProperties() {
        Files.write(
                HelpAntora.BEAN_DIR.resolve("user.properties"),
                BeanCommon.writeValueAsPropsString(BeanCommon.USER).getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    @SneakyThrows
    void userYaml() {
        Files.write(
                HelpAntora.BEAN_DIR.resolve("user.yaml"),
                new YAMLMapper().writeValueAsString(BeanCommon.USER).getBytes(StandardCharsets.UTF_8)
        );
    }
}
