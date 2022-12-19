package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.core.BaseTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Menu output dto.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuDTO extends BaseTree<MenuDTO> {

    private Integer id;

    private String name;

    private String url;

    private Integer priority;

    private String target;

    private String icon;

    private String team;

}
