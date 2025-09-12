package com.agile.framework.bean;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 树状结构数据工具类
 * @date: 2023/5/11 15:22
 * @author: chenzhanshang
 */
public class TreeUtil {
    /**
     * list转树形结构
     * 1. 父级id使用parentId,关联到当前数据的id, 0为根级
     * 2. 孩子数据存储于children
     * 3. 传入list的泛型需要实现TreeBaseDto<用户返回值类型>
     * @param list
     * @param <T> T extends TreeBaseDto<用户返回值类型>
     * @return
     */
    public static <T extends TreeBaseDto<T>> List<T> covertTree(List<T> list) {
        if(CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 获取所有根
        List<T> rootList = list.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
        for (T root : rootList) {
            covertOneNode(root, list);
        }
        return rootList;
    }

    /**
     * 解析一个节点的子集
     * @param root 节点
     * @param list 原始list数据
     * @param <T>
     */
    public static  <T extends TreeBaseDto<T>> void covertOneNode(T root, List<T> list) {
        // 获取该节点的所有子集
        List<T> childrenList = list.stream().filter(e -> Objects.equals(e.getParentId(), root.getId())).collect(Collectors.toList());
        root.setChildrenList(childrenList);
        if(CollectionUtils.isEmpty(childrenList)) {
            return;
        }
        // 解析子集的子集
        for (T child : childrenList) {
            covertOneNode(child, list);
        }
    }
}
