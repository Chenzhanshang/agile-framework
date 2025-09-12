package com.agile.event.config.dto;

import java.io.Serializable;

/**
 * 领域事件接口
 * @author: chenzhanshang
 * @date 2025/8/28 10:42
 * @desc:
 **/
public interface DomainEvent<T> extends Serializable {
    /**
     * 事件唯一标识
     * 需要保证全局唯一
     *
     * @return
     */
    String getUuid();

    /**
     * 事件源
     * eg: sys-user 、 sys-log 等
     *
     * @return
     */
    String getSource();

    /**
     * 事件类型
     *
     * @return
     */
    String getEventType();

    /**
     * 事件发生时间
     * 记录事件发生时间戳
     *
     * @return
     */
    Long getTimestamp();

    /**
     * 获取消息体
     *
     * @return 返回领域事件主要数据体
     */
    T getData();

}
