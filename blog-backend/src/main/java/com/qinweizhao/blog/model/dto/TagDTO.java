package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Tag output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-19
 */
@Data
public class TagDTO {

    private Integer id;

    private String name;

    private String slug;

    private String thumbnail;

    private LocalDateTime createTime;

    private String fullPath;
}
