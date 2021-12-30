package com.qinweizhao.site.model.dto;

import com.qinweizhao.site.model.dto.base.OutputConverter;
import com.qinweizhao.site.model.entity.Attachment;
import com.qinweizhao.site.model.enums.AttachmentType;
import lombok.Data;

import java.util.Date;

/**
 * Attachment output dto.
 *
 * @author johnniang
 * @date 3/21/19
 */
@Data
public class AttachmentDTO implements OutputConverter<AttachmentDTO, Attachment> {

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

    private Date createTime;
}
