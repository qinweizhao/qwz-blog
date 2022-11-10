package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.Mode;
import lombok.Data;

/**
 * Theme controller.
 *
 * @since 2019/5/4
 */
@Data
public class EnvironmentDTO {

    private String database;

    private Long startTime;

    private String version;

    private Mode mode;
}
