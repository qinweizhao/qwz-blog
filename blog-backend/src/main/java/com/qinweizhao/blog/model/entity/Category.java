package com.qinweizhao.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qinweizhao.blog.model.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String description;

    private String name;

    private Integer parentId;

    private Integer priority;

    private String slug;

    private String thumbnail;

}
