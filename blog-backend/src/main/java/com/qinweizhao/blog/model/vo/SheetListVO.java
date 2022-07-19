package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.post.PostSimpleDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Sheet list dto.
 *
 * @author johnniang
 * @date 19-4-24
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SheetListVO extends PostSimpleDTO {

    private Long commentCount;
}
