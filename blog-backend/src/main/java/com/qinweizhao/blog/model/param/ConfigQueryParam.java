package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.enums.ConfigType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Option query params.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ConfigQueryParam extends PageParam {

    private String keyword;

    private ConfigType type;
}
