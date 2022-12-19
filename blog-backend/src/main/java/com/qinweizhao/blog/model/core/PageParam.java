package com.qinweizhao.blog.model.core;

/**
 * 分页查询参数
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
public abstract class PageParam {

    private static final int DEFAULT_PAGE_NUM = 1;

    private static final int DEFAULT_PAGE_SIZE = 10;

    private int size = DEFAULT_PAGE_SIZE;

    private int page = DEFAULT_PAGE_NUM;

    private String group;

    private String sort;


    public int getPage() {
        return Math.max(page, 1);
    }

    public PageParam setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        if (size < 1) {
            size = DEFAULT_PAGE_SIZE;
        }
        return size;
    }

    public PageParam setSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.size = pageSize;
        return this;
    }

    public int getOffset() {
        return (getPage() - 1) * getSize();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
