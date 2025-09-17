package com.agile.event.config.support;

import com.agile.event.config.dto.DefaultDomainEvent;
import com.agile.event.config.dto.DomainEvent;
import com.agile.framework.exception.FrameworkException;
import com.agile.framework.util.JsonUtil;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.MessageProperties;

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

    /**
     * 格式化传输对象
     * 目前采用json格式传输
     */
    default String formatTransmissionObject(Object o) {
        return JsonUtil.encode(o);
    }


    /**
     * 解析传输对象
     * 目前采用json传输
     * @param body json格式内容
     * @return
     */
    default DomainEvent<?> getObject(String body) {
        return JsonUtil.decode(body, DefaultDomainEvent.class);
    }
}
