package com.qinweizhao.blog.model.core;

import java.io.Serializable;
import java.util.List;

/**
 * @author qinweizhao
 * @since 2022-07-06
 */
public abstract class BaseTree<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 父 ID
     */
    private Long parentId;

    /**
     * 子元素
     */
    private List<T> children;

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
