package com.agile.framework.config;

import com.agile.framework.constant.RequestContextConstants;
import com.agile.framework.context.AppContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * fei转发请求头配置
 * @author: chenzhanshang
 * @date: 2025/03/29 16:37
 */
@RequiredArgsConstructor
public class FeignRequestConfig implements RequestInterceptor {

    private final FeignProperties feignProperties;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = AppContext.getRequest();
        if (Objects.nonNull(request)) {
            for (String key : feignProperties.getKeys()) {
                requestTemplate.header(key, request.getHeader(key));
            }
            // 标识feign调用，日志存储
            requestTemplate.header(RequestContextConstants.REQUEST_ORIGIN, "1");
        }
    }

}
