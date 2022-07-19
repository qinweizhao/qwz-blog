package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.base.BaseTree;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Category output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryDTO extends BaseTree<CategoryDTO> {

    private Integer id;

    private String name;

    private String slug;

    private String description;

    private String thumbnail;

    private Date createTime;

    private String fullPath;
}
