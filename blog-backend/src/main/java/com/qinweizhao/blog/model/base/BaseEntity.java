package com.qinweizhao.blog.model.base;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
public class BaseEntity {

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
