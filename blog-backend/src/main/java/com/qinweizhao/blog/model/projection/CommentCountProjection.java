package com.qinweizhao.blog.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PostComment count projection
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCountProjection {

    private Long count;

    private Integer targetId;
}
