package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.param.CommentParam;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface CommentConvert {

    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);


    /**
     * convertToDTO
     *
     * @param comments comments
     * @return List
     */
    List<CommentDTO> convertToDTO(List<Comment> comments);

    /**
     * convertToVO
     *
     * @param comment comment
     * @return PageResult
     */
    PostCommentWithPostVO convertPostToVO(CommentDTO comment);


    /**
     * 状态转换
     *
     * @param status status
     * @return CommentStatus
     */
    default CommentStatus statusToEnum(Integer status) {
        return ValueEnum.valueToEnum(CommentStatus.class, status);
    }

    /**
     * convert
     *
     * @param comment comment
     * @return CommentDTO
     */
    CommentDTO convert(Comment comment);

    /**
     * convert
     *
     * @param commentParam commentParam
     * @return Comment
     */
    @Mapping(target = "type", ignore = true)
    Comment convert(CommentParam commentParam);


    /**
     * 状态转换
     *
     * @param status status
     * @return CommentStatus
     */
    default Integer statusToInteger(CommentStatus status) {
        if (status == null) {
            return null;
        }
        Integer commentStatus;
        switch (status) {
            case PUBLISHED:
                commentStatus = 0;
                break;
            case AUDITING:
                commentStatus = 1;
                break;
            case RECYCLE:
                commentStatus = 2;
                break;
            default:
                commentStatus = null;
        }
        return commentStatus;
    }
}
