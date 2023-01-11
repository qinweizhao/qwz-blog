package com.qinweizhao.blog.framework.handler.theme.config.support;

import lombok.Data;

import java.util.List;

/**
 * Theme configuration: group entity.
 *
 * @author qinweizhoa
 * @since 4/10/19
 */
@Data
public class Group {

    /**
     * Tab name.
     */
    private String name;

    /**
     * Tab label.
     */
    private String label;

    /**
     * Tab's items, default is empty list.
     */
    private List<Item> items;
}
