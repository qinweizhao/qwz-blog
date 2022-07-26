package com.qinweizhao.blog.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

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
     * 隐私
     */
    INTIMATE(3);

    @EnumValue
    private final int value;

    PostStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
