package com.qinweizhao.blog.model.dto;

import com.qinweizhao.blog.model.enums.AttachmentType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Attachment output dto.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
public class AttachmentDTO {

    private Integer id;

    private String name;

    private String path;

    private String fileKey;

    private String thumbPath;

    private String mediaType;

    private String suffix;

    private Integer width;

    private Integer height;

    private Long size;

    private AttachmentType type;

    private LocalDateTime createTime;
}
