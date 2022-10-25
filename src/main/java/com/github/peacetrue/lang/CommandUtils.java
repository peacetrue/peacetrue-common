package com.github.peacetrue.lang;

import lombok.extern.slf4j.Slf4j;

/**
 * 命令工具类。
 *
 * @author peace
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

}
