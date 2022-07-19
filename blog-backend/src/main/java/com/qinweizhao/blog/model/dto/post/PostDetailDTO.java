package com.qinweizhao.blog.model.dto.post;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Base post detail output dto.
 *
 * @author johnniang
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class PostDetailDTO extends PostSimpleDTO {

    private String originalContent;

    private String formatContent;

    private Long commentCount;
}
