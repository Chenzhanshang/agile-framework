package com.agile.framework.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数、字符串、UUID 生成工具类
 *
 * @author chenzhanshang
 */
public final class RandomUtils {

    /**
     * 生成唯一标识码
     */
    public static String createUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取32位不带中划线的UUID
     */
    public static String create32UUID() {
        return createUUID().replace("-", "");
    }

    /**
     * 获取一个字母和数字组合的随机字符串
     *
     * @param length 字符串长度
     */
    public static String createRandomString(int length) {
        String str = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
        return randomStr(str, length);
    }

    /**
     * 获取一个数字组成的随机字符串
     *
     * @param length 字符串长度
     */
    public static String createNumberString(int length) {
        String str = "0123456789";
        return randomStr(str, length);
    }

    /**
     * 获取一个随机整数，左闭右开：[origin, bound)
     *
     * @param origin 开始范围，闭区间
     * @param bound  结束范围，开区间
     * @return 随机整数
     */
    public static int createRandomInt(int origin, int bound) {
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

    private static String randomStr(String baseStr, int length) {
        StringBuilder sBuilder = new StringBuilder(length);
        double r;
        for (int i = 0; i < length; i++) {
            r = Math.random() * baseStr.length();
            sBuilder.append(baseStr.charAt((int) r));
        }
        return sBuilder.toString();
    }

    private RandomUtils() {
    }

}
