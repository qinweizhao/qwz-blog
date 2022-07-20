package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.entity.Tag;
import com.qinweizhao.blog.model.params.TagParam;

import java.util.List;

/**
 * Tag service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-14
 */
public interface TagService  {

    /**
     * 列表
     * @return List
     */
    List<TagDTO> list();

    /**
     * 新增标签
     * @param tagParam tagParam
     * @return boolean
     */
    boolean save(TagParam tagParam);

    /**
     * 详情
     * @param tagId tagId
     * @return TagDTO
     */
    TagDTO getById(Integer tagId);

    /**
     * 更新
     * @param tagId tagId
     * @param tagParam tagParam
     * @return boolean
     */
    boolean updateById(Integer tagId, TagParam tagParam);

    /**
     * 移除
     * @param tagId tagId
     * @return boolean
     */
    boolean removeById(Integer tagId);

    /**
     * 统计标签个数
     * @return Long
     */
    Long count();



//
//    /**
//     * Get tag by slug
//     *
//     * @param slug slug
//     * @return Tag
//     */
//    @NonNull
//    Tag getBySlugOfNonNull(@NonNull String slug);
//
//    /**
//     * Get tag by slug
//     *
//     * @param slug slug
//     * @return tag
//     */
//    @NonNull
//    Tag getBySlug(@NonNull String slug);
//
//    /**
//     * Get tag by tag name.
//     *
//     * @param name name
//     * @return Tag
//     */
//    @Nullable
//    Tag getByName(@NonNull String name);
//
//    /**
//     * Converts to tag dto.
//     *
//     * @param tag tag must not be null
//     * @return tag dto
//     */
//    @NonNull
//    TagDTO convertTo(@NonNull Tag tag);
//
//    /**
//     * Converts to tag dtos.
//     *
//     * @param tags tag list
//     * @return a list of tag output dto
//     */
//    @NonNull
//    List<TagDTO> convertTo(@Nullable List<Tag> tags);
}
