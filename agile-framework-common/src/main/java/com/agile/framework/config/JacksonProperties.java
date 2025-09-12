package com.agile.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * jackson 配置类
 *
 * @author chenzhanshang
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "agile.framework.jackson")
public class JacksonProperties {

    /**
     * 是否开启框架的 jackson 定制化配置
     */
    private boolean enabled;

}
