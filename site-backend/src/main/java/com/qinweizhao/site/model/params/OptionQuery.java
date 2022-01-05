package com.qinweizhao.site.model.params;

import com.qinweizhao.site.model.enums.OptionType;
import lombok.Data;

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
