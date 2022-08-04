package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Base meta Dto.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-12-10
 */
@Data
public class MetaDTO {
    private Long id;

    private Integer postId;

    private String key;

    private String value;

    private LocalDateTime createTime;
}
