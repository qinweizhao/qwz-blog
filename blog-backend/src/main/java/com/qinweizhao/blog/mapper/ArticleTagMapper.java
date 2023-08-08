package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.ArticleTag;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.projection.TagPostPostCountProjection;
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
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {


    /**
     * listByArticleId
     *
     * @param articleIds articleIds
     * @return List
     */
    default List<ArticleTag> listByArticleId(Collection<Integer> articleIds) {
        return this.selectList(new LambdaQueryWrapperX<ArticleTag>().in(ArticleTag::getArticleId, articleIds));
    }

    /**
     * 标签附加文章个数
     *
     * @return List
     */
    List<TagPostPostCountProjection> selectPostCount();

    /**
     * 通过标签 id 删除关联
     *
     * @param tagId tagId
     * @return boolean
     */
    default int deleteByTagId(Integer tagId) {
        return this.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tagId));
    }

    /**
     * 通过 articleId 查询 tagId 集合
     *
     * @param articleId articleId
     * @return Set
     */
    Set<Integer> selectTagIdsByarticleId(Integer articleId);

    /**
     * 查询 id 集合
     *
     * @param tagId  tagId
     * @param status status
     * @return Set
     */
    Set<Integer> selectsetArticleIdByTagIdAndPostStatus(@Param("tagId") Integer tagId, @Param("status") ArticleStatus status);

    /**
     * 通过文章 id 删除关联
     *
     * @param articleId articleId
     * @return boolean
     */
    default int deleteByarticleId(Integer articleId) {
        return this.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
    }

    /**
     * 批量删除关联
     *
     * @param articleId       articleId
     * @param removeTagIds removeTagIds
     * @return int
     */
    default int deleteBatchByarticleIdAndTagIds(Integer articleId, Collection<Integer> removeTagIds) {
        return this.delete(new LambdaQueryWrapperX<ArticleTag>().eq(ArticleTag::getArticleId, articleId).in(ArticleTag::getTagId, removeTagIds));
    }
}
