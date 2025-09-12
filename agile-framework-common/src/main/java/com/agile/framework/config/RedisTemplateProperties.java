package com.agile.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis 配置类
 *
 * @author chenzhanshang
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "agile.framework.redis-template")
public class RedisTemplateProperties {

    /**
     * 是否开启框架提供的 RedisTemplate
     */
    private boolean enabled;

}
