package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.enums.CommentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Comment query params.
 *
 * @author ryanwang
 * @date 2019/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQueryParam extends PageParam {

    /**
     * Keyword.
     */
    private String keyword;

    /**
     * Comment status.
     */
    private CommentStatus status;
}
