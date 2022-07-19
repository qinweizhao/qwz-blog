package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.enums.CommentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Comment query params.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019/04/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogQueryParam extends PageParam {

    /**
     * 关键字
     */
    private String keyword;

}
