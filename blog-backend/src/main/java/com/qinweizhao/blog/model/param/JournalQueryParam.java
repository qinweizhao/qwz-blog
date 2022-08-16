package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.enums.JournalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Journal query params.
 *
 * @author ryanwang
 * @since 2019/04/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JournalQueryParam extends PageParam {

    /**
     * Keyword.
     */
    private String keyword;

    private JournalType type;
}
