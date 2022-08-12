package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.JournalType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Journal dto.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-04-24
 */
@Data
public class JournalDTO {

    private Integer id;

    private String sourceContent;

    private String content;

    private Long likes;

    private LocalDateTime createTime;

    private JournalType type;

    private Long commentCount;
}
