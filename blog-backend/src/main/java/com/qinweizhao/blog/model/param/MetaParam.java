package com.qinweizhao.blog.model.param;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Post meta param.
 *
 * @author ryanwang
 * @author ikaisec
 * @since 2019-08-04
 */
@Data
public class MetaParam {

    @NotBlank(message = "文章 id 不能为空")
    private Integer postId;

    @NotBlank(message = "Meta key 不能为空")
    @Size(max = 1023, message = "Meta key 的字符长度不能超过 {max}")
    private String key;

    @NotBlank(message = "Meta value 不能为空")
    @Size(max = 1023, message = "Meta value 的字符长度不能超过 {max}")
    private String value;

}
