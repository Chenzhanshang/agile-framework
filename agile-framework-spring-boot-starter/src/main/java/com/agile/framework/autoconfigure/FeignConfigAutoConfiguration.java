package com.agile.framework.autoconfigure;

import com.agile.framework.config.FeignProperties;
import com.agile.framework.config.FeignRequestConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign配置自动装配
 * @author: chenzhanshang
 * @date: 2025/09/02 16:37
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(FeignProperties.class)
public class FeignConfigAutoConfiguration {

    private final FeignProperties feignProperties;

    @Bean
    @ConditionalOnProperty(name = "agile.framework.feign.auto-transmit", havingValue = "true")
    FeignRequestConfig feignRequestConfig() {
        return new FeignRequestConfig(feignProperties);
    }

}
