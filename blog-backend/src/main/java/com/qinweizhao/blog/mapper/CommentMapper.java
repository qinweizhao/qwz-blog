package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.projection.CommentChildrenCountProjection;
import com.qinweizhao.blog.model.projection.CommentCountProjection;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;
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
public interface CommentMapper extends BaseMapper<Comment> {


    /**
     * Count comments by post ids.
     *
     * @param postIds post id collection must not be null
     * @return a list of CommentCountProjection
     */
    List<CommentCountProjection> selectCountByPostIds(@Param("postIds") Collection<Integer> postIds);

    /**
     * Finds direct children count by comment ids.
     *
     * @param commentIds comment ids must not be null.
     * @return a list of CommentChildrenCountProjection
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
        return MyBatisUtils.buildPageResult(commentPage);
    }

    /**
     * 通过 postId 获取所有评论
     *
     * @param postId postId
     * @return List
     */
    default List<Comment> selectListByPostId(Integer postId) {
        return this.selectList(new LambdaQueryWrapperX<Comment>()
                .eq(Comment::getPostId, postId)
        );
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
                .eq(Comment::getPostId, postId)
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

}
