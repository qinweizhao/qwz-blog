package com.qinweizhao.blog.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface CommentConvert {

    CommentConvert INSTANCE = Mappers.getMapper(CommentConvert.class);


//    /**
//     * Converts to with post vo
//     *
//     * @param comment comment
//     * @return a comment with post vo
//     */
//    PostCommentWithPostVO convertToWithPostVo(Comment comment);
//
//    /**
//     * Converts to with post vo
//     *
//     * @param postComments comment list
//     * @return a list of comment with post vo
//     */
//    List<PostCommentWithPostVO> convertToWithPostVo(List<Comment> postComments);

//    /**
//     * convertToWithPostVo
//     * @param commentPage commentPage
//     * @return Page
//     */
//    Page<PostCommentWithPostVO> convertToWithPostVo(Page<Comment> commentPage);
}
