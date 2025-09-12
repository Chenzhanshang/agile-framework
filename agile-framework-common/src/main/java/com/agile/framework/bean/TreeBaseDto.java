package com.agile.framework.bean;

import java.util.List;

public interface TreeBaseDto<T extends TreeBaseDto<T>> {
     Long getId();

    Long getParentId();

    void setChildrenList(List<T> children);
}
