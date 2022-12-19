package com.qinweizhao.blog.model.projection;

import lombok.Data;

/**
 * Category post count projection.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
public class CategoryPostCountProjection {

    private Long postCount;

    private Integer categoryId;
}
