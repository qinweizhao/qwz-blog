package com.qinweizhao.blog.model.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Tag post count projection.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagPostPostCountProjection {

    /**
     * Post count.
     */
    private Long postCount;

    /**
     * Tag id
     */
    private Integer tagId;

}
