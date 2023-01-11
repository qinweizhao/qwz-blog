package com.qinweizhao.blog.framework.handler.theme.config.support;

import lombok.Data;

/**
 * Theme configuration: option entity.
 *
 * @author qinweizhoa
 * @since 4/10/19
 */
@Data
public class Option {

    /**
     * Option label.
     */
    private String label;

    /**
     * Option value.
     */
    private Object value;
}
