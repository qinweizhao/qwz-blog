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
public class Post extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 别名
     */
    private String slug;

    /**
     * 摘要
     */
    private String summary;

    /**
     * seo 关键字
     */
    private String metaKeywords;

    /**
     * seo 描述
     */
    private String metaDescription;

    /**
     * 访问量
     */
    private Long visits;

    /**
     * 字数
     */
    private Long wordCount;

    /**
     * 点赞数
     */
    private Long likes;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否允许评论
     */
    private Boolean disallowComment;

    /**
     * 优先级
     */
    private Integer topPriority;

}
