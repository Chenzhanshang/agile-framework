package com.agile.framework.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文信息管理器
 *
 * @author chenzhanshang
 */
public final class ContextManager {

    private static final ThreadLocal<Map<String, Object>> TL = new ThreadLocal<>();

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(String key) {
        return (T) getContext().get(key);
    }

    public static void addAttribute(String key, Object value) {
        getContext().put(key, value);
    }

    public static void removeAttribute(String key) {
        getContext().remove(key);
    }

    public static Map<String, Object> getContext() {
        if (TL.get() == null) {
            TL.set(new HashMap<>(16));
        }
        return TL.get();
    }

    public static void removeContext() {
        if (TL.get() != null) {
            TL.remove();
        }
    }

    private ContextManager() {
    }

}
