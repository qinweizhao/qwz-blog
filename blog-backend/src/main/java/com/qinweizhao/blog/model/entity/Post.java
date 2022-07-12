package com.qinweizhao.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qinweizhao.blog.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Post extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer type;

    private Boolean disallowComment;

    private LocalDateTime editTime;

    private Integer editorType;

    private String formatContent;

    private Long like;

    private String metaDescription;

    private String metaKeywords;

    private String originalContent;

    private String password;

    private String slug;

    private Integer status;

    private String summary;

    private String template;

    private String thumbnail;

    private String title;

    private Integer topPriority;

    private String url;

    private Long visit;

    private Long wordCount;

}
