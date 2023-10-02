package com.github.peacetrue.lang;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.peacetrue.lang.CommandUtils.NET_CARD_LOOPBACK;
import static com.github.peacetrue.lang.CommandUtils.sh;

/**
 * @author peace
 **/
@Slf4j
public class ShellUtils {
    private ShellUtils() {
    }

    /**
     * 执行命令并获取结果
     *
     * @param commands 命令
     * @return 结果
     * @throws IOException IO 异常
     */
    public static String exec(String... commands) throws IOException {
        Process process = Runtime.getRuntime().exec(commands);
        return toString(process.getInputStream());
    }

    /** 未受检的 {@link Supplier} */
    public interface UncheckedSupplier<T> {
        /**
         * 获取结果
         *
         * @return 结果
         * @throws IOException IO 异常
         */
        T get() throws IOException;
    }

    /**
     * 记录日志
     *
     * @param title   标题
     * @param content 内容
     * @throws IOException IO 异常
     */
    public static void log(String title, UncheckedSupplier<String> content) throws IOException {
        log.info("{}\n{}", title, content.get());
    }

    private static String toString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    private static String format(String string) {
        //移除结果末尾的换行符
        return string.replaceFirst("\n$", "").trim();
    }


    /**
     * 获取环回网卡名称
     *
     * @return 环回网卡名称
     * @throws IOException IO 异常
     */
    public static String loopback() throws IOException {
        return exec(sh(CommandUtils.tcpdump(NET_CARD_LOOPBACK)));
    }

    /**
     * 获取使用中的端口号集合（已升序排列）
     *
     * @return 端口号集合
     * @throws IOException IO 异常
     */
    public static List<Integer> netstatPorts() throws IOException {
        String ports = exec(sh(CommandUtils.NETSTAT_PORTS));
        return Arrays.stream(ports.split("\n"))
                .filter(port -> port.matches("\\d+")).map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * 获取从 from 起始的未使用的端口号
     *
     * @param ports 端口号集合
     * @param from  从什么端口号起始，即最小的端口号
     * @return 未使用的端口号
     */
    public static int getUnusedPort(List<Integer> ports, int from) {
        while (ports.contains(from)) from++;
        return from;
    }

    /**
     * 统计指定端口号相关的网络连接
     *
     * @param port 端口号
     * @return 网络连接数量
     * @throws IOException IO 异常
     */
    public static String netstat(int port) throws IOException {
        return exec(sh(CommandUtils.netstatByPort(port)));
    }

    /**
     * 统计指定端口号相关的网络连接数量
     *
     * @param port 端口号
     * @return 网络连接数量
     * @throws IOException IO 异常
     */
    public static int netstatCount(int port) throws IOException {
        return Integer.parseInt(format(exec(sh(CommandUtils.netstatCount(port)))));
    }


}
