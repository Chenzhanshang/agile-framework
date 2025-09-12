package com.agile.event.config.client.callback;

import com.agile.event.config.dto.DomainEvent;

/**
 * @author: chenzhanshang
 * @date 2025/09/09 11:08
 * @desc:
 **/
public interface DomainEventListener {

    /**
     * 当事件触发
     *
     * @param domainEvent
     */
    void onEvent(DomainEvent<?> domainEvent);
}
