package com.agile.framework.autoconfigure;

import com.agile.framework.context.AppContext;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * 框架定义的全局 Bean 自动配置
 *
 * @author HuangXuLin
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import({com.agile.framework.exception.GlobalExceptionHandler.class,
        com.agile.framework.util.SpringUtil.class,
        com.agile.framework.jackson.JacksonBuilderCustomizerConfig.class
})
public class GlobalBeanAutoConfiguration {
    @Bean
    AppContext appContext() {
        return new AppContext();
    }

}
