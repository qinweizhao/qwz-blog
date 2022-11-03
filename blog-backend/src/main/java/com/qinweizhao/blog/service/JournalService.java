package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.param.JournalParam;
import com.qinweizhao.blog.model.param.JournalQueryParam;

/**
 * Journal service interface.
 *
 * @author qinweizhao
 * @since 2019-04-24
 */
public interface JournalService {

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    PageResult<JournalDTO> page(JournalQueryParam param);

    /**
     * 新增
     *
     * @param param param
     * @return boolean
     */
    boolean save(JournalParam param);

    /**
     * 更新
     *
     * @param id    id
     * @param param param
     * @return Boolean
     */
    boolean updateById(Integer id, JournalParam param);

    /**
     * 删除
     *
     * @param journalId journalId
     * @return Boolean
     */
    boolean removeById(Integer journalId);


    /**
     * 统计分类个数
     *
     * @return Long
     */
    Long count();


    /**
     * 获取评论
     *
     * @param targetId targetId
     * @return JournalDTO
     */
    JournalDTO getById(Integer targetId);
}
