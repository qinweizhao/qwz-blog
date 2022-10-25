package com.qinweizhao.blog.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * Option Type.
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-12-02
 */
public enum ConfigType implements ValueEnum<Integer> {

    /**
     * 后台
     */
    ADMIN(0),

    /**
     * 前台
     */
    PORTAL(1);

    @EnumValue
    private final Integer value;

    ConfigType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
