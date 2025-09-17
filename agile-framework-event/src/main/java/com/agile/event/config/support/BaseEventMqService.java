package com.agile.event.config.support;

import com.agile.event.config.client.DomainEventRemoteClient;
import com.agile.event.config.client.callback.DomainEventListener;
import com.agile.framework.util.JsonUtil;
import com.agile.framework.util.RandomUtils;
import org.springframework.core.annotation.Order;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chenzhanshang
 * @date 2025/9/1 10:33
 * @describe :
 */
public abstract class BaseEventMqService implements EventMqService, DomainEventRemoteClient {
    /**
     * 监听者列表
     */
    protected Set<DomainEventListener> domainEventListeners = new HashSet<>();

    @Override
    public void setOnEventListener(DomainEventListener listener) {
        this.domainEventListeners.add(listener);
    }

}
