package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Base meta Dto.
 *
 * @author qinweizhao
 * @since 2019-12-10
 */
@Data
public class MetaDTO {
    private Long id;

    private Integer articleId;

    private String key;

    private String value;

    private LocalDateTime createTime;
}
