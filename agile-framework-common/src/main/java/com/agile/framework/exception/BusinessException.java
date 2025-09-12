package com.agile.framework.exception;

import com.agile.framework.bean.ApiStatus;

/**
 * 业务异常
 *
 * @author chenzhanshang
 */
public class BusinessException extends ApiException {

    private static final long serialVersionUID = 1L;

    protected BusinessException(int code, String message) {
        super(code, message);
    }

    protected BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 根据异常消息实例化 BusinessException
     *
     * @param message 异常消息内容
     * @return BusinessException
     */
    public static BusinessException of(String message) {
        return of(ApiStatus.BUSINESS.getCode(), message);
    }

    /**
     * 根据 Status 实例化 BusinessException
     *
     * @param apiStatus 异常状态 {@link ApiStatus}
     * @return BusinessException
     */
    public static BusinessException of(ApiStatus apiStatus) {
        return of(apiStatus.getCode(), apiStatus.getMessage());
    }

    /**
     * 根据异常状态码和异常消息内容实例化 BusinessException
     *
     * @param code    异常状态码
     * @param message 异常消息内容
     * @return BusinessException
     */
    public static BusinessException of(int code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 根据异常消息和 Throwable 实例化 BusinessException
     *
     * @param message 异常消息内容
     * @param cause   throwable
     * @return BusinessException
     */
    public static BusinessException of(String message, Throwable cause) {
        return new BusinessException(message, cause);
    }

}
