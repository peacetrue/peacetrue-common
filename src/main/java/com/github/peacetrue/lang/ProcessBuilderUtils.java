package com.github.peacetrue.lang;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static com.github.peacetrue.lang.CommandUtils.sh;

/**
 * ProcessBuilder 工具类。
 *
 * @author peace
 **/
@Slf4j
public class ProcessBuilderUtils {

    private ProcessBuilderUtils() {
    }

    /**
     * 使用 inheritIO 执行命令。
     *
     * @param commands 命令
     * @return 进程
     * @throws IOException 如果发生输入输出错误
     * @see Runtime#exec(String[])
     */
    public static Process exec(String... commands) throws IOException {
        return new ProcessBuilder()
                .inheritIO()
                .command(commands)
                .start();
    }

    /**
     * 列出当前 java 进程打开的文件。
     *
     * @param commands 命令
     * @return 进程
     * @throws IOException 如果进程执行过程中发生输入输出异常
     */
    public static Process lsofCurrent(String... commands) throws IOException {
        return ProcessBuilderUtils.exec(sh(String.format("lsof -nP -p %s %s", getPid(), String.join(" ", commands))));
    }

    /**
     * 获取当前 java 命令的进程号。
     *
     * @return 进程号
     */
    static int getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(name.split("@")[0]);
    }

    /**
     * 按行记录输入流中的信息（异步）。
     *
     * @param inputStream 输入流
     */
    public static void logAsync(InputStream inputStream) {
        new Thread(() -> log(inputStream)).start();
    }

    /**
     * 按行记录输入流中的信息。
     *
     * @param inputStream 输入流
     */
    public static void log(InputStream inputStream) {
        List<Byte> byteList = new LinkedList<>();
        try {
            while (true) {
                byte bytes = (byte) inputStream.read();
                // reach the end
                if (bytes == -1) {
                    log(byteList);
                    break;
                }
                // reach new line
                if (bytes == '\n') {
                    log(byteList);
                    byteList.clear();
                } else {
                    byteList.add(bytes);
                }
            }
        } catch (IOException e) {
            log.error("log inputStream error", e);
        }
    }

    private static void log(List<Byte> bytes) {
        if (!bytes.isEmpty()) log.debug("{}", new String(toBytes(bytes), StandardCharsets.UTF_8));
    }

    private static byte[] toBytes(List<Byte> byteList) {
        int i = 0;
        byte[] byteArray = new byte[byteList.size()];
        for (Byte bytes : byteList) {
            byteArray[i++] = bytes;
        }
        return byteArray;
    }
}
