package com.qinweizhao.blog.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
public enum ArticleStatus implements ValueEnum<Integer> {

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
    RECYCLE(2);

    @EnumValue
    private final int value;

    ArticleStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
