package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.ArticleTag;
import com.qinweizhao.blog.model.enums.ArticleStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Post tag service interface.
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
public interface ArticleTagService extends IService<ArticleTag> {

    /**
     * 按帖子 ID 列出标签列表映射
     *
     * @param articleIds post id collection
     * @return tag map (key: articleId, value: a list of tags)
     */
    Map<Integer, List<TagDTO>> listTagListMapBy(Collection<Integer> articleIds);

    /**
     * 删除关联
     *
     * @param tagId tagId
     * @return boolean
     */
    boolean removeByTagId(Integer tagId);

    /**
     * 查询标签集合
     *
     * @param articleId articleId
     * @return List
     */
    List<TagDTO> listTagsByarticleId(Integer articleId);

    /**
     * 列表
     *
     * @param tagId  tagId
     * @param status published
     * @return List
     */
    List<ArticleSimpleDTO> listPostsByTagIdAndPostStatus(Integer tagId, ArticleStatus status);

    /**
     * 列表
     *
     * @param tagSlug tagSlug
     * @param status  published
     * @return List
     */
    List<ArticleSimpleDTO> listPostsByTagSlugAndPostStatus(String tagSlug, ArticleStatus status);

}