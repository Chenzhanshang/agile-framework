package com.agile.event.config.support;

import com.agile.event.config.client.DomainEventRemoteClient;
import com.agile.event.config.client.callback.DomainEventListener;
import com.agile.framework.util.RandomUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chenzhanshang
 * @date 2025/9/1 10:33
 * @describe :
 */
public abstract class BaseEventMqService implements EventMqService, DomainEventRemoteClient {
    private final String mqNamePrefix = "agile.framework.event";

    /**
     * 监听者列表
     */
    protected Set<DomainEventListener> domainEventListeners = new HashSet<>();

    /**
     * 做一层封装
     * 由 （当前服务名+订阅服务名+订阅服务事件）构建出唯一的一条队列通道
     *
     * @param serviceName
     * @return
     */
    protected String wrapperQueue(String serviceName, String source, String type) {
        return ("paddy.framework.event.queue." + serviceName + "." + source + "." + type).toLowerCase();
    }


    /**
     * 包装临时队列
     * 由 （当前服务名+订阅服务名+订阅服务事件）构建出唯一的一条队列通道
     *
     * @param serviceName
     * @return
     */
    protected String wrapperTempQueue(String serviceName, String source, String type) {
        return wrapperQueue(serviceName, source, type) + "." + RandomUtils.createRandomString(5);
    }

    /**
     * 包装交换机名称
     */
    protected String wrapperExchange(String serviceName, String type) {
        return "agile.framework.event.exchange." + serviceName + "." + type;
    }

    protected String wrapperRoutingKey(String routingKey) {
        return "agile.framework.event.routingKey." + routingKey;
    }

    @Override
    public void setOnEventListener(DomainEventListener listener) {
        this.domainEventListeners.add(listener);
    }
}
