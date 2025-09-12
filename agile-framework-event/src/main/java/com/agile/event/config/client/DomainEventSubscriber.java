package com.agile.event.config.client;

import com.agile.event.config.dto.DomainEvent;

import java.util.function.Consumer;

/**
 * 领域事件订阅者
 *
 * @author: chenzhanshang
 * @date 2025/08/08 11:25
 * @desc:
 **/
public interface DomainEventSubscriber {

    /**
     * 订阅领域事件
     *
     * @param source    事件源
     * @param eventType 事件类型
     * @param consumer  数据消费
     */
    void subscriber(String source, String eventType, Consumer<DomainEvent<?>> consumer);

}
