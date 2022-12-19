package com.qinweizhao.blog.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 评论状态
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
public enum CommentStatus implements ValueEnum<Integer> {

    /**
     * 已发布
     */
    PUBLISHED(0),

    /**
     * 待审核
     */
    AUDITING(1),

    /**
     * 回收站
     */
    RECYCLE(2);

    @EnumValue
    private final Integer value;

    CommentStatus(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
