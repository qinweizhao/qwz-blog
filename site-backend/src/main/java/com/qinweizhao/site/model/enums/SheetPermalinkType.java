package com.qinweizhao.site.model.enums;

/**
 * Sheet Permalink type enum.
 *
 * @author ryanwang
 * @date 2020-12-01
 */
public enum SheetPermalinkType implements ValueEnum<Integer> {

    /**
     * /{@link com.qinweizhao.site.model.properties.PermalinkProperties#SHEET_PREFIX}/${slug}
     */
    SECONDARY(0),

    /**
     * /${slug}
     */
    ROOT(1);

    private final Integer value;

    SheetPermalinkType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
