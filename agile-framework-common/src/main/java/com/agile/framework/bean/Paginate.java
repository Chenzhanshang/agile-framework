package com.agile.framework.bean;

import com.agile.framework.util.CollectionUtils;
import com.agile.framework.util.JsonUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * {"page":1,"size":10,"sort":[{"asc":"hello"},{"desc":"world"}]}
 * spring request parameter不支持类似sort[1][asc]这样的方式, 因此需要在客户端即进行stringify
 * 例如query=%7B%22page%22%3A0%2C%22size%22%3A10%2C%22sort%22%3A%5B%7B%22asc%22%3A%22hello%22%7D%2C%7B%22desc%22%3A%22world%22%7D%5D%7D
 * 实际上是字符串{"page":1,"size":10,"sort":[{"asc":"hello"},{"desc":"world"}]}的urlencode的结果
 * 又或者可以接收这样的参数
 * page=0&size=10&sort=%7B%22asc%22%3A%22hello%22%7D&sort=%7B%22desc%22%3A%22world%22%7D
 * 即传多个sort参数, 由spring组装数组, sort的值则为数组元素的urlencode的值
 * @date: 2025/05/27 17:12
 * @author: chenzhanshang
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paginate implements Serializable {

    private static final long serialVersionUID = 1L;

    private long page = 1;

    private long size = 10;

    private Sorts sort = null;

    public Paginate(String jsonStr) {
        Paginate res = JsonUtil.decode(jsonStr, Paginate.class);
        if (res != null) {
            this.page = res.page;
            this.size = res.size;
            this.sort = res.sort;
        }
    }

    public static class Sorts extends ArrayList<Order> {
        private static final long serialVersionUID = 1L;

        public Sorts() {
            super();
        }
    }

    @Data
    @NoArgsConstructor
    public static class Order implements Serializable {
        private static final long serialVersionUID = 1L;
        private static final String ASCEND = "asc";
        private static final String DESCEND = "desc";
        private String asc;
        private String desc;

        public Order(String jsonStr) {
            Order res = JsonUtil.decode(jsonStr, Order.class);
            if (null != res.desc) {
                desc = res.desc;
            } else if (null != res.asc) {
                asc = res.asc;
            }
        }

        public Order(String direction, String property) {
            if (DESCEND.equals(direction)) {
                desc = property;
            } else if (ASCEND.equals(direction)) {
                asc = property;
            }
        }

        @JsonIgnore
        public boolean isAscending() {
            return null != asc;
        }

        @JsonIgnore
        public boolean isDescending() {
            return null != desc;
        }
    }

    /**
     * 转mybatis-plus 分页对象
     * @return
     * @param <T>
     */
    public <T> IPage<T> toPage() {
        return toPage(true);
    }

    /**
     * 转mybatis-plus 分页对象
     * @param camelCaseToUnderscore 排序属性是否驼峰转下划线
     * @param <T>
     */
    public <T> IPage<T> toPage(boolean camelCaseToUnderscore) {
        Page<T> pageObject = new Page<>(page, size);
        if (CollectionUtils.isNotEmpty(sort)) {
            for (Order order : sort) {
                if (camelCaseToUnderscore) {
                    if (null != order.asc)
                        order.asc = order.asc.replaceAll("([A-Z])", "_$1").toLowerCase();
                    if (null != order.desc)
                        order.desc = order.desc.replaceAll("([A-Z])", "_$1").toLowerCase();
                }
                if (order.isAscending()) {
                    pageObject.addOrder(new OrderItem(order.asc, true));
                } else if (order.isDescending()) {
                    pageObject.addOrder(new OrderItem(order.desc, false));
                }
            }
        }
        return pageObject;
    }
}
