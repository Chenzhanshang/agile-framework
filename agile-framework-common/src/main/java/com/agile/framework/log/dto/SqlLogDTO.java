package com.agile.framework.log.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * sql日志信息
 * @author: chenzhanshang
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SqlLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 服务器ip
     */
    private String serverIp;

    /**
     * 服务名
     */
    private String serverName;

    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 执行sql
     */
    private String execSql;

    /**
     * 执行耗时
     */
    private Long expend;

    /**
     * 操作/查询数量
     */
    private Integer count;

    /**
     * 时间
     */
    private Date executeTime;

    /**
     * 追踪id
     */
    private String traceId;

    /**
     * 数据库地址
     */
    private String dbHost;

    /**
     * 数据库名称
     */
    private String dbName;
}
