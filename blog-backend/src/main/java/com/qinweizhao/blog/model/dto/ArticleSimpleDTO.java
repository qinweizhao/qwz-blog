package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.ArticleStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qinweizhao
 * @since 2022-08-07
 */
@Data
public class ArticleSimpleDTO {

    private Integer id;

    private String title;

    private ArticleStatus status;

    private String metaKeywords;

    private String metaDescription;

    private String summary;

    private String fullPath;

    private String thumbnail;

    private Long visits;

    private Boolean disallowComment;

    private Integer topPriority;

    private LocalDateTime createTime;

    private List<CategoryDTO> categories;

    public boolean isTopped() {
        return this.topPriority != null && this.topPriority > 0;
    }

}