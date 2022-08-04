package com.qinweizhao.blog.model.core;


import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询参数
 *
 * @author qinweizhao
 */
public abstract class PageParam {

    private static final long serialVersionUID = 1L;

    public static final String ASC = "ASC";

    public static final String DESC = "DESC";

    private static final int DEFAULT_PAGE_SIZE = 10;

    private int size = DEFAULT_PAGE_SIZE;

    private int page = 1;

    private String orderDirection = DESC;

    private String groupBy;

    private boolean needTotalCount = true;


    private List<Sort> sorts = new ArrayList<>();

    public static class Sort {

        /**
         * 属性
         */
        private String field;

        /**
         * 是否正序排序
         */
        private boolean asc;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public boolean isAsc() {
            return asc;
        }

        public void setAsc(boolean asc) {
            this.asc = asc;
        }
    }

    public List<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    public int getPage() {
        if (page < 1) {
            return 1;
        }
        return page;
    }

    public PageParam setPage(int pageIndex) {
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

    public String getOrderDirection() {
        return orderDirection;
    }

    public PageParam setOrderDirection(String orderDirection) {
        if (ASC.equalsIgnoreCase(orderDirection) || DESC.equalsIgnoreCase(orderDirection)) {
            this.orderDirection = orderDirection;
        }
        return this;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public boolean isNeedTotalCount() {
        return needTotalCount;
    }

    public void setNeedTotalCount(boolean needTotalCount) {
        this.needTotalCount = needTotalCount;
    }

}
