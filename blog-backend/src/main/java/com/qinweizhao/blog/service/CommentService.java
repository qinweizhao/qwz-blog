package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentParam;
import com.qinweizhao.blog.model.param.CommentQueryParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Post comment service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-14
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
     * @param param param
     * @return Page
     */
    PageResult<CommentDTO> pageComment(CommentQueryParam param);


    /**
     * 统计评论个数
     *
     * @param status      status
     * @param type      type
     * @param targetIds postIds
     * @return Map
     */
    Map<Integer, Long> countBy(CommentStatus status, CommentType type, Set<Integer> targetIds);

    /**
     * 分页（树）
     *
     * @param targetId postId/journalId
     * @param param    param
     * @return PageResult
     */
    PageResult<CommentDTO> pageTree(Integer targetId, CommentQueryParam param);

    /**
     * 统计个数
     *
     * @return Long
     */
    Long count();

    /**
     * 更新评论状态
     *
     * @param commentId commentId
     * @param status    status
     * @return boolean
     */
    boolean updateStatus(Long commentId, CommentStatus status);


    /**
     * 新增
     *
     * @param commentParam postCommentParam
     * @return boolean
     */
    boolean save(CommentParam commentParam);


    /**
     * 删除评论（+子）
     *
     * @param commentId commentId
     * @return boolean
     */
    boolean removeById(Long commentId);

    /**
     * 批量修改状态
     *
     * @param ids    ids
     * @param status status
     * @return boolean
     */
    boolean updateStatusByIds(List<Long> ids, CommentStatus status);


    /**
     * 验证 CommentBlackList 状态
     */
    void validateCommentBlackListStatus();


    /**
     * 校验目标
     *
     * @param postId postId
     * @param type   type
     */
    void validateTarget(Integer postId, CommentType type);

    /**
     * 批量删除
     *
     * @param ids ids
     * @return boolean
     */
    boolean removeByIds(List<Long> ids);

    /**
     * 最新的数据
     *
     * @param param param
     * @return List
     */
    List<CommentDTO> listLatest(CommentQueryParam param);

    /**
     * 获取评论
     *
     * @param commentId commentId
     * @return CommentDTO
     */
    CommentDTO getById(Long commentId);

}
