package com.agile.event.config.factory;

import com.agile.event.config.dto.DefaultDomainEvent;
import com.agile.event.config.dto.DomainEvent;
import com.agile.framework.exception.FrameworkException;
import com.agile.framework.util.Assert;
import com.agile.framework.util.RandomUtils;

import java.util.function.Supplier;

/**
 * 领域事件对象单例工厂
 *
 * @author chenzhanshang
 * @date 2025/8/29 15:03
 * @describe :
 */
public class DomainEventFactory {
    /**
     * 工厂单例
     */
    private static volatile DomainEventFactory singleInstance;

    /**
     * 服务名称
     */
    private String serverName;

    private DomainEventFactory(String serverName) {
        this.serverName = serverName;
    }

    /**
     * 初始化
     *
     * @param serverName 服务名
     */
    public static void init(String serverName) {
        if (singleInstance == null) {
            synchronized (DomainEventFactory.class) {
                if (singleInstance == null) {
                    singleInstance = new DomainEventFactory(serverName);
                }
            }
        }
    }

    /**
     * 创建领域事件对象
     *
     * @param eventType 事件类型
     * @param data      事件数据体
     * @param <T>
     * @return
     */
    public static <T> DomainEvent<T> create(String eventType, T data) {
        Assert.notNull(singleInstance, (Supplier<RuntimeException>) () -> FrameworkException.of("领域事件工厂未被初始化"));
        return new DefaultDomainEvent<>(
                RandomUtils.create32UUID(),
                eventType,
                singleInstance.serverName,
                System.currentTimeMillis(),
                data
        );
    }

}
