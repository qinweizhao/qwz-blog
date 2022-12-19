package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.enums.AttachmentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Attachment query params.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AttachmentQueryParam extends PageParam {

    /**
     * Keyword.
     */
    private String keyword;

    private String mediaType;

    private AttachmentType attachmentType;
}
