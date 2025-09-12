package com.agile.framework.exception;

import com.agile.framework.bean.ApiResponse;
import com.agile.framework.bean.ApiStatus;
import com.agile.framework.log.RequestLogHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 * @author chenzhanshang
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 默认异常处理器
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResponse<?> defaultExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("defaultExceptionHandler: {}", e.getMessage());
        log.error(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.fail();
    }

    /**
     * 默认运行时异常处理器
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ApiResponse<?> defaultRuntimeExceptionHandler(HttpServletRequest request, RuntimeException e) {
        log.error("defaultRuntimeExceptionHandler: {}", e.getMessage());
        log.error(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.DEFAULT_RUNTIME);
    }

    /**
     * 忽略参数异常处理器
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<?> parameterMissingExceptionHandler(MissingServletRequestParameterException e) {
        log.info("parameterMissingExceptionHandler: {}", e.getMessage());
        log.info(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.NOT_VALID_PARAM);
    }

    /**
     * 参数转换异常处理器
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ApiResponse<?> parameterConversionExceptionHandler(HttpMessageConversionException e) {
        log.info("parameterConversionExceptionHandler: {}", e.getMessage());
        log.info(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.PARAMETER_BODY_MISSING_OR_CONVERT_ERROR);
    }

    /**
     * 参数效验异常处理器
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResponse<?> defaultMethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.info("defaultMethodArgumentNotValidExceptionHandler: {}", e.getMessage());
        log.info(e.getMessage(), e);
        StringBuilder message = new StringBuilder("参数校验不通过：");
        if (e.getBindingResult() != null && e.getBindingResult().getAllErrors() != null) {
            for (ObjectError objectError : e.getBindingResult().getAllErrors()) {
                message.append("[").append(objectError.getDefaultMessage()).append("] ");
            }
        }
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.METHOD_ARGUMENT_NOT_VALID.getCode(), message.toString());
    }

    /**
     * 参数效验异常处理器
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ApiResponse<?> defaultConstraintViolationExceptionHandler(ConstraintViolationException e) {
        log.info("defaultConstraintViolationExceptionHandler: {}", e.getMessage());
        log.info(e.getMessage(), e);
        StringBuilder message = new StringBuilder("参数校验不通过：");
        if (e.getConstraintViolations() != null) {
            for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                message.append("[").append(constraintViolation.getMessage()).append("] ");
            }
        }
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.METHOD_ARGUMENT_NOT_VALID.getCode(), message.toString());
    }

    /**
     * 请求方式不支持异常处理器
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ApiResponse<?> defaultRequestMethodRuntimeExceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        log.info("defaultRequestMethodRuntimeExceptionHandler: {}", e.getMessage());
        log.info(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 自定义异常处理器
     */
    @ExceptionHandler(value = ApiException.class)
    public ApiResponse<?> ApiExceptionHandler(HttpServletRequest request, ApiException e) {
        log.info("ApiExceptionHandler: {}", e.getMessage());
        log.info(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.API_FAIL);
    }

    /**
     * framework异常处理器
     */
    @ExceptionHandler(value = BusinessException.class)
    public ApiResponse<?> defaultPaddyExceptionHandler(HttpServletRequest req, BusinessException e) {
        log.info("defaultPaddyExceptionHandler: {}", e.getMessage());
        log.info(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(e.getCode(), e.getMessage());
    }

    /**
     * 404异常处理器
     * 由于开启了静态资源访问，此拦截目前无法生效，当访问不存在的请求ur时会被当成访问静态资源，并不会抛出异常
     */
    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<?> handlerNoFoundException(NoHandlerFoundException e) {
        log.info("handlerNoFoundException: {}", e.getMessage());
        log.info(e.getMessage(), e);
        // 需要的话日志记录错误信息
        RequestLogHolder.setErrorData(e.getMessage());
        return ApiResponse.create(ApiStatus.NOT_FOUND);
    }
}
