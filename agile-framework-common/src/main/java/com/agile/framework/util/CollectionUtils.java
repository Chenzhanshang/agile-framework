package com.agile.framework.util;

import java.util.*;

/**
 * Collection工具类
 *
 * @author chenzhanshang
 */
public class CollectionUtils {

    /**
     * 校验集合是否为空
     *
     * @param coll 入参
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 校验集合是否不为空
     *
     * @param coll 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断Map是否为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 获取一个非 null 的 list
     *
     * @param list list
     * @param <E>  element
     * @return non null list
     */
    public static <E> List<E> getNonNullList(List<E> list) {
        return Objects.isNull(list) ? Collections.emptyList() : list;
    }

    /**
     * 获取一个非 null 的 set
     *
     * @param set set
     * @param <E> element
     * @return non null set
     */
    public static <E> Set<E> getNonNullSet(Set<E> set) {
        return Objects.isNull(set) ? Collections.emptySet() : set;
    }

    /**
     * 获取一个非 null 的 map
     *
     * @param map map
     * @param <K> key
     * @param <V> value
     * @return non null map
     */
    public static <K, V> Map<K, V> getNonNullMap(Map<K, V> map) {
        return Objects.isNull(map) ? Collections.emptyMap() : map;
    }

}
