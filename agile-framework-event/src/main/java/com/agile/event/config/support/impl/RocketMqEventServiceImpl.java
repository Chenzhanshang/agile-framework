package com.agile.event.config.support.impl;

import com.agile.event.config.client.callback.DomainEventListener;
import com.agile.event.config.dto.DefaultDomainEvent;
import com.agile.event.config.dto.DomainEvent;
import com.agile.event.config.manager.DomainEventManager;
import com.agile.event.config.support.BaseEventMqService;
import com.agile.framework.exception.FrameworkException;
import com.agile.framework.util.CollectionUtils;
import com.agile.framework.util.JsonUtil;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
@Configuration
@ConditionalOnClass(DefaultMQProducer.class)
@ConditionalOnProperty(name = "agile.framework.event.mq", havingValue = "rocketmq")
public class RocketMqEventServiceImpl extends BaseEventMqService {

    @Value("${agile.framework.event.serviceName}")
    private String serviceName;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    private DefaultMQProducer producer;

    @PostConstruct
    public void init() {
        // 初始化 Producer
        producer = new DefaultMQProducer(serviceName + "-producer");
        producer.setNamesrvAddr(nameServer);
        try {
            producer.start();
            DomainEventManager.registerRemoteEventClient(this);
            log.info("【领域事件】: RocketMQ Producer 启动成功, group={}", serviceName);
        } catch (MQClientException e) {
            throw new RuntimeException("RocketMQ Producer 启动失败", e);
        }
    }

    @Override
    public boolean send(String topic, String tag, Object data) {
        try {
            String json = formatTransmissionObject(data);
            Message msg = new Message(topic, tag, json.getBytes(StandardCharsets.UTF_8));
            SendResult sendResult = producer.send(msg);
            log.info("【领域事件】: RocketMQ 发送成功 topic={}, tag={}, result={}", topic, tag, sendResult);
            return true;
        } catch (Exception e) {
            log.error("【领域事件】: RocketMQ 发送失败 topic={}, tag={}", topic, tag, e);
            throw FrameworkException.of("RocketMQ事件推送失败");
        }
    }

    @Override
    public boolean publish(DomainEvent<?> domainEvent) throws FrameworkException {
        return send(domainEvent.getSource(), domainEvent.getEventType(), domainEvent);
    }

    /**
     * todo: 目前随机consumerGroup的方式，只支持从最新 offset 开始消费，是否需要加入参数，支持消费历史消息？？？
     * @param source    事件源
     * @param eventType 事件类型
     * @param consumer  数据消费
     */
    @Override
    public void subscriber(String source, String eventType, Consumer<DomainEvent<?>> consumer) {
        String consumerGroup = serviceName + "-consumer-" + source + "-" + eventType + "-" + UUID.randomUUID();
        DefaultMQPushConsumer pushConsumer = new DefaultMQPushConsumer(consumerGroup);
        pushConsumer.setNamesrvAddr(nameServer);

        try {
            pushConsumer.subscribe(source, eventType);
            pushConsumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
                for (MessageExt msg : msgs) {
                    try {
                        String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                        if (CollectionUtils.isEmpty(domainEventListeners)) {
                            log.warn("【领域事件】: 观察者集合为空，消息事件：{}", body);
                            continue;
                        }
                        // 解析消息
                        DomainEvent<?> domainEvent = getObject(body);
                        for (DomainEventListener listener : domainEventListeners) {
                            try {
                                listener.onEvent(domainEvent);
                            } catch (Exception e) {
                                log.error("【领域事件】: 消费事件失败 listener={}, 事件={}", listener, domainEvent, e);
                            }
                        }
                    } catch (Exception e) {
                        log.error("【领域事件】: 消息解析失败", e);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            pushConsumer.start();
            log.info("【领域事件】: RocketMQ Consumer 启动成功 group={}, topic={}, tag={}",
                    consumerGroup, source, eventType);
        } catch (MQClientException e) {
            throw FrameworkException.of("【领域事件】: RocketMQ订阅领域事件失败", e);
        }
    }

    /**
     * 解析传输对象
     * 目前暂时只支持json传输
     * @param body
     * @return
     */
    private DomainEvent<?> getObject(String body) {
       return JsonUtil.decode(body, DefaultDomainEvent.class);
    }
}
