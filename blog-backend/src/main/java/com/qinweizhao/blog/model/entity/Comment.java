package com.qinweizhao.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.qinweizhao.blog.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer type;

    private Boolean allowNotification;

    private String author;

    private String authorUrl;

    private String content;

    private String email;

    private String gravatarMd5;

    private String ipAddress;

    private Boolean isAdmin;

    private Long parentId;

    private Integer postId;

    private Integer status;

    private Integer topPriority;

    private String userAgent;

}
