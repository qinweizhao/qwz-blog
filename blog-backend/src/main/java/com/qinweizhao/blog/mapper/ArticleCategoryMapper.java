package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.ArticleCategory;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.projection.CategoryPostCountProjection;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
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
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategory> {

    /**
     * 通过 articleId 集合查询关联
     *
     * @param articleIds articleIds
     * @return List
     */
    default List<ArticleCategory> selectlistByArticleIds(Collection<Integer> articleIds) {
        return this.selectList(new LambdaQueryWrapper<ArticleCategory>().in(ArticleCategory::getArticleId, articleIds));
    }

    /**
     * 每个分类包含多少文章数量
     *
     * @return List
     */
    List<CategoryPostCountProjection> selectPostCount();


    /**
     * 通过分类 Id 删除关联
     *
     * @param categoryId categoryId
     * @return int
     */
    default int deleteByCategoryId(Integer categoryId) {
        return this.delete(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getCategoryId, categoryId));
    }

    /**
     * 查询分类 id 集合
     *
     * @param articleId articleId
     * @return Set
     */
    Set<Integer> selectSetCategoryIdsByarticleId(Integer articleId);


    /**
     * 查询 id 集合
     *
     * @param categoryId categoryId
     * @param status     status
     * @return Set
     */
    Set<Integer> selectsetArticleIdByCategoryIdAndPostStatus(@Param("categoryId") Integer categoryId, @Param("status") ArticleStatus status);

    /**
     * 通过文章 Id 删除关联
     *
     * @param articleId articleId
     * @return int
     */
    default int deleteByarticleId(Integer articleId) {
        return this.delete(new LambdaQueryWrapper<ArticleCategory>().eq(ArticleCategory::getArticleId, articleId));
    }

    /**
     * 批量删除关联
     *
     * @param articleId            articleId
     * @param removeCategoryIds removeCategoryIds
     * @return int
     */
    default int deleteBatchByarticleIdAndTagIds(Integer articleId, Collection<Integer> removeCategoryIds) {
        return this.delete(new LambdaQueryWrapperX<ArticleCategory>().eq(ArticleCategory::getArticleId, articleId).in(ArticleCategory::getCategoryId, removeCategoryIds));
    }
}
