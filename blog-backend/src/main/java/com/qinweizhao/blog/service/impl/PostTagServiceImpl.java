package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.convert.TagConvert;
import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.mapper.PostTagMapper;
import com.qinweizhao.blog.mapper.TagMapper;
import com.qinweizhao.blog.model.dto.TagWithPostCountDTO;
import com.qinweizhao.blog.model.entity.PostTag;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.projection.TagPostPostCountProjection;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostTagService;
import com.qinweizhao.blog.service.TagService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * Post tag service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-19
 */
@Service
@AllArgsConstructor
public class PostTagServiceImpl implements PostTagService {


    private final TagService tagService;

    private final OptionService optionService;

    private final TagMapper tagMapper;

    private final PostMapper postMapper;

    private final PostTagMapper postTagMapper;


//    @Override
//    public List<Tag> listTagsBy(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        // Find all tag ids
//        Set<Integer> tagIds = postTagRepository.findAllTagIdsByPostId(postId);
//
//        return tagRepository.findAllById(tagIds);
//    }

    @Override
    public List<TagWithPostCountDTO> listTagWithPostCount() {

        // 查找所有标签
        List<Tag> tags = tagMapper.selectList(null);

        // 查找所有帖子计数
        Map<Integer, Long> tagPostCountMap = ServiceUtils.convertToMap(postTagMapper.selectPostCount(), TagPostPostCountProjection::getTagId, TagPostPostCountProjection::getPostCount);


        return tags.stream().map(
                tag -> {
                    TagWithPostCountDTO tagWithCountOutputDTO = TagConvert.INSTANCE.convertWithPostCountDTO(tag);
                    tagWithCountOutputDTO.setPostCount(tagPostCountMap.getOrDefault(tag.getId(), 0L));

                    StringBuilder fullPath = new StringBuilder();

                    if (optionService.isEnabledAbsolutePath()) {
                        fullPath.append(optionService.getBlogBaseUrl());
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(optionService.getTagsPrefix())
                            .append(URL_SEPARATOR)
                            .append(tag.getSlug())
                            .append(optionService.getPathSuffix());

                    tagWithCountOutputDTO.setFullPath(fullPath.toString());

                    return tagWithCountOutputDTO;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, List<Tag>> listTagListMapBy(Collection<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return Collections.emptyMap();
        }

        // 查找所有帖子标签
        List<PostTag> postTags = postTagMapper.listByPostId(postIds);

        // Fetch tag ids
        Set<Integer> tagIds = ServiceUtils.fetchProperty(postTags, PostTag::getTagId);

        // Find all tags
        List<Tag> tags = tagMapper.selectBatchIds(tagIds);

        // Convert to tag map
        Map<Integer, Tag> tagMap = ServiceUtils.convertToMap(tags, Tag::getId);

        // Create tag list map
        Map<Integer, List<Tag>> tagListMap = new HashMap<>();

        // Foreach and collect
        postTags.forEach(postTag -> tagListMap.computeIfAbsent(postTag.getPostId(), postId -> new LinkedList<>()).add(tagMap.get(postTag.getTagId())));

        return tagListMap;
    }

    @Override
    public boolean removeByTagId(Integer tagId) {
        return postTagMapper.deleteByTagId(tagId) > 0;
    }
//
//
//    @Override
//    public List<Post> listPostsBy(Integer tagId) {
//        Assert.notNull(tagId, "Tag id must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postTagRepository.findAllPostIdsByTagId(tagId);
//
//        return postRepository.findAllById(postIds);
//    }
//
//    @Override
//    public List<Post> listPostsBy(Integer tagId, PostStatus status) {
//        Assert.notNull(tagId, "Tag id must not be null");
//        Assert.notNull(status, "Post status must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postTagRepository.findAllPostIdsByTagId(tagId, status);
//
//        return postRepository.findAllById(postIds);
//    }
//
//    @Override
//    public List<Post> listPostsBy(String slug, PostStatus status) {
//        Assert.notNull(slug, "Tag slug must not be null");
//        Assert.notNull(status, "Post status must not be null");
//
//        Tag tag = tagRepository.getBySlug(slug).orElseThrow(() -> new NotFoundException("查询不到该标签的信息").setErrorData(slug));
//
//        Set<Integer> postIds = postTagRepository.findAllPostIdsByTagId(tag.getId(), status);
//
//        return postRepository.findAllById(postIds);
//    }
//
//    @Override
//    public Page<Post> pagePostsBy(Integer tagId, Pageable pageable) {
//        Assert.notNull(tagId, "Tag id must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postTagRepository.findAllPostIdsByTagId(tagId);
//
//        return postRepository.findAllByIdIn(postIds, pageable);
//    }
//
//    @Override
//    public Page<Post> pagePostsBy(Integer tagId, PostStatus status, Pageable pageable) {
//        Assert.notNull(tagId, "Tag id must not be null");
//        Assert.notNull(status, "Post status must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//
//        // Find all post ids
//        Set<Integer> postIds = postTagRepository.findAllPostIdsByTagId(tagId, status);
//
//        return postRepository.findAllByIdIn(postIds, pageable);
//    }
//
//    @Override
//    public List<PostTag> mergeOrCreateByIfAbsent(Integer postId, Set<Integer> tagIds) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        if (CollectionUtils.isEmpty(tagIds)) {
//            return Collections.emptyList();
//        }
//
//        // Create post tags
//        List<PostTag> postTagsStaging = tagIds.stream().map(tagId -> {
//            // Build post tag
//            PostTag postTag = new PostTag();
//            postTag.setPostId(postId);
//            postTag.setTagId(tagId);
//            return postTag;
//        }).collect(Collectors.toList());
//
//        List<PostTag> postTagsToRemove = new LinkedList<>();
//        List<PostTag> postTagsToCreate = new LinkedList<>();
//
//        List<PostTag> postTags = postTagRepository.findAllByPostId(postId);
//
//        postTags.forEach(postTag -> {
//            if (!postTagsStaging.contains(postTag)) {
//                postTagsToRemove.add(postTag);
//            }
//        });
//
//        postTagsStaging.forEach(postTagStaging -> {
//            if (!postTags.contains(postTagStaging)) {
//                postTagsToCreate.add(postTagStaging);
//            }
//        });
//
//        // Remove post tags
//        removeAll(postTagsToRemove);
//
//        // Remove all post tags need to remove
//        postTags.removeAll(postTagsToRemove);
//
//        // Add all created post tags
//        postTags.addAll(createInBatch(postTagsToCreate));
//
//        // Return post tags
//        return postTags;
//    }
//
//    @Override
//    public List<PostTag> listByPostId(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        return postTagRepository.findAllByPostId(postId);
//    }
//
//    @Override
//    public List<PostTag> listByTagId(Integer tagId) {
//        Assert.notNull(tagId, "Tag id must not be null");
//
//        return postTagRepository.findAllByTagId(tagId);
//    }
//
//    @Override
//    public Set<Integer> listTagIdsByPostId(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        return postTagRepository.findAllTagIdsByPostId(postId);
//    }
//
//    @Override
//    public List<PostTag> removeByPostId(Integer postId) {
//        Assert.notNull(postId, "Post id must not be null");
//
//        return postTagRepository.deleteByPostId(postId);
//    }
//
//    @Override
//    public List<PostTag> removeByTagId(Integer tagId) {
//        Assert.notNull(tagId, "Tag id must not be null");
//
//        return postTagRepository.deleteByTagId(tagId);
//    }
}
