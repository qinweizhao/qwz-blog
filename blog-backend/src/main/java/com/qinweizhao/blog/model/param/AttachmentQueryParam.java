package com.qinweizhao.blog.model.param;

import com.qinweizhao.blog.model.core.PageParam;
import com.qinweizhao.blog.model.enums.AttachmentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Attachment query params.
 *
 * @author ryanwang
 * @since 2019/04/18
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
