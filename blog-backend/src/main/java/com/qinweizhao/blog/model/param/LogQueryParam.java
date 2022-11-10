package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Comment query params.
 *
 * @author qinweizhao
 * @since 2019/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogQueryParam extends PageParam {

    /**
     * 关键字
     */
    private String keyword;

}
