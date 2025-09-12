package com.agile.framework.log;

import com.agile.framework.log.dto.SqlLogDTO;

/**
 * sql日志信息
 * @author chenzhanshang
 */
public class SqlLogHolder {
    private static ThreadLocal<SqlLogDTO> CONTEXT = ThreadLocal.withInitial(SqlLogDTO::new);

    public static void setSqlLog(SqlLogDTO sqlLogDTO) {
        CONTEXT.set(sqlLogDTO);
    }

    public static SqlLogDTO getSqlLog() {
        return CONTEXT.get();
    }

    public static void removeSql() {
       CONTEXT.remove();
    }
}
