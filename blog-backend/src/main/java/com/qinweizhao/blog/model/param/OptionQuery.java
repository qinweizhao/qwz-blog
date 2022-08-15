package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.enums.OptionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Option query params.
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OptionQuery extends PageParam {

    private String keyword;

    private OptionType type;
}
