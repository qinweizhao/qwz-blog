package com.qinweizhao.blog.model.enums;

/**
 * Post status.
 *
 * @author johnniang
 */
public enum PostStatus implements ValueEnum<Integer> {

    /**
     * 发布
     */
    PUBLISHED(0),

    /**
     * 草稿
     */
    DRAFT(1),

    /**
     * 回收
     */
    RECYCLE(2),

    /**
     * Intimate status
     */
    INTIMATE(3);

    private final int value;

    PostStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
