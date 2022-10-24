package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
public class PostDTO {

    private Integer id;

    private String title;

    private String slug;

    private PostStatus status;

    private String metaKeywords;

    private String metaDescription;

    private String fullPath;

    private String formatContent;

    private String originalContent;

    private String summary;

    private String thumbnail;

    private Long visits;

    private Boolean disallowComment;

    private Integer topPriority;

    private Long likes;

    private Long wordCount;

    private Long commentCount;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;

    private Set<Integer> tagIds;

    private List<TagDTO> tags;

    private Set<Integer> categoryIds;

    private List<CategoryDTO> categories;

}
