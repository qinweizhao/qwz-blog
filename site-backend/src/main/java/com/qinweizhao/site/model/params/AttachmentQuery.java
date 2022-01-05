package com.qinweizhao.site.model.params;

import com.qinweizhao.site.model.enums.AttachmentType;
import lombok.Data;

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
