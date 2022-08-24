package com.qinweizhao.blog.model.properties;

import org.springframework.lang.NonNull;

/**
 * Sheet properties.
 *
 * @author ryanwang
 * @since 2020-02-11
 */
public enum SheetProperties implements PropertyEnum {


    /**
     * Journals page title.
     */
    JOURNALS_TITLE("journals_title", String.class, "日志"),

    /**
     * Journals page size.
     */
    JOURNALS_PAGE_SIZE("journals_page_size", Integer.class, "10");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    SheetProperties(String value, Class<?> type, String defaultValue) {
        this.value = value;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    @NonNull
    public String defaultValue() {
        return defaultValue;
    }
}
