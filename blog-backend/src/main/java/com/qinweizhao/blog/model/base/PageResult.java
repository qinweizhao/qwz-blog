package com.qinweizhao.blog.model.base;


import java.util.Collections;
import java.util.List;

/**
 * 分页返回结果
 *
 * @author qinweizhao
 */
public class PageResult<T> {

    /**
     * 查询数据列表
     */
    protected List<T> content = Collections.emptyList();

    /**
     * 总数
     */
    protected Long total = 0L;

    /**
     * 是否有上一页
     */
    protected Boolean hasPrevious = false;

    /**
     * 是否有下一页
     */
    protected Boolean hasNext = false;


    public PageResult() {
    }

    public PageResult(long total) {
        this.total = total;
    }

    public PageResult(List<T> content, long total,boolean hasPrevious,boolean hasNext) {
        this.content = content;
        this.total = total;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Boolean hasPrevious() {
        return hasPrevious;
    }



    public Boolean hasNext() {
        return hasNext;
    }


}
