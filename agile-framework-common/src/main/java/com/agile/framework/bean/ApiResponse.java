package com.agile.framework.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的 Response 返回对象
 *
 * @author chenzhanshang
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private T data;

    public static <T> ApiResponse<T> success() {
        return create(ApiStatus.SUCCESS);
    }

    public static <T> ApiResponse<T> successOfMessage(String message) {
        return create(ApiStatus.SUCCESS.getCode(), message);
    }

    public static <T> ApiResponse<T> successOfData(T data) {
        return new ApiResponse<>(ApiStatus.SUCCESS.getCode(), ApiStatus.SUCCESS.getMessage(), data);
    }

    public static ApiResponse<Map<String, Object>> successOfData(String key, Object value) {
        Map<String, Object> data = new HashMap<>(16);
        data.put(key, value);
        return new ApiResponse<>(ApiStatus.SUCCESS.getCode(), ApiStatus.SUCCESS.getMessage(), data);
    }

    @SuppressWarnings("unchecked")
    public ApiResponse<T> addDataItem(String key, Object value) {
        Map<String, Object> content = (Map<String, Object>) data;
        if (content == null) {
            content = new HashMap<>(16);
        }
        content.put(key, value);
        this.data = (T) content;
        return this;
    }

    public boolean ok() {
        return this.code == ApiStatus.SUCCESS.getCode();
    }

    public static <T> ApiResponse<T> fail() {
        return create(ApiStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ApiResponse<T> failOfMessage(String message) {
        return create(ApiStatus.INTERNAL_SERVER_ERROR.getCode(), message);
    }

    public static <T> ApiResponse<T> create(int code, String message) {
        return new ApiResponse<>(code, message);
    }

    public static <T> ApiResponse<T> create(ApiStatus baseApiStatus) {
        return create(baseApiStatus.getCode(), baseApiStatus.getMessage());
    }

    public static <T> ApiResponse<T> create(ApiStatus baseApiStatus, T data) {
        return new ApiResponse<>(baseApiStatus.getCode(), baseApiStatus.getMessage(), data);
    }

    private ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ApiResponse(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

}
