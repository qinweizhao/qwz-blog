package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.ConfigType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Option list output dto.
 *
 * @author ryanwang
 * @since 2019-12-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConfigSimpleDTO extends ConfigDTO {

    private Integer id;

    private ConfigType type;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
