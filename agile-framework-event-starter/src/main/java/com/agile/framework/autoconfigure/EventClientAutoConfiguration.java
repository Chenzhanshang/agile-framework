package com.agile.framework.autoconfigure;

import com.agile.event.config.EventClientProperties;
import com.agile.event.config.factory.DomainEventFactory;
import com.agile.event.config.support.EventMqService;
import com.agile.event.config.support.impl.RabbitMqEventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: chenzhanshang
 * @date 2025/09/07 15:21
 * @desc:
 **/
@Slf4j
@Configuration
@EnableConfigurationProperties(EventClientProperties.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "agile.framework.event.enabled", havingValue = "true")


public class EventClientAutoConfiguration {

    @Autowired
    private EventClientProperties properties;

    @Bean
    public EventMqService eventClient() {
        properties.check();
        DomainEventFactory.init(properties.getServiceName());
        return new RabbitMqEventServiceImpl();
    }
}
