package com.agile.framework.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池关闭处理
 *
 * @author chenzhanshang
 */
@Slf4j
@RequiredArgsConstructor
public class ThreadPoolClosedHandler implements ApplicationListener<ContextClosedEvent> {

    private final ThreadPoolTaskExecutor executor;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 关闭线程池
        executor.destroy();
    }
}
