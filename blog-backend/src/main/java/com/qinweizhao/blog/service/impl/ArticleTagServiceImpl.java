package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.ArticleMapper;
import com.qinweizhao.blog.mapper.ArticleTagMapper;
import com.qinweizhao.blog.mapper.TagMapper;
import com.qinweizhao.blog.model.convert.ArticleConvert;
import com.qinweizhao.blog.model.convert.TagConvert;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Article;
import com.qinweizhao.blog.model.entity.ArticleTag;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleTagService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Post tag service implementation.
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
@Service
@AllArgsConstructor
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {


    private final TagMapper tagMapper;

    private final ArticleMapper articleMapper;

    private final ArticleTagMapper articleTagMapper;

    private final SettingService settingService;


    @Override
    public Map<Integer, List<TagDTO>> listTagListMapBy(Collection<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }

        // 查找所有帖子标签
        List<ArticleTag> articleTags = articleTagMapper.listByPostId(postIds);

        // Fetch tag ids
        Set<Integer> tagIds = ServiceUtils.fetchProperty(articleTags, ArticleTag::getTagId);

        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyMap();
        }

        List<TagDTO> tags = TagConvert.INSTANCE.convertToDTO(tagMapper.selectBatchIds(tagIds));

        Map<Integer, TagDTO> tagMap = ServiceUtils.convertToMap(tags, TagDTO::getId);

        Map<Integer, List<TagDTO>> tagListMap = new LinkedHashMap<>();

        articleTags.forEach(articleTag -> tagListMap.computeIfAbsent(articleTag.getArticleId(), postId -> new LinkedList<>()).add(tagMap.get(articleTag.getTagId())));

        return tagListMap;
    }

    @Override
    public boolean removeByTagId(Integer tagId) {
        return articleTagMapper.deleteByTagId(tagId) > 0;
    }

    @Override
    public List<TagDTO> listTagsByPostId(Integer postId) {
        Set<Integer> tagIds = articleTagMapper.selectTagIdsByPostId(postId);
        List<Tag> tags = tagMapper.selectListByIds(tagIds);
        return TagConvert.INSTANCE.convertToDTO(tags);
    }

    @Override
    public List<ArticleSimpleDTO> listPostsByTagIdAndPostStatus(Integer tagId, ArticleStatus status) {

        Set<Integer> postIds = articleTagMapper.selectSetPostIdByTagIdAndPostStatus(tagId, status);

        List<Article> articles = articleMapper.selectListByIds(postIds);
        List<ArticleSimpleDTO> result = ArticleConvert.INSTANCE.convertToSimpleDTO(articles);
        result.forEach(item -> item.setFullPath(settingService.buildFullPath(item.getId())));
        return result;
    }

    @Override
    public List<ArticleSimpleDTO> listPostsByTagSlugAndPostStatus(String tagSlug, ArticleStatus status) {
        Tag tag = tagMapper.selectBySlug(tagSlug);
        Set<Integer> postIds = articleTagMapper.selectSetPostIdByTagIdAndPostStatus(tag.getId(), status);

        List<Article> articles = articleMapper.selectListByIds(postIds);
        return ArticleConvert.INSTANCE.convertToSimpleDTO(articles);
    }

}
