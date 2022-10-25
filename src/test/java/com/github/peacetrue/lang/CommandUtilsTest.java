package com.github.peacetrue.lang;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.peacetrue.lang.CommandUtils.sh;

@Slf4j
class CommandUtilsTest {

    @Test
    void ssh() {
        Assertions.assertEquals("ssh root@localhost 'ls -a'", CommandUtils.ssh("root", "localhost", "ls -a"));
    }

    @Test
    void getPid() {
        Assertions.assertDoesNotThrow(ProcessBuilderUtils::getPid);
    }

    @SneakyThrows
    @Test
    void lsof$$() {
        Assertions.assertEquals(0, Runtime.getRuntime().exec(sh("lsof -p $$")).waitFor());
    }

    @Test
    @SneakyThrows
    void lsofCurrent() {
        // lsof -p $$ | grep -E ' [[:digit:]]{1,3}[ rwu]'
        Process process = ProcessBuilderUtils.lsofCurrent();
        Assertions.assertEquals(0, process.waitFor());
//        log.info("lsof:\n{}", IOUtils.toString(process.getInputStream()));
    }

}
