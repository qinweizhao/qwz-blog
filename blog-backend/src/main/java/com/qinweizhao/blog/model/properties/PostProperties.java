package com.qinweizhao.blog.model.properties;

import com.qinweizhao.blog.model.enums.TimeUnit;

/**
 * Post properties.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-04-01
 */
public enum PostProperties implements PropertyEnum {

    /**
     * Post summary words length.
     */
    SUMMARY_LENGTH("post_summary_length", Integer.class, "150"),

    /**
     * Post index page size.
     */
    INDEX_PAGE_SIZE("post_index_page_size", Integer.class, "10"),

    /**
     * 归档分页大小
     */
    ARCHIVES_PAGE_SIZE("post_archives_page_size", Integer.class, "10"),

    /**
     * 启用自动清理回收状态文章
     */
    RECYCLED_POST_CLEANING_ENABLED("recycled_post_cleaning_enabled", Boolean.class, "false"),

    /**
     * 回收后保留时间
     */
    RECYCLED_POST_RETENTION_TIME("recycled_post_retention_time", Integer.class, "30"),

    /**
     * 回收文章后保留时间单位
     */
    RECYCLED_POST_RETENTION_TIMEUNIT("recycled_post_retention_timeunit", TimeUnit.class, TimeUnit.DAY.name());

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    PostProperties(String value, Class<?> type, String defaultValue) {
        this.value = value;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String getValue() {
        return value;
    }
}
