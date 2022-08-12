package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qinweizhao
 * @since 2022-08-07
 */
@Data
public class PostSimpleDTO {

    private Integer id;

    private String title;

    private PostStatus status;

    private String metaKeywords;

    private String metaDescription;

    private String summary;

    private String fullPath;

    private String thumbnail;

    private Long visits;

    private Boolean disallowComment;

    private Integer topPriority;

    private Long likes;

    private LocalDateTime createTime;

    public boolean isTopped() {
        return this.topPriority != null && this.topPriority > 0;
    }

}
