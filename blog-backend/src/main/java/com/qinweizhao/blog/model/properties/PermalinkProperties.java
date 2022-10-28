package com.qinweizhao.blog.model.properties;

/**
 * Permalink properties enum.
 *
 * @author ryanwang
 * @since 2020-01-07
 */
public enum PermalinkProperties implements PropertyEnum {


    /**
     * Categories prefix
     * such as: /categories or /categories/${slug}
     */
    CATEGORIES_PREFIX("categories_prefix", String.class, "categories"),

    /**
     * Tags prefix
     * such as: /tags or /tags/${slug}
     */
    TAGS_PREFIX("tags_prefix", String.class, "tags"),

    /**
     * Article prefix
     */
    ARTICLE_PREFIX("article_prefix", String.class, "article"),

    /**
     * Archives prefix.
     * such as: /archives
     */
    ARCHIVES_PREFIX("archives_prefix", String.class, "archives"),

    /**
     * Journals page prefix
     * default is journals
     */
    JOURNALS_PREFIX("journals_prefix", String.class, "journals");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    PermalinkProperties(String value, Class<?> type, String defaultValue) {
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
