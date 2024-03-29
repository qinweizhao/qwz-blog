package com.qinweizhao.blog.model.dto;


import com.qinweizhao.blog.model.enums.ConfigType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
public class ConfigDTO {

    private Integer id;

    private String key;

    private Object value;

    private ConfigType type;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
