package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.projection.CommentChildrenCountProjection;
import com.qinweizhao.blog.model.projection.CommentCountProjection;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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
                .eq(Comment::getStatus, published)
        );
    }
}
