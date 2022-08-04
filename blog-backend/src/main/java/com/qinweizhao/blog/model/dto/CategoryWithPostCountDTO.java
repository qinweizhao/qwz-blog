package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Category with post count dto.
 *
 * @author johnniang
 * @date 19-4-23
 */
@Data
public class CategoryWithPostCountDTO {

    private Integer id;

    private String name;

    private String slug;

    private String description;

    private String thumbnail;

    private Integer parentId;

    private LocalDateTime createTime;

    private String fullPath;

    private Long postCount;
}
