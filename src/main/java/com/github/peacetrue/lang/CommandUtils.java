package com.github.peacetrue.lang;

import lombok.extern.slf4j.Slf4j;

/**
 * 命令工具类。
 *
 * @author peace
 * @see ProcessBuilder#command()
 */
@Slf4j
public class CommandUtils {

    private CommandUtils() {
    }

    /**
     * 使用 sh 封装命令，避免管道问题。
     *
     * @param commands 原始命令
     * @return sh 封装后的命令
     * @see <a href="https://stackoverflow.com/questions/3776195/using-java-processbuilder-to-execute-a-piped-command">ProcessBuilder 如何支持管道命令</a>
     */
    public static String[] sh(String... commands) {
        String command = String.join(" ", commands);
        log.debug("/bin/sh -c \"{}\"", command);
        return new String[]{"/bin/sh", "-c", command};
    }

    /**
     * 使用 ssh 封装命令。
     *
     * @param user     用户
     * @param host     主机
     * @param commands 原始命令
     * @return ssh 封装后的命令
     */
    public static String ssh(String user, String host, String commands) {
        return String.format("ssh %s@%s '%s'", user, host, commands);
    }


    /** 网络连接统计 */
    public static final String NETSTAT = "netstat -nat";
    /** 网络连接占用的端口号：tcp    0      0 127.0.0.1:25    0.0.0.0:*       LISTEN */
    public static final String NETSTAT_PORTS = String.format("%s | grep 'tcp' | awk '{print $4}' | awk -F '[.:]' '{print $NF}'", NETSTAT);

    /**
     * 构建 netstat 命令。
     * <br>netstat 是一个用于查看网络连接信息的命令行工具：
     * <br>-n：禁用将 IP 地址和端口号解析为主机名和服务名。这样做可以提高命令执行速度，同时以数字形式显示 IP 地址和端口号。
     * <br>-a：显示所有的网络连接，包括监听状态和非监听状态的连接。这包括 TCP、UDP 和 UNIX 域套接字（AF_UNIX）等连接。
     * <br>-t：仅显示 TCP 连接信息。这将筛选出仅涉及 TCP 协议的连接，排除 UDP 和其他协议。
     * <p>
     * Rename method "netstat" to prevent any misunderstanding/clash with field "NETSTAT".
     *
     * @param port 端口号
     * @return 命令
     */
    public static String netstatByPort(int port) {
        return String.format("%s | grep %s", NETSTAT, port);
    }

    /**
     * 统计网络连接数量
     *
     * @param port 端口号
     * @return 命令
     */
    public static String netstatCount(int port) {
        return String.format("%s | wc -l", netstatByPort(port));
    }

    /** 回环网卡 */
    public static final String NET_CARD_LOOPBACK = "Loopback";
    /** 无线网卡 */
    public static final String NET_CARD_WIRELESS = "Wireless";

    /**
     * 获取网卡名称
     * <pre>
     * tcpdump -D | grep Loopback
     * 10.lo0 [Up, Running, Loopback]
     * </pre>
     *
     * @param type 网卡类型
     * @return 命令
     */
    public static String tcpdump(String type) {
        return String.format("tcpdump -D | grep %s | awk -F '[. ]' '{print $2}'", type);
    }

}
