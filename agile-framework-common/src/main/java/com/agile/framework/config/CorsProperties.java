package com.agile.framework.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * 跨域相关配置，参数详细说明：https://www.jianshu.com/p/840b4f83c3b5
 *
 * @author chenzhanshang
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "agile.framework.cors")
public class CorsProperties {

    /**
     * 是否开启框架的跨域配置
     */
    private boolean enabled;

    /**
     * 允许跨域的路径
     */
    private String pathPattern = "/**";

    /**
     * 允许跨域的站点，*号表示全部
     */
    private String allowedOrigin = CorsConfiguration.ALL;

    /**
     * 允许跨域的 HTTP 请求方法
     */
    private String[] allowedMethods = new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    /**
     * 允许的请求头
     */
    private String[] allowedHeaders = new String[]{CorsConfiguration.ALL};

    /**
     * 允许暴露给前端的响应头
     */
    private String[] exposedHeaders = new String[0];

    /**
     * 浏览器是否应该包含与请求相关的任何 Cookie
     */
    private boolean allowCredentials;

    /**
     * 预响应缓存持续时间的最大时间，默认 1800 秒：30分钟
     */
    private long maxAge = 1800;

}
