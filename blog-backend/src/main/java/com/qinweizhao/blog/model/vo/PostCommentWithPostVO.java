package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.BaseCommentDTO;
import com.qinweizhao.blog.model.dto.post.BasePostMinimalDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * PostComment list with post vo.
 *
 * @author johnniang
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class PostCommentWithPostVO extends BaseCommentDTO {

    private BasePostMinimalDTO post;
}
