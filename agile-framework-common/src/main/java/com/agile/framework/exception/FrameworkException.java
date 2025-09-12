package com.agile.framework.exception;

import com.agile.framework.bean.ApiStatus;

/**
 * 框架异常，常常发生在启动/调用framework方法时
 * 主要用于区分异常类型，快速定位
 * @author chenzhanshang
 */
public class FrameworkException extends ApiException {

    private static final long serialVersionUID = 1L;

    protected FrameworkException(int code, String message) {
        super(code, message);
    }

    protected FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * 根据异常状态码和异常消息内容实例化 FrameworkException
     *
     * @param code    异常状态码
     * @param message 异常消息内容
     * @return FrameworkException
     */
    public static FrameworkException of(int code, String message) {
        return new FrameworkException(code, message);
    }

    /**
     * 根据异常消息实例化 FrameworkException
     *
     * @param message 异常消息内容
     * @return FrameworkException
     */
    public static FrameworkException of(String message) {
        return of(ApiStatus.FRAMEWORK_ERROR.getCode(), message);
    }

    /**
     * 根据 Status 实例化 FrameworkException
     *
     * @param apiStatus 异常状态 {@link ApiStatus}
     * @return FrameworkException
     */
    public static FrameworkException of(ApiStatus apiStatus) {
        return of(apiStatus.getCode(), apiStatus.getMessage());
    }

    /**
     * 根据异常消息和 Throwable 实例化 FrameworkException
     *
     * @param message 异常消息内容
     * @param cause   throwable
     * @return FrameworkException
     */
    public static FrameworkException of(String message, Throwable cause) {
        return new FrameworkException(message, cause);
    }

}
