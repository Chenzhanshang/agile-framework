package com.agile.framework.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 用户上下文
 *
 * @author chenzhanshang
 */
@Slf4j
public final class UserContext {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static String getUsername() {
        String userName = ContextManager.getAttribute(ContextCacheKey.CURRENT_USERNAME);
        if (StringUtils.isEmpty(userName)) return userName;
        try {
            return URLDecoder.decode(userName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("解码用户名异常:{}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * 获取当前用户id
     * B/C端都可以使用该方法
     *
     * @return
     */
    public static Long getUserId() {
        return parseLong(ContextManager.getAttribute(ContextCacheKey.CURRENT_USER_ID));
    }

    /**
     * 防异常处理
     *
     * @param attr
     * @return
     */
    private static Long parseLong(Object attr) {
        if (null == attr) {
            return null;
        }
        try {
            return Long.parseLong(attr.toString());
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 当前访问资源是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return null != getUserId();
    }

    private UserContext() {
    }
}
