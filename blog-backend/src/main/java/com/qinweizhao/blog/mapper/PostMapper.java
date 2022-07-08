package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.enums.PostStatus;
import org.apache.ibatis.annotations.Param;


/**
 * @author qinweizhao
 * @since 2022/7/6
 */
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 计算所有帖子访问次数
     *
     * @return Long
     */
    Long countVisit();

    /**
     * 计算所有帖子喜欢
     *
     * @return Long
     */
    Long countLike();

    /**
     * 按条件查询
     *
     * @param year  year
     * @param month month
     * @param slug  slug
     * @return Post
     */
    Post selectByTimeAndSlug(@Param("year") Integer year, @Param("month") Integer month, @Param("slug") String slug);
//    default Post findBy(@Param("year") Integer year, @Param("month") Integer month, @Param("slug") String slug) {
//        return selectOne(new LambdaQueryWrapper<Post>()
//                .eq(Post::getCreateTime, year)
//                .eq(Post::getCreateTime, month)
//                .eq(Post::getSlug, slug));
//    }

    /**
     * 按条件查询
     *
     * @param year post create year
     * @param slug post slug
     * @return Post
     */
    Post selectByTimeAndSlug(@Param("year") Integer year, @Param("slug") String slug);


    /**
     * 按条件查询
     *
     * @param year   create year
     * @param month  create month
     * @param slug   slug
     * @param status status
     * @return Post
     */
    Post selectByTimeAndSlug(@Param("year") Integer year, @Param("month") Integer month, @Param("slug") String slug, @Param("status") PostStatus status);

    /**
     * 按条件查询
     *
     * @param year  create year
     * @param month create month
     * @param day   create day
     * @param slug  slug
     * @return Post
     */
    Post selectByTimeAndSlug(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day, @Param("slug") String slug);

    /**
     * 按条件查询
     *
     * @param year   create year
     * @param month  create month
     * @param day    create day
     * @param slug   slug
     * @param status status
     * @return Post
     */
    Post selectByTimeAndSlug(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day, @Param("slug") String slug, @Param("status") PostStatus status);
}
