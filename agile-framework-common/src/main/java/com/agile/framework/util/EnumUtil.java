package com.agile.framework.util;

import com.agile.framework.bean.IBaseEnum;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 枚举工具类
 * @date: 2023/5/16 16:24
 * @author: chenzhanshang
 */
public class EnumUtil {

    // 枚举缓存
    @SuppressWarnings("rawtypes")
    private static final ConcurrentMap<Class<? extends IBaseEnum>, Map<Object, Object>> cache = new ConcurrentHashMap<>();

    /**
     * 枚举转Map
     * @param clazz 枚举类型
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<Object, Object> enumConvertToMap(Class<? extends IBaseEnum> clazz) {
        Map<Object, Object> mapResult = new LinkedHashMap<>();
        if(clazz == null || !clazz.isEnum()) {
            throw new UnsupportedOperationException("参数不合法：clazz为空或非枚举类，不支持转换，请检查程序是否有误！");
        }
        if(cache.get(clazz) != null) {
            return cache.get(clazz);
        }
        // 通过class.getEnumConstants();获取所有的枚举字段和值
        IBaseEnum[] iBaseEnums = clazz.getEnumConstants();
        for (IBaseEnum iBaseEnum : iBaseEnums) {
            mapResult.put(iBaseEnum.getValue(), iBaseEnum.getLabel());
        }
        cache.put(clazz, mapResult);
        return mapResult;
    }
}
