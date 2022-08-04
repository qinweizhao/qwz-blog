package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.core.BaseTree;
import com.qinweizhao.blog.model.enums.CommentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Base comment output dto.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentDTO extends BaseTree<CommentDTO> {

    private Long id;

    private Integer postId;

    private String author;

    private String email;

    private String ipAddress;

    private String authorUrl;

    private String gravatarMd5;

    private String content;

    private CommentStatus status;

    private String userAgent;

    private Long parentId;

    private Boolean isAdmin;

    private Boolean allowNotification;

    private LocalDateTime createTime;

}
