package com.wondersgroup.yss.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
/**
 * 分布式ID, 基于"雪花算法", 但对各段的位数进行了调整.
 * 适合中小型企业, 单应用并发写 <= 6W/s, 集群节点(容器或虚拟机) < 1024.
 * long 型共64位, 占用58位, 首6位空闲.
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |00000000|11111111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |------xx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxxxxx--|--------|--------|--------|
 * |--------|--------|--------|--------|------xx|xxxxxxxx|--------|--------|
 * |--------|--------|--------|--------|--------|--------|xxxxxxxx|xxxxxxxx|
 * <p>
 * 时间戳位: 32位, 单位秒, (2的32次方=2147483647秒=68年)
 * 机器位 : 10位, 1024台虚拟机(含Docker容器)
 * 序列号位: 16位, 2的16次方=65535, 即支持单应用6W/s并发写入
 * <p>
 * 若要适配更高TPS, 可将时间戳调整为毫秒为单位
 *
 */
@Slf4j
@Component
public class SnowFlake {
    /**
     * 开始时间, 2020-1-1. 时间戳位上并不存储实际的时间, 而是当前时间与开始时间的差值(秒)
     */
    private final static long START_TIMESTAMP = LocalDate.of(2020, 1, 1)
            .atStartOfDay(ZoneId.of("Z")).toEpochSecond();
    /**
     * ID结构: 32位时间戳(秒) + 10位机器 + 16位序列号
     */
    private final static long TIMESTAMP_BIT = 32;
    private final static long MACHINE_BIT = 10;
    private final static long SEQUENCE_BIT = 16;
    /**
     * 最大机器号
     */
    private final static long MAX_MACHINE = 1 << MACHINE_BIT - 1;
    /**
     * 最大序列号
     */
    private static final long MAX_SEQUENCE = 1 << SEQUENCE_BIT - 1;
    /**
     * 机器位和时间戳左移位数.
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long TIMESTAMP_LEFT = MACHINE_BIT + SEQUENCE_BIT;
    /**
     * 机器标识
     */
    private static long machineId = 1;
    private static boolean machineInitialized = false;
    /**
     * 序列号
     */
    private static long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private static long lastTimestamp = -1L;
    static {
        initMachineId();
    }
    /**
     * 获取分布式ID
     *
     * @return
     */
    public static long nextId() {
        return nextId(getNowSeconds());
    }
    private static synchronized long nextId(long nowSeconds) {
        long currentTimestamp = nowSeconds;
        // 当前时间异常, 自动校准
        if (currentTimestamp < lastTimestamp) {
            currentTimestamp = lastTimestamp;
        }
        // 相同秒内，序列号自增
        if (currentTimestamp == lastTimestamp) {
            long nextSequence = sequence + 1;
            //同一秒的序列数已经达到最大, 获取下一秒的ID(直接使用下一秒)
            if (nextSequence >= MAX_SEQUENCE) {
                return nextId(nowSeconds + 1);
            } else {
                sequence = nextSequence;
            }
        } else {
            // 不同秒内，序列号置重置为0, 重新计数
            sequence = 0L;
        }
        lastTimestamp = currentTimestamp;
        // 当前时间与开始时间的差值
        long timeDelta = currentTimestamp - START_TIMESTAMP;
        // 各段结构拼接("|"操作)成ID
        return timeDelta << TIMESTAMP_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }
    private static long getNowSeconds() {
        return System.currentTimeMillis() / 1000;
    }
    /**
     * 通过hostname初始化机器ID, 将hostname各字符累加并对machineId最大值取余
     * <p>虚拟主机: 可手工设置, eg:prod1 </p>
     * <p>普通容器: 默认容器ID为HOSTNAME, eg: b88598ebca38</p>
     * <p>K8s Pod: 默认Pod ID为HOSTNAME, eg: user-service-433cf-5f6d48c7f4-8fclh</p>
     *
     * @return
     */
    public static void initMachineId() {
        try {
            if (!machineInitialized) {
                String hostname = InetAddress.getLocalHost().getHostName();
                char[] hostnameChars = hostname.toCharArray();
                int sumOfHostname = 0;
                for (int i = 0, length = hostnameChars.length; i < length; i++) {
                    sumOfHostname += hostnameChars[i];
                }
                machineId = sumOfHostname % MAX_MACHINE;
                log.info("====> Init machineId. hostname[" + hostname + "], machineId[" + machineId + "]");
            }
        } catch (UnknownHostException e) {
            log.error("====> Init machineId failure");
            e.printStackTrace();
        } finally {
            machineInitialized = true;
        }
    }
    /**
     * 覆盖默认machineID
     *
     * @param mid machineId
     */
    public static void setMachineId(long mid) {
        if (mid > 0) {
            machineId = mid;
        }
    }
}