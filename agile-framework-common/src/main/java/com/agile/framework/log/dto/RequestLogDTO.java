package com.agile.framework.log.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 请求日志信息
 * @author: chenzhanshang
 */
@Getter
@Setter
@NoArgsConstructor
public class RequestLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 应用id
     */
    private Long appid;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 服务端ip
     */
    private String serverIp;

    /**
     * 服务名
     */
    private String serverName;

    /**
     * 接口名 todo:接口名和事件类型有什么区别
     */
    private String interfaceName;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 时间
     */
    private Date executeTime;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件描述
     */
    private String eventDetail;

    /**
     * 客户端ip
     */
    private String clientIp;

    /**
     * 客户端设备类型
     */
    private String facilityType;

    /**
     * 客户端设备系统
     */
    private String facilityOs;

    /**
     * 客户端浏览器
     */
    private String browser;

    /**
     * 调用来源，0-网关，1-feign
     */
    private Integer origin;

    /**
     * 追踪id, todo：制定生成规则
     */
    private String traceId;

    /**
     * 请求数据
     */
    private String requestData;

    /**
     * 结果，0-成功，1-失败
     */
    private Integer result = 0;

    /**
     * 响应数据
     */
    private String responseData;

    /**
     * 响应时长
     */
    private Long expend;

    /**
     * 错误信息
     */
    private String errorMsg;

}
