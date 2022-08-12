package com.qinweizhao.blog.model.enums;

/**
 * Post Permalink type enum.
 *
 * @author ryanwang
 * @since 2020-01-07
 */
public enum PostPermalinkType implements ValueEnum<Integer> {


    /**
     * /?p=${id}
     */
    ID(3);

    private final Integer value;

    PostPermalinkType(Integer value) {
        this.value = value;
    }


    @Override
    public Integer getValue() {
        return value;
    }
}
