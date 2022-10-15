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
