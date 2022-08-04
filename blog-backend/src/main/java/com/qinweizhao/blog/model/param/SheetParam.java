package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.enums.PostStatus;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Sheet param.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-4-24
 */
@Data
public class SheetParam {

    @NotBlank(message = "页面标题不能为空")
    @Size(max = 100, message = "页面标题的字符长度不能超过 {max}")
    private String title;

    private PostStatus status = PostStatus.DRAFT;

    @Size(max = 255, message = "页面别名的字符长度不能超过 {max}")
    private String slug;

    private String originalContent;

    private String summary;

    @Size(max = 255, message = "封面图链接的字符长度不能超过 {max}")
    private String thumbnail;

    private Boolean disallowComment = false;

    @Size(max = 255, message = "页面密码的字符长度不能超过 {max}")
    private String password;

    @Size(max = 255, message = "Length of template must not be more than {max}")
    private String template;

    @Min(value = 0, message = "Post top priority must not be less than {value}")
    private Integer topPriority = 0;

    private LocalDateTime createTime;

    private String metaKeywords;

    private String metaDescription;

//    private Set<SheetMetaParam> metas;


}
