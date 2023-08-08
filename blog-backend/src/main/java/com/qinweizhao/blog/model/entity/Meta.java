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
public class Meta extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer type;

    private String metaKey;

    private Integer articleId;

    private String metaValue;

}
