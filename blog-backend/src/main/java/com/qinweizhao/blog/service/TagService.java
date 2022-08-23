package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.param.TagParam;

import java.util.List;

/**
 * Tag service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-03-14
 */
public interface TagService {

    /**
     * 列表
     *
     * @return List
     */
    List<TagDTO> list();

    /**
     * 新增标签
     *
     * @param tagParam tagParam
     * @return boolean
     */
    boolean save(TagParam tagParam);

    /**
     * 详情
     *
     * @param tagId tagId
     * @return TagDTO
     */
    TagDTO getById(Integer tagId);

    /**
     * 通过别名获取
     *
     * @param slug slug
     * @return TagDTO
     */
    TagDTO getBySlug(String slug);

    /**
     * 更新
     *
     * @param tagId    tagId
     * @param tagParam tagParam
     * @return boolean
     */
    boolean updateById(Integer tagId, TagParam tagParam);

    /**
     * 移除
     *
     * @param tagId tagId
     * @return boolean
     */
    boolean removeById(Integer tagId);

    /**
     * 统计标签个数
     *
     * @return Long
     */
    Long count();

}
