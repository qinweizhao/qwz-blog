package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Comment query params.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentQueryParam extends PageParam {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 评论状态
     */
    private CommentStatus status;

    /**
     * 类型（前端不用传）
     */
    private CommentType type;

}
