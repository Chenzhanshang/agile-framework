package com.agile.framework.autoconfigure;

import com.agile.framework.config.CorsProperties;
import com.agile.framework.config.WebMvcConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 跨域相关自动配置类
 *
 * @author chenzhanshang
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@ConditionalOnProperty(name = "agile.framework.cors.enabled", havingValue = "true")
@EnableConfigurationProperties(CorsProperties.class)
public class CorsAutoConfiguration {

    private final CorsProperties corsProperties;

    @Bean
    WebMvcConfig webMvcConfig() {
        return new WebMvcConfig(corsProperties);
    }

}
