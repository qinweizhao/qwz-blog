package com.qinweizhao.blog.model.enums;

/**
 * Comment type.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
public enum CommentType implements ValueEnum<Integer> {

    /**
     * 文章
     */
    POST(0),

    /**
     * 日志
     */
    JOURNAL(1);

    private final Integer value;

    CommentType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
