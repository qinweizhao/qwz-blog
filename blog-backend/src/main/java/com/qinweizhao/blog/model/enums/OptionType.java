package com.qinweizhao.blog.model.enums;

/**
 * Option Type.
 *
 * @author ryanwang
 * @since 2019-12-02
 */
public enum OptionType implements ValueEnum<Integer> {

    /**
     * internal option
     */
    INTERNAL(0),

    /**
     * custom option
     */
    CUSTOM(1);

    private final Integer value;

    OptionType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
