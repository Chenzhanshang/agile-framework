package com.agile.framework.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 雪花id实际生成工具单例包装
 *
 * @date: 2023/8/24 10:07
 * @author: chenzhanshang
 */
@Slf4j
public class SnowFlakeIdHelper {
    /**
     * 单例实例
     */
    private static SnowFlakeIdWorker snowflake;
    /**
     * 默认数据中心id
     */
    private static final Long DEFAULT_DATA_CENTER_ID = 0L;
    /**
     * 默认机器设备id
     */
    private static final Long DEFAULT_MACHINE_ID = 0L;
    /**
     * 分布式雪花id生成器key
     */
    public static final String CLUSTER_SNOW_FLAKE_KEY = "agile.framework.snowflake.key";

    /**
     * 根据自增值生成对应的雪花id生成器
     * 由于数据中心以及设备id都是[0,32),我们对自增值对 32*32 取余数，然后我们再对剩余的结果分别做 除法以及取余，获得两个结果
     * 除的结果作为数据中心id，取余的结果作为设备id
     *
     * @param increment
     */
    public static void init(Long increment) {
        if (null == increment || increment < 0) throw new IllegalArgumentException();
        if (null == snowflake) {
            long index = increment % (32 * 32);
            long centerId = index / 32;
            long machineId = index % 32;
            init(centerId, machineId);
        }
    }

    /**
     * 交由应用层去决定数据中心以及设备id的取值
     *
     * @param centerId  数据中心
     * @param machineId 设备
     */
    public synchronized static void init(Long centerId, Long machineId) {
        if (null == snowflake) {
            try {
                snowflake = new SnowFlakeIdWorker(centerId, machineId);
                log.info("初始化SnowFlakeIdWorker: centerId:{},machineId:{}", centerId, machineId);
            } catch (Exception e) {
                log.error("初始化雪花id生成器异常: centerId :{} machineId:{} message:{}", centerId, machineId, e.getMessage(), e);
                snowflake = new SnowFlakeIdWorker(DEFAULT_DATA_CENTER_ID, DEFAULT_MACHINE_ID);
            }
        }
    }

    /**
     * 应用应该在这个getId方法之前执行init(Long centerId, Long machineId)方法
     * 否则将会采用默认的参数进行装配雪花id {centerId:0,machineId:0}
     */
    public static long getId() {
        if (null == snowflake) {
            init(DEFAULT_DATA_CENTER_ID, DEFAULT_MACHINE_ID);
        }
        return snowflake.nextId();
    }
}
