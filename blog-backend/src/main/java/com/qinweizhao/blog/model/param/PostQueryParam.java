package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.enums.PostStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Post query.
 *
 * @author johnniang
 * @author qinweizhao
 * @date 4/10/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostQueryParam extends PageParam {

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
