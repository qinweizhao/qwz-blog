package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.PostTag;
import com.qinweizhao.blog.model.enums.PostStatus;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Post tag service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-19
 */
public interface PostTagService extends IService<PostTag> {

//    /**
//     * Lists tags by post id.
//     *
//     * @param postId post id must not be null
//     * @return a list of tag
//     */
//    List<Tag> listTagsBy(@NonNull Integer postId);
//

    /**
     * 按帖子 ID 列出标签列表映射
     *
     * @param postIds post id collection
     * @return tag map (key: postId, value: a list of tags)
     */
    Map<Integer, List<TagDTO>> listTagListMapBy(Collection<Integer> postIds);

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
     * @param postId postId
     * @return List
     */
    List<TagDTO> listTagsByPostId(Integer postId);


//
//    /**
//     * Lists posts by tag id.
//     *
//     * @param tagId tag id must not be null
//     * @return a list of post
//     */
//    @NonNull
//    List<Post> listPostsBy(@NonNull Integer tagId);
//
//    /**
//     * Lists posts by tag id and post status.
//     *
//     * @param tagId  tag id must not be null
//     * @param status post status
//     * @return a list of post
//     */
//    @NonNull
//    List<Post> listPostsBy(@NonNull Integer tagId, @NonNull PostStatus status);
//
//    /**
//     * Lists posts by tag slug and post status.
//     *
//     * @param slug   tag slug must not be null
//     * @param status post status
//     * @return a list of post
//     */
//    @NonNull
//    List<Post> listPostsBy(@NonNull String slug, @NonNull PostStatus status);
//
//    /**
//     * Pages posts by tag id.
//     *
//     * @param tagId    must not be null
//     * @param pageable must not be null
//     * @return a page of post
//     */
//    Page<Post> pagePostsBy(@NonNull Integer tagId, Pageable pageable);
//
//    /**
//     * Pages posts by tag id and post status.
//     *
//     * @param tagId    must not be null
//     * @param status   post status
//     * @param pageable must not be null
//     * @return a page of post
//     */
//    Page<Post> pagePostsBy(@NonNull Integer tagId, @NonNull PostStatus status, Pageable pageable);
//
//    /**
//     * Merges or creates post tags by post id and tag id set if absent.
//     *
//     * @param postId post id must not be null
//     * @param tagIds tag id set
//     * @return a list of post tag
//     */
//    @NonNull
//    List<PostTag> mergeOrCreateByIfAbsent(@NonNull Integer postId, @Nullable Set<Integer> tagIds);
//
//    /**
//     * Lists post tags by post id.
//     *
//     * @param postId post id must not be null
//     * @return a list of post tag
//     */
//    @NonNull
//    List<PostTag> listByPostId(@NonNull Integer postId);
//
//    /**
//     * Lists post tags by tag id.
//     *
//     * @param tagId tag id must not be null
//     * @return a list of post tag
//     */
//    @NonNull
//    List<PostTag> listByTagId(@NonNull Integer tagId);
//
//    /**
//     * Lists tag id set by post id.
//     *
//     * @param postId post id must not be null
//     * @return a set of tag id
//     */
//    @NonNull
//    Set<Integer> listTagIdsByPostId(@NonNull Integer postId);
//
//    /**
//     * Removes post tags by post id.
//     *
//     * @param postId post id must not be null
//     * @return a list of post tag
//     */
//    @NonNull
//    @Transactional
//    List<PostTag> removeByPostId(@NonNull Integer postId);

    /**
     * 列表
     *
     * @param tagId  tagId
     * @param status published
     * @return List
     */
    List<PostSimpleDTO> listPostsByTagIdAndPostStatus(Integer tagId, PostStatus status);

    /**
     * 列表
     *
     * @param tagSlug tagSlug
     * @param status  published
     * @return List
     */
    List<PostSimpleDTO> listPostsByTagSlugAndPostStatus(String tagSlug, PostStatus status);

}
