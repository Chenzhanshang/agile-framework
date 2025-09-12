package com.agile.framework.util;

import com.agile.framework.exception.BusinessException;

/**
 * 数字工具类
 *
 * @author chenzhanshang
 */
public final class NumUtils {

    /**
     * 判断数字为 null 或 0
     *
     * @param number 数字
     * @return 判定结果
     */
    public static boolean isNullOrZero(Number number) {
        if (number == null) {
            return true;
        }
        if (number instanceof Integer) {
            return (Integer) number == 0;
        }
        if (number instanceof Long) {
            return (Long) number == 0;
        }
        if (number instanceof Byte) {
            return (Byte) number == 0;
        }
        if (number instanceof Short) {
            return (Short) number == 0;
        }
        if (number instanceof Double) {
            return (Double) number == 0;
        }
        if (number instanceof Float) {
            return (Float) number == 0;
        }
        throw BusinessException.of("number type error");
    }

    private NumUtils() {
    }

}
