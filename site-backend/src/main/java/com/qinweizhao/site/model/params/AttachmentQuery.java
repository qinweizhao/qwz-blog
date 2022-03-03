package com.qinweizhao.site.model.params;

import lombok.Data;
import com.qinweizhao.site.model.enums.AttachmentType;

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
