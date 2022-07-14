package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * Category output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
@Data
public class CategoryDTO {

    private Integer id;

    private String name;

    private String slug;

    private String description;

    private String thumbnail;

    private Integer parentId;

    private Date createTime;

    private String fullPath;
}
