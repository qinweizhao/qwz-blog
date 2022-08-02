package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.base.PageParam;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.params.PostCommentParam;
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
public interface CommentService {

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
    PageResult<CommentDTO> pageComment(CommentQueryParam commentQueryParam);


    /**
     * 统计评论个数
     *
     * @param postIds postIds
     * @return Map
     */
    Map<Integer, Long> countByPostIds(Set<Integer> postIds);

    /**
     * 分页（树）
     *
     * @param postId postId
     * @param param  param
     * @return PageResult
     */
    PageResult<CommentDTO> pageTree(Integer postId, PageParam param);

    /**
     * 统计个数
     * @return Long
     */
    Long count();

    /**
     * 构建返回的 VO
     *
     * @param contents contents
     * @return List
     */
    List<PostCommentWithPostVO> buildResultVO(List<CommentDTO> contents);

    /**
     * 构建返回结果
     * @param commentResult commentResult
     * @return PageResult
     */
    PageResult<PostCommentWithPostVO> buildPageResultVO(PageResult<CommentDTO> commentResult);

    /**
     * 更新评论状态
     * @param commentId commentId
     * @param status status
     * @return boolean
     */
    boolean updateStatus(Long commentId, CommentStatus status);


    /**
     * 新增
     * @param postCommentParam postCommentParam
     * @return boolean
     */
    boolean save(PostCommentParam postCommentParam);


    /**
     * 删除评论（+子）
     * @param commentId commentId
     * @return boolean
     */
    boolean removeById(Long commentId);

    /**
     * 批量修改状态
     * @param ids ids
     * @param status status
     * @return boolean
     */
    boolean updateStatusByIds(List<Long> ids, CommentStatus status);


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
