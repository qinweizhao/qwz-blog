package com.qinweizhao.blog.model.params;

import com.qinweizhao.blog.model.core.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Photo query params.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019/04/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PhotoQueryParam extends PageParam {

    /**
     * Keyword.
     */
    private String keyword;

    private String team;
}
