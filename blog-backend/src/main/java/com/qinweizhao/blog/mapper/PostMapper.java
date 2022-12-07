package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.convert.PostConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author qinweizhao
 * @since 2022/7/6
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {

    /**
     * 统计文章个数
     *
     * @param published published
     * @return Long
     */
    default long selectCountByStatus(PostStatus published) {
        return this.selectCount(new LambdaQueryWrapper<Post>().eq(Post::getStatus, published.getValue()));
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
    default PageResult<Post> selectPage(PostQueryParam param) {
        IPage<Post> page = MyBatisUtils.buildPage(param);

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("keyword", param.getKeyword());
        paramMap.put("categoryId", param.getCategoryId());
        paramMap.put("tagId", param.getTagId());
        Integer status = PostConvert.INSTANCE.statusToInteger(param.getStatus());
        paramMap.put("status", status);
        // 不显示的状态
        if (ObjectUtils.isEmpty(status)) {
            paramMap.put("excludeStatus", PostStatus.RECYCLE.getValue());
        }

        Page<Post> postPage = this.selectPagePosts(page, paramMap);
        return MyBatisUtils.buildPageResult(postPage);
    }

    /**
     * 分页（不需要关联分类）
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Post> selectPageSimple(PostQueryParam param) {
        IPage<Post> page = MyBatisUtils.buildPage(param);

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("keyword", param.getKeyword());
        paramMap.put("status", String.valueOf(PostConvert.INSTANCE.statusToInteger(param.getStatus())));
        Page<Post> postPage = this.selectPageSimplePosts(page, paramMap);
        return MyBatisUtils.buildPageResult(postPage);
    }

    /**
     * 分页(关联查询)
     *
     * @param page  page
     * @param param param
     * @return PageResult
     */
    Page<Post> selectPagePosts(IPage<Post> page, @Param("param") Map<String, Object> param);

    /**
     * 分页(关联查询)
     *
     * @param page  page
     * @param param param
     * @return PageResult
     */
    @Deprecated
    Page<Post> selectPageSimplePosts(IPage<Post> page, @Param("param") Map<String, Object> param);

    /**
     * 更新帖子状态
     *
     * @param status status
     * @param postId postId
     * @return int
     */
    default int updateStatusById(int status, Integer postId) {
        Post post = new Post();
        post.setId(postId);
        post.setStatus(status);
        return this.updateById(post);
    }


    /**
     * 查询列表
     *
     * @param postIds postIds
     * @return List
     */
    default List<Post> selectListByIds(Set<Integer> postIds) {
        return selectList(new LambdaQueryWrapperX<Post>().inIfPresent(Post::getId, postIds));
    }

    /**
     * 通过状态查询发布
     *
     * @param status status
     * @return List
     */
    default List<Post> selectListByStatus(PostStatus status) {
        return this.selectList(new LambdaQueryWrapperX<Post>().eq(Post::getStatus, status).orderByDesc(Post::getCreateTime));
    }

    /**
     * 查询列表(最新的已经发布的)
     *
     * @param top top
     * @return List
     */
    default List<Post> selectListLatest(int top) {
        return this.selectList(new LambdaQueryWrapper<Post>().orderByDesc(Post::getCreateTime).eq(Post::getStatus, PostStatus.PUBLISHED).last("limit " + top));
    }

    /**
     * 获取文章状态
     *
     * @param postId postId
     * @return PostStatus
     */
    PostStatus selectStatusById(Integer postId);

    /**
     * 查询下一个文章 id
     *
     * @param postId postId
     * @param status status
     * @return Integer
     */
    Integer selectNextIdByIdAndStatus(@Param("postId") Integer postId, @Param("status") PostStatus status);

    /**
     * 查询上一个文章 id
     *
     * @param postId postId
     * @param status status
     * @return Integer
     */
    Integer selectPrevIdByIdAndStatus(@Param("postId") Integer postId, @Param("status") PostStatus status);

    /**
     * 查询 id
     *
     * @param slug slug
     * @return Integer
     */
    Integer selectIdBySlug(@Param("slug") String slug);

}
