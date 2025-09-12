package com.agile.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * feign配置
 * @author: chenzhanshang
 * @date: 2025/03/29 16:37
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "agile.framework.feign")
public class FeignProperties {

    /**
     * Feign调用是否自动转发请求头信息
     */
    private boolean autoTransmit;
    /**
     * 转发的配置
     */
    private List<String> keys = Arrays.asList(

    );
}
