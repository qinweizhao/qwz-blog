package com.qinweizhao.blog.model.dto;

import lombok.Data;
import com.qinweizhao.blog.model.enums.Mode;

/**
 * Theme controller.
 *
 * @author ryanwang
 * @date 2019/5/4
 */
@Data
public class EnvironmentDTO {

    private String database;

    private Long startTime;

    private String version;

    private Mode mode;
}
