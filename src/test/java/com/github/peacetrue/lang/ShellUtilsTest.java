package com.github.peacetrue.lang;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

/**
 * @author peace
 **/
@Slf4j
class ShellUtilsTest {

    /** 基础网络使用案例 */
    @Test
    void net() throws IOException {
        Assertions.assertTrue(ShellUtils.loopback().startsWith("lo"));

        int from = 10_000;
        List<Integer> ports = ShellUtils.netstatPorts();
        log.info("ports: {}", ports);
        int unusedPort = ShellUtils.getUnusedPort(ports, from);
        log.info("unusedPort: {}", unusedPort);
        Assertions.assertTrue(unusedPort >= from);

        ServerSocket serverSocket = new ServerSocket(unusedPort);
        Assertions.assertEquals(1, ShellUtils.netstatCount(unusedPort));
    }


}
