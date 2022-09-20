package com.github.peacetrue.time;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 运行时间工具类。可用于计算方法的的运行时间。
 *
 * @author peace
 **/
public class ElapsedTimeUtils {

    private ElapsedTimeUtils() {
    }

    /** 时钟。用于获取当前时间（毫秒） */
    private static Supplier<Long> clock = System::currentTimeMillis;

    /**
     * 设置时钟。
     *
     * @param clock 时钟，可获取当前时间（毫秒）
     */
    public static void setClock(Supplier<Long> clock) {
        ElapsedTimeUtils.clock = Objects.requireNonNull(clock);
    }

    /**
     * 获取指定函数的运行时间。
     *
     * @param runnable 一个无参无返回值的函数。任何其他形式的函数，需自行转换
     * @return 返回指定函数的运行时间（毫秒）
     */
    public static long evaluate(Runnable runnable) {
        Long start = clock.get();
        runnable.run();
        return clock.get() - start;
    }

}
