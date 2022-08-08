package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.param.JournalParam;
import com.qinweizhao.blog.model.param.JournalQuery;

/**
 * Journal service interface.
 *
 * @author qinweizhao
 * @date 2019-04-24
 */
public interface JournalService {

    /**
     * 分页
     *
     * @param journalQuery journalQuery
     * @return PageResult
     */
    PageResult<JournalDTO> page(JournalQuery journalQuery);

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
     * @return Long
     */
    Long count();


}
