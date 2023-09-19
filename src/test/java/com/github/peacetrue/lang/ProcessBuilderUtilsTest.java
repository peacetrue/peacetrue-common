package com.github.peacetrue.lang;

import com.github.peacetrue.test.SourcePathUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

/**
 * @author peace
 **/
@Slf4j
class ProcessBuilderUtilsTest {

    @Test
    @SneakyThrows
    void exec() {
        Assertions.assertEquals(0, ProcessBuilderUtils.exec("echo", "1").waitFor());
        Assertions.assertEquals(0, ProcessBuilderUtils.exec(CommandUtils.sh("echo", "1", "|", "xargs", "echo")).waitFor());
    }

    @Test
    @SneakyThrows
    void logAsync() {
        String cwd = SourcePathUtils.getTestResourceAbsolutePath("/lang");
        Path logPath = Paths.get(cwd).resolve("async.log");
        int length = 5;
        ProcessBuilder builder = new ProcessBuilder()
                .directory(new File(cwd))
                .command("./async.sh");
        {
            Process process = builder.start();
            log.info("{}", IOUtils.toString(process.getInputStream()));//阻塞，不能与 java 进程交互执行
            write(logPath, length);
        }
        {
            log.info("---------- async ------------");
            Process process = builder.start();
            ProcessBuilderUtils.logAsync(process.getInputStream());//非阻塞，可以与 java 进程交互执行
            write(logPath, length);
        }
        Files.deleteIfExists(logPath);
    }

    private static void write(Path logPath, int length) throws IOException, InterruptedException {
        for (int i = 0; i < length; i++) {
            log.info("write: {}", i);
            Files.write(logPath, String.valueOf(i).getBytes(StandardCharsets.UTF_8), CREATE, TRUNCATE_EXISTING);
            Thread.sleep(1_000);
        }
    }

    //@Test
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
    //@Test
    @Deprecated
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
