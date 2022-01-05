package com.qinweizhao.site.model.params;

import com.qinweizhao.site.model.dto.base.InputConverter;
import com.qinweizhao.site.model.entity.Option;
import com.qinweizhao.site.model.enums.OptionType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Optional param.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-20
 */
@Data
public class OptionParam implements InputConverter<Option> {

    @NotBlank(message = "Option key must not be blank")
    @Size(max = 100, message = "Length of option key must not be more than {max}")
    private String key;

    private String value;

    private OptionType type;
}
