package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@ToString
@EqualsAndHashCode
public class LogDTO {

    private Long id;

    private String logKey;

    private LogType type;

    private String content;

    private String ipAddress;

    private LocalDateTime createTime;
}
