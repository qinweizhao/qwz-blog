package com.qinweizhao.blog.model.params;

import com.qinweizhao.blog.model.enums.CommentStatus;
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
