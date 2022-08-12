package com.qinweizhao.blog.model.dto;

import lombok.Data;

/**
 * Link output dto.
 *
 * @author ryanwang
 * @since 2019/3/21
 */
@Data
public class LinkDTO {

    private Integer id;

    private String name;

    private String url;

    private String logo;

    private String description;

    private String team;

    private Integer priority;
}
