package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.enums.ConfigType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author qinweizhao
 * @since 2019-03-20
 */
@Data
public class ConfigParam {

    private Integer id;

    @NotBlank(message = "键不能为空")
    @Size(max = 100, message = "键的长度不能超过 {max}")
    private String key;


    @Size(max = 1023, message = "值的长度不能超过 {max}")
    private String value;

    private ConfigType type;
}
