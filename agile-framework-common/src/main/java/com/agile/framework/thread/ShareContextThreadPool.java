package com.agile.framework.thread;

import com.agile.framework.context.ContextManager;
import com.agile.framework.exception.BusinessException;
import com.agile.framework.util.CollectionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 共享上下文线程池
 * @author: chenzhanshang
 * @desc:
 **/
@Slf4j
public class ShareContextThreadPool {
    /**
     * 真实线程池对象
     */
    @Getter
    private static ExecutorService executor;
    /**
     * 构建线程名称
     */
    private static final AtomicInteger idx = new AtomicInteger();

    private ShareContextThreadPool() {
    }

    static {
        init();
    }

    /**
     * 初始化全局线程池
     */
    private synchronized static void init() {
        if (null != executor) {
            executor.shutdownNow();
        }
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        executor = new ShareThreadPool(
                Math.max(availableProcessors + 1, 8),
                100,
                1,
                TimeUnit.MINUTES,
                new SynchronousQueue<>(),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("paddy-global-thread-" + idx.incrementAndGet());
                    thread.setDaemon(false);
                    thread.setUncaughtExceptionHandler((t, e) ->
                            log.error("{} occur exception: {}", t.getName(), e.getMessage(), e)
                    );
                    return thread;
                });
    }

    public static class ShareThreadPool extends ThreadPoolExecutor {

        public ShareThreadPool(int corePoolSize,
                               int maximumPoolSize,
                               long keepAliveTime, TimeUnit unit,
                               BlockingQueue<Runnable> workQueue,
                               ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory,
                    (r, executor) -> {
                        log.error("全局共享上下文线程池已满,active count={}", executor.getActiveCount());
                        throw BusinessException.of("当前服务忙，请稍后重试");
                    });
        }

        @Override
        public void execute(Runnable command) {
            super.execute(wrapper(command));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(wrapper(task));
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            return super.submit(wrapper(task));
        }

        /**
         * 包装携带上下文
         * @param callable
         * @return
         * @param <T>
         */
        private <T> Callable<T> wrapper(Callable<T> callable) {
            final AtomicReference<Map<String, Object>> context =
                    new AtomicReference<>(new HashMap<>(ContextManager.getContext()));
            return () -> {
                if (CollectionUtils.isNotEmpty(context.get())) {
                    context.get().forEach(ContextManager::addAttribute);
                }
                try {
                    return callable.call();
                } finally {
                    ContextManager.removeContext();
                }
            };
        }

        /**
         * 包装携带上下文
         * @param runnable
         * @return
         */
        private Runnable wrapper(Runnable runnable) {
            final AtomicReference<Map<String, Object>> context =
                    new AtomicReference<>(new HashMap<>(ContextManager.getContext()));
            return () -> {
                if (CollectionUtils.isNotEmpty(context.get())) {
                    context.get().forEach(ContextManager::addAttribute);
                }
                try {
                    runnable.run();
                } finally {
                    ContextManager.removeContext();
                }
            };
        }
    }


    /**
     * 关闭公共线程池
     *
     * @param isNow 是否立即关闭而不等待正在执行的线程
     */
    public synchronized static void shutdown(boolean isNow) {
        if (null != executor) {
            if (isNow) {
                executor.shutdownNow();
            } else {
                executor.shutdown();
            }
        }
    }

    /**
     * 直接在公共线程池中执行线程
     *
     * @param runnable 可运行对象
     */
    public static void execute(Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            throw BusinessException.of("Exception when running task!");
        }
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param <T> 执行的Task
     * @param task {@link Callable}
     * @return Future
     */
    public static <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    /**
     * 执行有返回值的异步方法<br>
     * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
     *
     * @param runnable 可运行对象
     * @return {@link Future}
     * @since 3.0.5
     */
    public static Future<?> submit(Runnable runnable) {
        return executor.submit(runnable);
    }
}
