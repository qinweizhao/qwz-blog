package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Post comment service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
public interface CommentService extends IService<Comment> {

    /**
     * 统计文章个数
     *
     * @param published published
     * @return Long
     */
    long countByStatus(CommentStatus published);

    /**
     * 分页
     *
     * @param commentQueryParam commentQueryParam
     * @return Page
     */
    PageResult<CommentDTO>  pageComment(CommentQueryParam commentQueryParam);


    /**
     * 统计评论个数
     * @param postIds postIds
     * @return Map
     */
    Map<Integer, Long> countByPostIds(Set<Integer> postIds);


//
//    /**
//     * 分页
//     *
//     * @param pageable     pageable
//     * @param commentQuery commentQuery
//     * @return Page
//     */
//    Page<Comment> page(Pageable pageable, CommentQuery commentQuery);
//
//    /**
//     * 验证 CommentBlackList 状态
//     */
//    void validateCommentBlackListStatus();
//
//
//    /**
//     * 校验目标
//     *
//     * @param postId postId
//     */
//    void validateTarget(Integer postId);
//
//    /**
//     * 通过状态统计
//     *
//     * @param published published
//     * @return long
//     */
//    long countByStatus(CommentStatus published);
}
