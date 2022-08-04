package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.core.BaseTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Category output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
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

    private LocalDateTime createTime;

    private String fullPath;
}
