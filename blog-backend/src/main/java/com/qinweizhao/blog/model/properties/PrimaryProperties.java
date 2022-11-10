package com.qinweizhao.blog.model.properties;


/**
 * Primary properties.
 *
 * @since 4/2/19
 */
public enum PrimaryProperties implements PropertyEnum {

    /**
     * developer mode.
     */
    DEV_MODE("developer_mode", Boolean.class, "false"),

    /**
     * default menu team name
     */
    DEFAULT_MENU_TEAM("default_menu_team", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    PrimaryProperties(String value, Class<?> type, String defaultValue) {
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
