package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.Mode;
import lombok.Data;

/**
 * Theme controller.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
public class EnvironmentDTO {

    private String database;

    private Long startTime;

    private String version;

    private Mode mode;
}
