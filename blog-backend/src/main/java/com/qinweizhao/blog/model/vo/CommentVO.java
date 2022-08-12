package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Base comment vo.
 *
 * @author johnniang
 * @since 19-4-24
 */
@Data
public class CommentVO  {
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

    List<CommentVO> children;
}
