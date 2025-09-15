package com.agile.event.config.support.impl;

import com.agile.event.config.client.callback.DomainEventListener;
import com.agile.event.config.dto.DefaultDomainEvent;
import com.agile.event.config.dto.DomainEvent;
import com.agile.event.config.manager.DomainEventManager;
import com.agile.event.config.support.BaseEventMqService;
import com.agile.framework.exception.FrameworkException;
import com.agile.framework.util.CollectionUtils;
import com.agile.framework.util.JsonUtil;
import com.agile.framework.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author chenzhanshang
 * @date 2025/9/1 11:33
 * @describe :
 */
@Slf4j
@Configuration
@ConditionalOnClass({RabbitTemplate.class, Channel.class})
@ConditionalOnProperty(name = "agile.framework.event.mq", havingValue = "rabbitmq", matchIfMissing = true)
public class RabbitMqEventServiceImpl extends BaseEventMqService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${agile.framework.event.serviceName}")
    private String serviceName;

    @PostConstruct
    public void register() {
        DomainEventManager.registerRemoteEventClient(this);
    }

    @Override
    public boolean send(String exchange, String routingKey, Object data) {
        try {
            String transmissionData = formatTransmissionObject(data);
            log.info("发送消息：{}， 内容：{}", routingKey, transmissionData);
            rabbitTemplate.convertAndSend(
                    wrapperExchange(exchange, routingKey),
                    wrapperRoutingKey(routingKey),
                    transmissionData,
                    message -> {
//                        // 业务需求时，可将可抽取公共部分放置头上传输
//                        Map<String, Object> headers = message.getMessageProperties().getHeaders();
                        if(StringUtils.isJsonStr(transmissionData)) {
                            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                        }
                        return message;
                    }
            );
        } catch (AmqpException e) {
            log.error("【领域事件】: rabbitMQ事件推送失败：{}", e.getMessage(), e);
            throw FrameworkException.of("rabbitMQ事件推送失败");
        }
        return true;
    }

    @Override
    public boolean publish(DomainEvent<?> domainEvent) throws FrameworkException {
        return send(domainEvent.getSource(), domainEvent.getEventType(), domainEvent);
    }

    @Override
    public void subscriber(String source, String eventType, Consumer<DomainEvent<?>> consumer) {
        String queue = wrapperQueue(serviceName, source, eventType);
        String exchange = wrapperExchange(serviceName, eventType);
        try {
            ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
            Connection connection = connectionFactory.createConnection();
            Channel channel = connection.createChannel(false);
            // 定义队列
            channel.queueDeclare(queue, true, false, false, null);
            // 定义交换机
            channel.exchangeDeclare(exchange, "fanout", true);
            // 定义绑定关系
            channel.queueBind(queue, exchange, wrapperRoutingKey(eventType));
            // 定义消费者
            channel.basicConsume(queue, false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    try {
                        String data = new String(body, StandardCharsets.UTF_8);
                        if (CollectionUtils.isEmpty(domainEventListeners)) {
                            log.warn("【领域事件】: 观察者集合为空，消息事件：{}", data);
                            return;
                        }
                        // 解析消息内容
                        DomainEvent<?> domainEvent = getObject(properties, body);
                        for (DomainEventListener domainEventListener : domainEventListeners) {
                            // 前面的观察者处理失败不影响后续观察者处理
                            try {
                                domainEventListener.onEvent(domainEvent);
                            } catch (Exception e) {
                                log.error("【领域事件】: 消费事件失败，当前消费者:{} , 领域事件：{}", domainEventListener, domainEvent, e);
                            }
                        }
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (Exception e) {
                        log.error("【领域事件】: 服务消费异常:{}", e.getMessage(), e);
                    }
                }
            });
        } catch (IOException e) {
            log.error("【领域事件】: 订阅领域事件失败");
            throw FrameworkException.of("【领域事件】: 订阅领域事件失败");
        }
    }

    /**
     * 解析传输对象
     * 目前暂时只支持json传输
     * @param properties
     * @param body
     * @return
     */
    private DomainEvent<Object> getObject(AMQP.BasicProperties properties, byte[] body) {
        if (MessageProperties.CONTENT_TYPE_JSON.equals(properties.getContentType())) {
            return (DomainEvent) JsonUtil.decode(new String(body), DefaultDomainEvent.class);
        }
        throw FrameworkException.of("【领域事件】: rabbitMQ事件处理失败，暂不支持的传输数据格式");
    }
}
