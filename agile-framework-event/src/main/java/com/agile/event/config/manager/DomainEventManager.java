package com.agile.event.config.manager;

import com.agile.event.config.client.DomainEventClient;
import com.agile.event.config.client.DomainEventRemoteClient;
import com.agile.event.config.dto.DomainEvent;
import com.agile.event.config.factory.DomainEventFactory;
import com.agile.event.config.type.EventMode;
import com.agile.framework.exception.BusinessException;
import com.agile.framework.exception.FrameworkException;
import com.agile.framework.util.Assert;
import com.agile.framework.util.CollectionUtils;
import lombok.Data;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 领域事件管理器
 * 主要用于处理本地事件以及分布式微服务事件
 * 事件管理器也用于协调同一服务多次订阅相同的事件管道
 * 可以实现多路复用机制
 *
 * @author chenzhanshang
 * @date 2025/8/28 9:41
 * @describe :
 */
@Slf4j
public class DomainEventManager {
    private static final Map<RouteKey, Set<Consumer<DomainEvent<?>>>> remoteEventHandler = new ConcurrentHashMap<>();

    private static final Map<String, Set<Consumer<DomainEvent<?>>>> localEventHandler = new ConcurrentHashMap<>();

    /**
     * 远程事件发布支持
     */
    private static DomainEventRemoteClient remoteDomainEventClient;

    /**
     * 注册远程事件客户端
     *
     * @param remoteClient
     */
    public static void registerRemoteEventClient(DomainEventRemoteClient remoteClient) {
        synchronized (DomainEventManager.class) {
            Assert.notNull(remoteClient, () -> FrameworkException.of("【领域事件】：注册远程事件客户端, 注册对象不能为空"));
            Assert.isNull(remoteDomainEventClient, () -> FrameworkException.of("【领域事件】：重复注册的远程事件服务端"));
            remoteDomainEventClient = remoteClient;
            remoteDomainEventClient.setOnEventListener(DomainEventManager::onRemoteEvent);
            // 如果注册比监听顺序后面，则需要重新订阅
            if (CollectionUtils.isNotEmpty(remoteEventHandler)) {
                for (Map.Entry<RouteKey, Set<Consumer<DomainEvent<?>>>> routeKeySetEntry : remoteEventHandler.entrySet()) {
                    RouteKey routeKey = routeKeySetEntry.getKey();
                    remoteDomainEventClient.subscriber(routeKey.getSource(), routeKey.getEventType(), domainEvent -> routeKeySetEntry.getValue());
                }
            }
        }
    }

    /**
     * 发布事件
     *
     * @param eventType 事件类型
     * @param eventMode 事件模式
     * @param data      数据体
     */
    public static void publish(String eventType, EventMode eventMode, Object data) {
        publish(DomainEventFactory.create(eventType, data), eventMode);
    }

    /**
     * 发布事件
     *
     * @param domainEvent 领域事件对象
     * @param eventMode   事件模式
     */
    public static boolean publish(DomainEvent<Object> domainEvent, EventMode eventMode) {
        Assert.notNull(eventMode, () -> BusinessException.of("【领域事件】：事件模式不能为空"));
        switch (eventMode) {
            case LOCAL:
                // 本地事件直接触发
                onLocalEvent(domainEvent);
                return true;
            case REMOTE:
                Assert.notNull(remoteDomainEventClient, () -> FrameworkException.of("【领域事件】：当前服务未注册领域事件发布客户端"));
                return remoteDomainEventClient.publish(domainEvent);
            case ALL:
                Assert.notNull(remoteDomainEventClient, () -> FrameworkException.of("【领域事件】：当前服务未注册领域事件发布客户端"));
                if (!remoteDomainEventClient.publish(domainEvent)) {
                    return false;
                }
                onLocalEvent(domainEvent);
                return true;
            default:
                throw BusinessException.of("【领域事件】：暂不支持的事件模式: " + eventMode);
        }
    }

    /**
     * 注册远程事件观察者
     *
     * @param source   订阅产生事件的服务名
     * @param type     类型
     * @param consumer 观察者处理逻辑
     */
    public static void subscriberRemote(String source, String type, Consumer<DomainEvent<?>> consumer) {
        synchronized (DomainEventManager.class) {
            RouteKey key = RouteKey.build(source, type);
            Set<Consumer<DomainEvent<?>>> consumers = remoteEventHandler.get(key);
            if (CollectionUtils.isEmpty(consumers)) { // 如果当前服务未监听过该服务+事件类型，需要多绑定一个queue
                if (null != remoteDomainEventClient) {
                    remoteDomainEventClient.subscriber(source, type, consumer);
                }
                remoteEventHandler.put(key, new HashSet<>());
            }
            remoteEventHandler.get(key).add(consumer);
        }
    }

    /**
     * 注册本地事件观察者
     *
     * @param type     类型
     * @param consumer 观察者处理逻辑
     */
    public static void subscriberLocal(String type, Consumer<DomainEvent<?>> consumer) {
        Set<Consumer<DomainEvent<?>>> consumers = localEventHandler.get(type);
        if (CollectionUtils.isEmpty(consumers)) { // 如果当前服务未监听过该服务+事件类型，需要多绑定一个queue
            localEventHandler.put(type, new HashSet<>());
        }
        localEventHandler.get(type).add(consumer);
    }

    /**
     * 当远程领域事件触发时
     *
     * @param domainEvent 事件内容
     */
    public static void onRemoteEvent(DomainEvent<?> domainEvent) {
        Set<Consumer<DomainEvent<?>>> consumers = remoteEventHandler.get(RouteKey.build(domainEvent.getSource(), domainEvent.getEventType()));
        if (CollectionUtils.isEmpty(consumers)) {
            log.error("【领域事件】：接收微服务远程事件：{}， 消费者为空", domainEvent.getEventType());
            return;
        }
        for (Consumer<DomainEvent<?>> consumer : consumers) {
            consumer.accept(domainEvent);
        }
    }

    /**
     * 当本地领域事件触发时
     */
    public static void onLocalEvent(DomainEvent<Object> domainEvent) {
        Set<Consumer<DomainEvent<?>>> consumers = localEventHandler.get(domainEvent.getEventType());
        if (CollectionUtils.isEmpty(consumers)) {
            log.error("【领域事件】：接收微服务本地事件：{}， 消费者为空", domainEvent.getEventType());
            return;
        }
        for (Consumer<DomainEvent<?>> consumer : consumers) {
            consumer.accept(domainEvent);
        }
    }

    /**
     * 注册本地监听
     *
     * @param eventType 事件类型
     * @param consumer  处理方法
     */
    public static void localEventSubscriber(String eventType, Consumer<DomainEvent<?>> consumer) {
        synchronized (DomainEventManager.class) {
            log.info("【领域事件】：注册本地事件监听: {}", eventType);
            if (!localEventHandler.containsKey(eventType)) {
                localEventHandler.put(eventType, new HashSet<>());
            }
            localEventHandler.get(eventType).add(consumer);
        }
    }

    /**
     * 注册远程时间监听
     *
     * @param source    时间产生源
     * @param eventType 事件类型
     * @param consumer  处理方法
     */
    public static void remoteEventSubscriber(String source, String eventType, Consumer<DomainEvent<?>> consumer) {
        synchronized (DomainEventManager.class) {
            log.info("【领域事件】：注册远程事件监听: {}, {}", source, eventType);
            Assert.notNull(remoteDomainEventClient, () -> FrameworkException.of("【领域事件】：当前服务未注册领域事件发布客户端"));
            RouteKey routeKey = RouteKey.build(source, eventType);
            if (!remoteEventHandler.containsKey(routeKey)) {
                // 首次订阅，需要创建远程工具绑定关系
                remoteDomainEventClient.subscriber(source, eventType, consumer);
                remoteEventHandler.put(routeKey, new HashSet<>());
            }
            remoteEventHandler.get(routeKey).add(consumer);
        }
    }

    /**
     * 由事件源+事件类型确认唯一的一个key
     */
    @Data
    private static final class RouteKey {

        private static RouteKey build(String source, String type, boolean exclusive) {
            return new RouteKey(source, type, exclusive);
        }

        private static RouteKey build(String source, String type) {
            return new RouteKey(source, type, true);
        }

        /**
         * 事件源
         */
        private final String source;
        /**
         * 事件类型
         */
        private final String eventType;
        /**
         * 是否服务独立queue
         */
        private final boolean exclusive;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RouteKey routeKey = (RouteKey) o;
            return source.equals(routeKey.source) && eventType.equals(routeKey.eventType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, eventType);
        }
    }
}
