package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Tag output dto.
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
@Data
public class TagDTO {

    private Integer id;

    private String name;

    private String slug;

    private String thumbnail;

    private LocalDateTime createTime;

    private String fullPath;

    private Long postCount;
}
