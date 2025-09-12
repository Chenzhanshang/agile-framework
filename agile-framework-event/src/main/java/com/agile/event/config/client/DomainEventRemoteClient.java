package com.agile.event.config.client;

import com.agile.event.config.client.callback.DomainEventListener;

/**
 * @author: chenzhanshang
 * @date 2025/09/10 10:33
 * @desc:
 **/
public interface DomainEventRemoteClient extends DomainEventClient, DomainEventSubscriber {

    /**
     * 事件监听
     *
     * @param listener
     */
    void setOnEventListener(DomainEventListener listener);
}
