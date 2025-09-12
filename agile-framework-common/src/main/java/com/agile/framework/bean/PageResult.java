package com.agile.framework.bean;

import com.agile.framework.util.CollectionUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页查询结果集对象
 *
 * @author chenzhanshang
 */
@Data
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页
     */
    private long page;

    /**
     * 每页条数
     */
    private long size;

    /**
     * 数据总条数
     */
    private long total;

    /**
     * 当前分页数据
     */
    protected List<T> list;

    public PageResult(long page, long size, long total, List<T> list) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.list = list;
    }

    /**
     * 构建分页查询结果集
     *
     * @param total 数据总量
     * @param list  分页数据
     * @param <T>   泛型
     * @return 分页数据结果集
     */
    public static <T> PageResult<T> of(long page, long size, long total, List<T> list) {
        return new PageResult<>(page, size, total, list);
    }

    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    /**
     * 空页
     *
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(1, 10, 0, Collections.emptyList());
    }

    /**
     * 转化
     *
     * @param convert
     * @param <R>
     * @return
     */
    public <R> PageResult<R> convert(Function<T, R> convert) {
        return new PageResult<>(page, size, total, list.stream().map(convert).collect(Collectors.toList()));
    }

    /**
     * 包装
     *
     * @param consumer
     * @return
     */
    public PageResult<T> wrapper(Consumer<List<T>> consumer) {
        if (CollectionUtils.isNotEmpty(list))
            consumer.accept(list);
        return this;
    }

}
