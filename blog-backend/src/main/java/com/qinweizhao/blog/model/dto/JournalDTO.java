package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.JournalType;
import lombok.Data;

import java.util.Date;

/**
 * Journal dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-24
 */
@Data
public class JournalDTO {

    private Integer id;

    private String sourceContent;

    private String content;

    private Long likes;

    private Date createTime;

    private JournalType type;
}
