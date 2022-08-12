package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.CommentDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Base comment with parent comment vo.
 *
 * @author johnniang
 * @since 3/31/19
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class CommentWithParentVO extends CommentDTO implements Cloneable {

    /**
     * Parent comment.
     */
    private CommentWithParentVO parent;

    @Override
    public CommentWithParentVO clone() {
        try {
            return (CommentWithParentVO) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Clone not support exception", e);
            return null;
        }
    }
}
