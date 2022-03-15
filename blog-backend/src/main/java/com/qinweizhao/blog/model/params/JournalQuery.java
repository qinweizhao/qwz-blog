package com.qinweizhao.blog.model.params;

import lombok.Data;
import com.qinweizhao.blog.model.enums.JournalType;

/**
 * Journal query params.
 *
 * @author ryanwang
 * @date 2019/04/26
 */
@Data
public class JournalQuery {

    /**
     * Keyword.
     */
    private String keyword;

    private JournalType type;
}
