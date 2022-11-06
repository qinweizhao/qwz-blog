package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.enums.CommentType;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * PostComment param.
 *
 * @author qinweizhao
 * @since 3/22/19
 */
@Data
public class CommentParam {
    @NotBlank(message = "评论者昵称不能为空")
    @Size(max = 50, message = "评论者昵称的字符长度不能超过 {max}")
    private String author;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 255, message = "邮箱的字符长度不能超过 {max}")
    private String email;

    @Size(max = 255, message = "评论者博客链接的字符长度不能超过 {max}")
    @URL(message = "博客链接格式不正确")
    private String authorUrl;

    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1023, message = "评论内容的字符长度不能超过 {max}")
    private String content;

    @Min(value = 1, message = "文章编号不能小于 {value}")
    private Integer targetId;


    @Min(value = 0, message = "PostComment parent id must not be less than {value}")
    private Long parentId = 0L;

    private Boolean allowNotification = true;

    /**
     * 类型（前端不用传）
     */
    private CommentType type;


}
