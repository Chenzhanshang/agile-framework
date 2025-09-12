package com.agile.framework.util;

import com.agile.framework.exception.BusinessException;

import java.util.function.Supplier;

/**
 * 断言工具类
 * @author chenzhanshang
 * @date 2025/8/29 11:18
 * @describe :
 */
public class Assert {
    /**
     * 断言是否为真，如果为 {@code false} 抛出给定的异常<br>
     *
     * @param <X>        异常类型
     * @param expression 布尔值
     * @param supplier   指定断言不通过时抛出的异常
     * @throws X if expression is {@code false}
     */
    public static <X extends Throwable> void isTrue(boolean expression, Supplier<? extends X> supplier) throws X {
        if(!expression) {
            throw supplier.get();
        }
    }

    /**
     * 断言是否为假，如果为 {@code true} 抛出给定的异常<br>
     *
     * @param <X>        异常类型
     * @param expression 布尔值
     * @param supplier   指定断言不通过时抛出的异常
     * @throws X 条件不满足时抛出异常
     */
    public static <X extends Throwable> void isFalse(boolean expression, Supplier<? extends X> supplier) throws X {
        if(expression) {
            throw supplier.get();
        }
    }

    /**
     * 断言不为空
     * @param <X>        异常类型
     * @param object     判断对象
     * @param supplier   指定断言不通过时抛出的异常
     * @throws X 条件不满足时抛出异常
     */
    public static <X extends Throwable> void notNull(Object object, Supplier<? extends X> supplier) throws X {
        if(object == null) {
            throw supplier.get();
        }
    }
    /**
     * 断言为空
     * @param <X>        异常类型
     * @param object     判断对象
     * @param supplier   指定断言不通过时抛出的异常
     * @throws X 条件不满足时抛出异常
     */
    public static <X extends Throwable> void isNull(Object object, Supplier<? extends X> supplier) throws X {
        if(object != null) {
            throw supplier.get();
        }
    }
}
