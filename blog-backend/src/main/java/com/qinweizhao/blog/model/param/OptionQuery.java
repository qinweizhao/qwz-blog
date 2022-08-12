package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.enums.OptionType;
import lombok.Data;

/**
 * Option query params.
 *
 * @author ryanwang
 * @since 2019-12-02
 */
@Data
public class OptionQuery {

    private String keyword;

    private OptionType type;
}
