package com.qinweizhao.blog.model.params;

import lombok.Data;
import com.qinweizhao.blog.model.enums.AttachmentType;

/**
 * Attachment query params.
 *
 * @author ryanwang
 * @date 2019/04/18
 */
@Data
public class AttachmentQuery {

    /**
     * Keyword.
     */
    private String keyword;

    private String mediaType;

    private AttachmentType attachmentType;
}
