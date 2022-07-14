package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.convert.PostConvert;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.utils.MyBatisUtils;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author qinweizhao
 * @since 2022/7/6
 */
public interface PostMapper extends BaseMapper<Post> {


    /**
     * 按条件查询
     *
     * @param year  year
     * @param month month
     * @param slug  slug
     * @return Post
     */
    Post selectByTimeAndSlug(@Param("year") Integer year, @Param("month") Integer month, @Param("slug") String slug);

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


    /**
     * 统计文章个数
     *
     * @param published published
     * @return Long
     */
    default long selectCountByStatus(PostStatus published) {
        return this.selectCount(new LambdaQueryWrapper<Post>()
                .eq(Post::getStatus, published.getValue())
        );
    }

    /**
     * 统计所有文章的阅读次数
     *
     * @return long
     */
    long selectCountVisits();


    /**
     * 统计 like
     *
     * @return Long
     */
    long selectCountLikes();


    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Post> selectPagePosts(PostQueryParam param) {
        // 未来拓展下 MybatisPlus 直接使用关联查询，或者使用其他方案。
        Page<Post> page = MyBatisUtils.buildPage(param);

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("keyword", param.getKeyword());
        paramMap.put("categoryId", param.getCategoryId());
        paramMap.put("status", PostConvert.INSTANCE.statusToInteger(param.getStatus()));

        Page<Post> postPage = this.selectPagePosts(page, paramMap);
        return MyBatisUtils.buildPageResult(postPage);
    }

    /**
     * 分页(关联查询)
     *
     * @param page  page
     * @param param param
     * @return PageResult
     */
    Page<Post> selectPagePosts(Page<Post> page, @Param("param") Map<String, Object> param);

}
