package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.enums.ConfigType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Optional param.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-20
 */
@Data
public class ConfigParam {

    private Integer id;

    @NotBlank(message = "Option key must not be blank")
    @Size(max = 100, message = "Length of option key must not be more than {max}")
    private String key;


    @Size(max = 1023, message = "Length of option value must not be more than {max}")
    private String value;

    private ConfigType type;
}
