package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.projection.CommentChildrenCountProjection;
import com.qinweizhao.blog.model.projection.CommentCountProjection;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 统计评论个数
     *
     * @param status    status
     * @param type      type
     * @param targetIds targetIds
     * @return List
     */
    List<CommentCountProjection> selectListCommentCountProjection(@Param("status") CommentStatus status, @Param("type") CommentType type, @Param("targetIds") Collection<Integer> targetIds);

    /**
     * 按评论 id 查找直接子评论。
     *
     * @param commentIds commentIds
     * @return List
     */
    List<CommentChildrenCountProjection> selectCountDirectChildren(@Param("commentIds") Collection<Long> commentIds);

    /**
     * 根据时间范围和 IP 地址统计评论次数
     *
     * @param ipAddress IP地址
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 评论次数
     */
    default long countByIpAndTime(String ipAddress, Date startTime, Date endTime) {
        return selectCount(new LambdaQueryWrapper<Comment>().eq(Comment::getIpAddress, ipAddress)
                .between(Comment::getUpdateTime, startTime, endTime));
    }

    /**
     * 根据状态统计个数
     *
     * @param published published
     * @return long
     */
    default long selectCountByStatus(CommentStatus published) {
        return selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getStatus, published.getValue())
        );
    }

    /**
     * 分页
     *
     * @param param param
     * @return Page
     */
    default PageResult<Comment> selectPageComments(CommentQueryParam param) {
        Page<Comment> page = MyBatisUtils.buildPage(param);
        Page<Comment> commentPage = this.selectPage(page, new LambdaQueryWrapperX<Comment>()
                .eq(Comment::getType, param.getType())
                .eqIfPresent(Comment::getStatus, param.getStatus())
                .likeIfPresent(Comment::getAuthor, param.getKeyword())
                .likeIfPresent(Comment::getContent, param.getKeyword())
                .likeIfPresent(Comment::getEmail, param.getKeyword())
        );
        return MyBatisUtils.buildSimplePageResult(commentPage);
    }

    /**
     * 更新评论状态
     *
     * @param commentId commentId
     * @param status    status
     * @return boolean
     */
    boolean updateStatusById(@Param("commentId") Long commentId, @Param("status") CommentStatus status);

    /**
     * 查询列表
     *
     * @param postId          postId
     * @param commentParentId commentParentId
     * @return List
     */
    default List<Comment> selectListByPostIdAndParentId(Integer postId, Long commentParentId) {
        return this.selectList(new LambdaQueryWrapperX<Comment>()
                .eq(Comment::getTargetId, postId)
                .eq(Comment::getParentId, commentParentId)
        );
    }

    /**
     * 查询列表
     *
     * @param commentIds commentIds
     * @return List
     */
    default List<Comment> selectListByParentIds(Set<Long> commentIds) {
        return this.selectList(new LambdaQueryWrapperX<Comment>()
                .in(Comment::getParentId, commentIds)
        );
    }

    /**
     * 通过文章 id 删除评论
     *
     * @param postId postId
     * @return List
     */
    default int deleteByPostId(Integer postId) {
        return this.delete(new LambdaQueryWrapperX<Comment>()
                .eq(Comment::getTargetId, postId)
        );
    }

    /**
     * 统计个数
     *
     * @param postId postId
     * @param status status
     * @return Long
     */
    default Long selectCountByPostIdAndStatus(Integer postId, CommentStatus status) {
        return this.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getTargetId, postId)
                .eq(Comment::getStatus, status)
        );
    }

    /**
     * 查询评论列表
     *
     * @param param    param
     * @param targetId targetId
     * @return List
     */
    default List<Comment> selectListComment(CommentQueryParam param, Integer targetId) {
        return this.selectList(new LambdaQueryWrapperX<Comment>()
                .eqIfPresent(Comment::getTargetId, targetId)
                .eqIfPresent(Comment::getType, param.getType())
                .eqIfPresent(Comment::getStatus, param.getStatus())
        );
    }

}
