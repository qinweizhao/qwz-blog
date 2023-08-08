package com.qinweizhao.blog.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qinweizhao.blog.model.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qinweizhao
 * @since 2022/8/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("content")
public class Content extends BaseEntity {

    /**
     * 文章编号
     */
    @TableId(value = "post_id")
    private Integer articleId;

    /**
     * 内容
     */
    private String content;

    /**
     * 原始内容
     */
    private String originalContent;

    /**
     * 状态
     */
    private Integer status;

}
