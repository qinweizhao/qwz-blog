package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.convert.ArticleConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Article;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
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
 * @since 2022-03-15
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 统计文章个数
     *
     * @param published published
     * @return Long
     */
    default long selectCountByStatus(ArticleStatus published) {
        return this.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getStatus, published.getValue()));
    }

    /**
     * 统计所有文章的阅读次数
     *
     * @return long
     */
    long selectCountVisits();

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Article> selectPage(ArticleQueryParam param) {
        IPage<Article> page = MyBatisUtils.buildPage(param);

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("keyword", param.getKeyword());
        paramMap.put("categoryId", param.getCategoryId());
        paramMap.put("tagId", param.getTagId());
        Integer status = ArticleConvert.INSTANCE.statusToInteger(param.getStatus());
        paramMap.put("status", status);
        // 不显示的状态
        if (ObjectUtils.isEmpty(status)) {
            paramMap.put("excludeStatus", ArticleStatus.RECYCLE.getValue());
        }

        Page<Article> postPage = this.selectPagePosts(page, paramMap);
        return MyBatisUtils.buildPageResult(postPage);
    }

    /**
     * 分页（不需要关联分类）
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Article> selectPageSimple(ArticleQueryParam param) {
        IPage<Article> page = MyBatisUtils.buildPage(param);

        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("keyword", param.getKeyword());
        paramMap.put("status", String.valueOf(ArticleConvert.INSTANCE.statusToInteger(param.getStatus())));
        Page<Article> postPage = this.selectPageSimplePosts(page, paramMap);
        return MyBatisUtils.buildPageResult(postPage);
    }

    /**
     * 分页(关联查询)
     *
     * @param page  page
     * @param param param
     * @return PageResult
     */
    Page<Article> selectPagePosts(IPage<Article> page, @Param("param") Map<String, Object> param);

    /**
     * 分页(关联查询)
     *
     * @param page  page
     * @param param param
     * @return PageResult
     */
    @Deprecated
    Page<Article> selectPageSimplePosts(IPage<Article> page, @Param("param") Map<String, Object> param);

    /**
     * 更新帖子状态
     *
     * @param status status
     * @param articleId articleId
     * @return int
     */
    default int updateStatusById(int status, Integer articleId) {
        Article article = new Article();
        article.setId(articleId);
        article.setStatus(status);
        return this.updateById(article);
    }


    /**
     * 查询列表
     *
     * @param articleIds articleIds
     * @return List
     */
    default List<Article> selectListByIds(Set<Integer> articleIds) {
        return selectList(new LambdaQueryWrapperX<Article>().inIfPresent(Article::getId, articleIds));
    }

    /**
     * 通过状态查询发布
     *
     * @param status status
     * @return List
     */
    default List<Article> selectListByStatus(ArticleStatus status) {
        return this.selectList(new LambdaQueryWrapperX<Article>().eq(Article::getStatus, status).orderByDesc(Article::getCreateTime));
    }

    /**
     * 查询列表(最新的已经发布的)
     *
     * @param top top
     * @return List
     */
    default List<Article> selectListLatest(int top) {
        return this.selectList(new LambdaQueryWrapper<Article>().orderByDesc(Article::getUpdateTime).eq(Article::getStatus, ArticleStatus.PUBLISHED).last("limit " + top));
    }

    /**
     * 获取文章状态
     *
     * @param articleId articleId
     * @return PostStatus
     */
    ArticleStatus selectStatusById(Integer articleId);

    /**
     * 查询下一个文章 id
     *
     * @param articleId articleId
     * @param status status
     * @return Integer
     */
    Integer selectNextIdByIdAndStatus(@Param("articleId") Integer articleId, @Param("status") ArticleStatus status);

    /**
     * 查询上一个文章 id
     *
     * @param articleId articleId
     * @param status status
     * @return Integer
     */
    Integer selectPrevIdByIdAndStatus(@Param("articleId") Integer articleId, @Param("status") ArticleStatus status);

    /**
     * 查询 id
     *
     * @param slug slug
     * @return Integer
     */
    Integer selectIdBySlug(@Param("slug") String slug);

}
