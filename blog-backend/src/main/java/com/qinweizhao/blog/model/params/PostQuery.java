package com.qinweizhao.blog.model.params;

import com.qinweizhao.blog.model.enums.PostStatus;
import lombok.Data;

/**
 * Post query.
 *
 * @author johnniang
 * @date 4/10/19
 */
@Data
public class PostQuery {

    /**
     * Keyword.
     */
    private String keyword;

    /**
     * Post status.
     */
    private PostStatus status;

    /**
     * Category id.
     */
    private Integer categoryId;

}
