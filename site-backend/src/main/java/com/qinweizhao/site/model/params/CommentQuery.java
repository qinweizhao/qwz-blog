package com.qinweizhao.site.model.params;

import com.qinweizhao.site.model.enums.CommentStatus;
import lombok.Data;

/**
 * Comment query params.
 *
 * @author ryanwang
 * @date 2019/04/18
 */
@Data
public class CommentQuery {

    /**
     * Keyword.
     */
    private String keyword;

    /**
     * Comment status.
     */
    private CommentStatus status;
}
