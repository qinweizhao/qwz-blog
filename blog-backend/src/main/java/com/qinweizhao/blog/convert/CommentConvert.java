package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;

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
     * @param result result
     * @return PageResult
     */
    PageResult<CommentDTO> convertToDTO(PageResult<Comment> result);

    /**
     * convertToVO
     *
     * @param result result
     * @return PageResult
     */
    PageResult<PostCommentWithPostVO> convertToVO(PageResult<CommentDTO> result);


    /**
     * 状态转换
     *
     * @param status status
     * @return CommentStatus
     */
    default CommentStatus statusToEnum(Integer status) {
        if (status == null) {
            return null;
        }
        CommentStatus commentStatus;
        switch (status) {
            case 0:
                commentStatus = CommentStatus.PUBLISHED;
                break;
            case 1:
                commentStatus = CommentStatus.AUDITING;
                break;
            case 2:
                commentStatus = CommentStatus.RECYCLE;
                break;
            default:
                commentStatus = null;
        }
        return commentStatus;
    }

}
