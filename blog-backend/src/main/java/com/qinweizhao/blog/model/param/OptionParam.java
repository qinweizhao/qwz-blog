package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.enums.OptionType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Optional param.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-03-20
 */
@Data
public class OptionParam {

    @NotBlank(message = "Option key must not be blank")
    @Size(max = 100, message = "Length of option key must not be more than {max}")
    private String key;


    @Size(max = 1023, message = "Length of option value must not be more than {max}")
    private String value;

    private OptionType type;
}
