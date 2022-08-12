package com.qinweizhao.blog.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category post count projection.
 *
 * @author johnniang
 * @since 19-4-23
 */
@Data
public class CategoryPostCountProjection {

    private Long postCount;

    private Integer categoryId;
}
