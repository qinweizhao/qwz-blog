package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.mapper.PostTagMapper;
import com.qinweizhao.blog.mapper.TagMapper;
import com.qinweizhao.blog.model.convert.PostConvert;
import com.qinweizhao.blog.model.convert.TagConvert;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.entity.PostTag;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostTagService;
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
public class PostTagServiceImpl extends ServiceImpl<PostTagMapper, PostTag> implements PostTagService {


    private final TagMapper tagMapper;

    private final PostMapper postMapper;

    private final PostTagMapper postTagMapper;

    private final ConfigService configService;


    @Override
    public Map<Integer, List<TagDTO>> listTagListMapBy(Collection<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }

        // 查找所有帖子标签
        List<PostTag> postTags = postTagMapper.listByPostId(postIds);

        // Fetch tag ids
        Set<Integer> tagIds = ServiceUtils.fetchProperty(postTags, PostTag::getTagId);

        if (CollectionUtils.isEmpty(tagIds)) {
            return Collections.emptyMap();
        }

        List<TagDTO> tags = TagConvert.INSTANCE.convertToDTO(tagMapper.selectBatchIds(tagIds));

        Map<Integer, TagDTO> tagMap = ServiceUtils.convertToMap(tags, TagDTO::getId);

        Map<Integer, List<TagDTO>> tagListMap = new LinkedHashMap<>();

        postTags.forEach(postTag -> tagListMap.computeIfAbsent(postTag.getPostId(), postId -> new LinkedList<>()).add(tagMap.get(postTag.getTagId())));

        return tagListMap;
    }

    @Override
    public boolean removeByTagId(Integer tagId) {
        return postTagMapper.deleteByTagId(tagId) > 0;
    }

    @Override
    public List<TagDTO> listTagsByPostId(Integer postId) {
        Set<Integer> tagIds = postTagMapper.selectTagIdsByPostId(postId);
        List<Tag> tags = tagMapper.selectListByIds(tagIds);
        return TagConvert.INSTANCE.convertToDTO(tags);
    }

    @Override
    public List<PostSimpleDTO> listPostsByTagIdAndPostStatus(Integer tagId, PostStatus status) {

        Set<Integer> postIds = postTagMapper.selectSetPostIdByTagIdAndPostStatus(tagId, status);

        List<Post> posts = postMapper.selectListByIds(postIds);
        List<PostSimpleDTO> result = PostConvert.INSTANCE.convertToSimpleDTO(posts);
        result.forEach(item -> item.setFullPath(configService.buildFullPath(item.getId())));
        return result;
    }

    @Override
    public List<PostSimpleDTO> listPostsByTagSlugAndPostStatus(String tagSlug, PostStatus status) {
        Tag tag = tagMapper.selectBySlug(tagSlug);
        Set<Integer> postIds = postTagMapper.selectSetPostIdByTagIdAndPostStatus(tag.getId(), status);

        List<Post> posts = postMapper.selectListByIds(postIds);
        return PostConvert.INSTANCE.convertToSimpleDTO(posts);
    }

}
