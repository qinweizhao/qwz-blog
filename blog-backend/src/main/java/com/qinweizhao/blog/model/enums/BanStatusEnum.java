package com.qinweizhao.blog.model.enums;

import lombok.Getter;

/**
 * 封禁状态
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Getter
public enum BanStatusEnum {
    /**
     * 封禁状态
     */
    NORMAL(0);

    private final int status;

    BanStatusEnum(int status) {
        this.status = status;
    }
}
