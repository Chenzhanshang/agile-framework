package com.agile.event.config.support;

/**
 * 远程消息支持
 * @author chenzhanshang
 * @date 2025/9/1 10:30
 * @describe :
 */
public interface EventMqService {
    /**
     * 发送消息
     * @param exchange
     * @param routingKey
     * @param data
     */
    boolean send(String exchange, String routingKey, Object data);
}
