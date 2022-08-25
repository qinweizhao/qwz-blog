package com.qinweizhao.blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.MetaDTO;
import com.qinweizhao.blog.model.entity.Meta;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author qinweizhao
 * @since 2022/7/14
 */
public interface MetaService extends IService<Meta> {


    /**
     * getListMetaAsMapByPostIds
     *
     * @param postIds postIds
     * @return Map
     */
    Map<Integer, List<MetaDTO>> getListMetaAsMapByPostIds(Set<Integer> postIds);

    /**
     * 通过文章 id 删除
     *
     * @param postId postId
     * @return boolean
     */
    boolean removeByPostId(Integer postId);

    /**
     * 查询元数据
     *
     * @param postId postId
     * @return List
     */
    List<MetaDTO> listByPostId(Integer postId);


}
