package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.PostEditorType;
import com.qinweizhao.blog.model.enums.PostStatus;
import lombok.Data;

import java.util.Date;

/**
 * Base post detail output dto.
 *
 * @author johnniang
 */
@Data
public class PostDetailDTO {

    /**
     * MinimalDTO
     */
    private Integer id;

    private String title;

    private PostStatus status;

    private String slug;

    private PostEditorType editorType;

    private Date updateTime;

    private Date createTime;

    private Date editTime;

    private String metaKeywords;

    private String metaDescription;

    private String fullPath;

    private String summary;

    private String thumbnail;

    private Long visits;

    private Boolean disallowComment;

    private String password;

    private String template;

    private Integer topPriority;

    private Long likes;

    private Long wordCount;

    /**
     * ++
     */
    private String formatContent;

    public boolean isTopped() {
        return this.topPriority != null && this.topPriority > 0;
    }

    private String originalContent;


    private Long commentCount;
}
