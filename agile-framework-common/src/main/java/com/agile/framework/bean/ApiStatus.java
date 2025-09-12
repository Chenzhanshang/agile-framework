package com.agile.framework.bean;

import lombok.Getter;

/**
 * 接口响应状态枚举
 *
 * @author chenzhanshang
 */
@Getter
public enum ApiStatus {

    /**
     * Mapping Http Response Status 0 -> 200
     */
    SUCCESS(0, "请求成功", "Success"),
    NOT_VALID_PARAM(400, "提交的参数有误，数据加载失败", "The submitted parameters are incorrect and the data loading failed"),
    NOT_LOGIN(401, "用户尚未登录", "The user has not logged in yet."),
    FORBIDDEN(403, "权限不足，拒绝操作", "Insufficient permissions, operation is refused"),
    NOT_FOUND(404, "请求的资源不存在或者已经被删除", "The requested resource does not exist or has been deleted"),
    METHOD_NOT_ALLOWED(405, "HTTP请求方法不支持", "The HTTP request method is not supported"),
    METHOD_ARGUMENT_NOT_VALID(406, "参数校验不通过", "The parameter verification failed"),
    PARAMETER_BODY_MISSING_OR_CONVERT_ERROR(407, "缺少请求体或参数格式错误", "The request body is missing or the parameter format is incorrect"),
    PARAMETER_BODY_MISSING(415, "缺少请求体", "The request body is missing"),
    API_FAIL(416, "业务异常", "Business anomaly"),
    INTERNAL_SERVER_ERROR(500, "发生未知错误，请求失败", "An unknown error occurred and the request failed"),
    DEFAULT_RUNTIME(501, "发生未知错误，请求失败", "An unknown error occurred and the request failed"),
    BUSINESS(505, "业务异常", "Business anomaly"),
    FRAMEWORK_ERROR(601, "框架异常", "framework execute error"),
    PACKAGE_FORBIDDEN(4003, "套餐资源权限不足，拒绝操作", "Insufficient permissions, operation is refused"),
    ;

    private final int code;

    private final String message;

    private final String enMessage;

    ApiStatus(int code, String message, String enMessage) {
        this.code = code;
        this.message = message;
        this.enMessage = enMessage;
    }

    /**
     * 根据状态码获取枚举值
     */
    public static ApiStatus getByCode(int code) {
        for (ApiStatus status : ApiStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new RuntimeException("未知的枚举值 ApiStatus: code=" + code);
    }

}
