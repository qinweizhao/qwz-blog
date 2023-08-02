package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Post query.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryParam extends PageParam {

    /**
     * Keyword.
     */
    private String keyword;

    /**
     * Post status.
     */
    private ArticleStatus status;

    /**
     * Category id.
     */
    private Integer categoryId;

    /**
     * Tag id.
     */
    private Integer tagId;

}
