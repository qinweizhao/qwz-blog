package com.qinweizhao.site.model.params;

import lombok.Data;
import com.qinweizhao.site.model.enums.OptionType;

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
