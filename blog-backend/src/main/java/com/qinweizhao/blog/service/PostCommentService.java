package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;
import com.qinweizhao.blog.service.base.BaseCommentService;

import java.util.List;

/**
 * Post comment service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
public interface PostCommentService extends IService<Comment> {

    /**
     * Converts to with post vo.
     *
     * @param commentPage comment page must not be null
     * @return a page of comment with post vo
     */
    @NonNull
    Page<PostCommentWithPostVO> convertToWithPostVo(@NonNull Page<Comment> commentPage);

    /**
     * Converts to with post vo
     *
     * @param comment comment
     * @return a comment with post vo
     */
    @NonNull
    PostCommentWithPostVO convertToWithPostVo(@NonNull Comment comment);

    /**
     * Converts to with post vo
     *
     * @param postComments comment list
     * @return a list of comment with post vo
     */
    @NonNull
    List<PostCommentWithPostVO> convertToWithPostVo(@Nullable List<Comment> postComments);

    /**
     * Validate CommentBlackList Status
     */
    void validateCommentBlackListStatus();
}
