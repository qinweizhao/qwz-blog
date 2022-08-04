package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author qinweizhao
 * @date 2022-08-07
 */
@Data
public class PostSimpleDTO {

    private Integer id;

    private String title;

    private PostStatus status;

    private String slug;

    private String metaKeywords;

    private String metaDescription;

    private String fullPath;

    private String summary;

    private String thumbnail;

    private Long visits;

    private Boolean disallowComment;

    private String password;

    private String template;

    private Boolean topped;

    private Long likes;

    private Long wordCount;

    private String formatContent;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

}
