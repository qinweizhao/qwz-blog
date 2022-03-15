package com.qinweizhao.blog.model.params;

import lombok.Data;
import com.qinweizhao.blog.model.enums.OptionType;

/**
 * Option query params.
 *
 * @author ryanwang
 * @date 2019-12-02
 */
@Data
public class OptionQuery {

    private String keyword;

    private OptionType type;
}
