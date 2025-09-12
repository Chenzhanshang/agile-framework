package com.agile.event.config.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenzhanshang
 * @date 2025/8/29 15:34
 * @describe :
 */
@Data
@NoArgsConstructor
public class DefaultDomainEvent<T> implements DomainEvent<T> {
    /**
     * 事件唯一标识
     */
    private String uuid;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件源（产生事件的服务名）
     * 例：user-service
     */
    private String source;

    /**
     * 事件产生的时间戳
     */
    private Long timestamp;

    /**
     * 消息体
     */
    private T data;

    public DefaultDomainEvent(String uuid, String eventType, String source, Long timestamp, T data) {
        this.uuid = uuid;
        this.eventType = eventType;
        this.source = source;
        this.timestamp = timestamp;
        this.data = data;
    }
}
