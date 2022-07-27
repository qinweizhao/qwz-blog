package com.qinweizhao.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Tag with post count output dto.
 *
 * @author johnniang
 * @date 3/20/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TagWithPostCountDTO extends TagDTO {

    private Long postCount;

}
