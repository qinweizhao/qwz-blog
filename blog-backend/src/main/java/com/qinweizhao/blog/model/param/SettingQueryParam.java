package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
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
public class SettingQueryParam extends PageParam {

    private String keyword;

}
