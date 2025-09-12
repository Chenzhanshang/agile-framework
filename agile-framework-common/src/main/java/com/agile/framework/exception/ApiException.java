package com.agile.framework.exception;

import com.agile.framework.bean.ApiStatus;
import lombok.Getter;

/**
 * 自定义 API 异常父类
 *
 * @author chenzhanshang
 */
@Getter
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常状态码
     */
    private final int code;

    /**
     * 异常消息内容
     */
    private final String message;

    protected ApiException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    protected ApiException(String message, Throwable cause) {
        super(message, cause);
        this.code = ApiStatus.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
    }

    /**
     * 根据异常消息实例化 PaddyException
     *
     * @param message 异常消息内容
     * @return PaddyException
     */
    public static ApiException of(String message) {
        return of(ApiStatus.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    /**
     * 根据异常状态码和异常消息内容实例化 PaddyException
     *
     * @param code    异常状态码
     * @param message 异常消息内容
     * @return PaddyException
     */
    public static ApiException of(int code, String message) {
        return new BusinessException(code, message);
    }

}
