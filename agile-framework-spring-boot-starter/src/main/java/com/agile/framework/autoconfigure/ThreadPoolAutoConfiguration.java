package com.agile.framework.autoconfigure;

import com.agile.framework.config.ThreadPoolProperties;
import com.agile.framework.context.AppContext;
import com.agile.framework.context.ThreadPoolClosedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池自动配置类
 *
 * @author chenzhanshang
 */
@Slf4j
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(TaskExecutionAutoConfiguration.class)
@EnableConfigurationProperties(ThreadPoolProperties.class)
@ConditionalOnProperty(name = "agile.framework.thread-pool.enabled", havingValue = "true")
public class ThreadPoolAutoConfiguration {

    private final ThreadPoolProperties threadPoolProperties;

    @Bean
    ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
        // 告诉线程池，在销毁之前执行 shutdown 方法
        executor.setWaitForTasksToCompleteOnShutdown(threadPoolProperties.isWaitForTasksToCompleteOnShutdown());
        // shutdown/shutdownNow 之后等待 3 秒
        executor.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationSeconds());
        return executor;
    }

    @Bean
    ThreadPoolClosedHandler threadPoolClosedHandler() {
        ThreadPoolTaskExecutor poolTaskExecutor = AppContext.getBean(ThreadPoolTaskExecutor.class);
        return new ThreadPoolClosedHandler(poolTaskExecutor);
    }
}
