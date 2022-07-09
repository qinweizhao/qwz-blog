package com.qinweizhao.blog.model.dto.post;

import com.qinweizhao.blog.model.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.qinweizhao.blog.model.dto.base.OutputConverter;
import com.qinweizhao.blog.model.enums.PostEditorType;
import com.qinweizhao.blog.model.enums.PostStatus;

import java.util.Date;

/**
 * Base post minimal output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
@Data
@ToString
@EqualsAndHashCode
public class BasePostMinimalDTO implements OutputConverter<BasePostMinimalDTO, Post> {

    private Integer id;

    private String title;

    private PostStatus status;

    private String slug;

    private PostEditorType editorType;

    private Date updateTime;

    private Date createTime;

    private Date editTime;

    private String metaKeywords;

    private String metaDescription;

    private String fullPath;
}
