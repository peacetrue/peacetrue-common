package com.github.peacetrue.lang;

import com.github.peacetrue.test.SourcePathUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author peace
 **/
@Slf4j
class ProcessBuilderUtilsTest {

    /** 继承 IO 输出乱序 */
    @Test
    @SneakyThrows
    void inheritIOOutOrder() {
        log.info("log: start");
        Process process = new ProcessBuilder()
                .directory(new File(SourcePathUtils.getTestResourceAbsolutePath("/lang")))
                .command("echo-continue.sh")
                .inheritIO()
                .start();
        Assertions.assertEquals(0, process.waitFor());
        log.info("log: end");
    }

    @Test
    @SneakyThrows
    void exec() {
        Assertions.assertEquals(0, ProcessBuilderUtils.exec("echo", "1").waitFor());
        Assertions.assertEquals(0, ProcessBuilderUtils.exec(CommandUtils.sh("echo", "1", "|", "echo")).waitFor());
    }

    //            @Test
    @SneakyThrows
    void createTxt() {
        String folder = SourcePathUtils.getTestResourceAbsolutePath("/lang");
        Runtime runtime = Runtime.getRuntime();
        runtime.exec(String.format("dd if=/dev/zero of=%s bs=1K count=1", folder + "/1K.txt")).waitFor();
        runtime.exec(String.format("dd if=/dev/zero of=%s bs=1M count=1", folder + "/1M.txt")).waitFor();
        Process process = runtime.exec(String.format("ls -lah %s", folder));
        process.waitFor();
        log.info("ls:\n{}", IOUtils.toString(process.getInputStream()));
    }

    /** 溢出输入缓冲区时阻塞进程 */
    @Test
    @SneakyThrows
    void blockByOverflowInputBuffer() {
        String folder = SourcePathUtils.getTestResourceAbsolutePath("/lang");
        Runtime runtime = Runtime.getRuntime();

        Assertions.assertTrue(
                runtime.exec(String.format("cat %s", folder + "/1K.txt")).waitFor(2, TimeUnit.SECONDS),
                "可以正常读取 1K 的文件"
        );
        Assertions.assertFalse(
                runtime.exec(String.format("cat %s", folder + "/1M.txt")).waitFor(2, TimeUnit.SECONDS),
                "不能正常读取 1M 的文件"
        );
        Assertions.assertTrue(
                new ProcessBuilder().inheritIO().command("cat", folder + "/1M.txt").start().waitFor(2, TimeUnit.SECONDS),
                "inheritIO 可以正常读取 1M 的文件"
        );

        Process process = runtime.exec(String.format("cat %s", folder + "/1M.txt"));
        ProcessBuilderUtils.logAsync(process.getInputStream());
        Assertions.assertTrue(process.waitFor(2, TimeUnit.SECONDS), "在读取输入信息后，可以正常读取 1M 的文件");
    }


}
