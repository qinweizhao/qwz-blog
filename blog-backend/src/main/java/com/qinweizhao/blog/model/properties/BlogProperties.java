package com.qinweizhao.blog.model.properties;

/**
 * @since 2019-03-17
 */
public enum BlogProperties implements PropertyEnum {


    /**
     * Blog locale.
     */
    BLOG_LOCALE("blog_locale", String.class, ""),

    /**
     * Blog title.
     */
    BLOG_TITLE("blog_title", String.class, "Blog"),

    /**
     * Blog logo.
     */
    BLOG_LOGO("blog_logo", String.class, ""),

    /**
     * Blog favicon.
     */
    BLOG_FAVICON("blog_favicon", String.class, ""),

    /**
     * Blog footer info.
     */
    BLOG_FOOTER_INFO("blog_footer_info", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    BlogProperties(String value, Class<?> type, String defaultValue) {
        this.defaultValue = defaultValue;
        if (!PropertyEnum.isSupportedType(type)) {
            throw new IllegalArgumentException("Unsupported blog property type: " + type);
        }

        this.value = value;
        this.type = type;
    }

    /**
     * Get enum value.
     *
     * @return enum value
     */
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

}
