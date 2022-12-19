package com.qinweizhao.blog.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Comment children count projection.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentChildrenCountProjection {

    private Long directChildrenCount;

    private Long commentId;
}
