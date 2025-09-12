package com.agile.framework.log;

import com.agile.framework.log.dto.RequestLogDTO;


/**
 * 请求日志信息
 * @author chenzhanshang
 */
public class RequestLogHolder {
    private static ThreadLocal<RequestLogDTO> CONTEXT = new ThreadLocal<>();

    public static void set(RequestLogDTO requestLogDTO) {
        CONTEXT.set(requestLogDTO);
    }

    public static RequestLogDTO get() {
        return CONTEXT.get();
    }

    public static void remove() {
        CONTEXT.remove();
    }

    public static void setErrorData(String errMsg) {
        RequestLogDTO requestLogDTO = CONTEXT.get();
        if(requestLogDTO != null) {
            requestLogDTO.setResult(1);
            requestLogDTO.setErrorMsg(errMsg);
        }
    }
}
