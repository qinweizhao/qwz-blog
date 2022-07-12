package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.CommentDTO;
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
@EqualsAndHashCode(callSuper = true)
public class PostCommentWithPostVO extends CommentDTO {

    private BasePostMinimalDTO post;
}
