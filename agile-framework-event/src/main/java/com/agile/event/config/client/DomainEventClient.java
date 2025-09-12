package com.agile.event.config.client;

import com.agile.event.config.dto.DomainEvent;
import com.agile.framework.exception.FrameworkException;


/**
 * 领域消息事件客户端
 * 事件由事件源 + 事件类型 决定领域事件的唯一性以及路由追踪
 * 例如： 用户服务的用户注册完成事件，｜ source：user-svr ｜ type： register-success  ｜
 * 事件订阅客户端则需要订阅的是 服务 + 事件类型 去确定消息的匹配消费
 *
 * @author: chenzhanshang
 * @date 2025/08/28 11:56
 * @desc:
 **/
public interface DomainEventClient {

    /**
     * 发布领域事件
     *
     * @param domainEvent 领域事件
     * @return 发送结果 true代表成功 false代表失败
     * @throws FrameworkException 消息传输异常时抛出框架一场
     */
    boolean publish(DomainEvent<?> domainEvent) throws FrameworkException;

}
