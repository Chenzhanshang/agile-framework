package com.agile.framework.bean;

/**
 * 枚举基类
 * @date: 2025/03/25 16:20
 * @author: chenzhanshang
 */
public interface IBaseEnum<T, R> {

    T getValue();

    R getLabel();
}
