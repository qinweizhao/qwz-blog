package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Comment query params.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019/04/18
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
