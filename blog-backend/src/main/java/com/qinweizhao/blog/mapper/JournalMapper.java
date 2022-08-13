package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.param.JournalQuery;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
@Mapper
public interface JournalMapper extends BaseMapper<Journal> {

    /**
     * 查询列表
     *
     * @param journalIds journalIds
     * @return List
     */
    default List<Journal> selectListByIds(Set<Integer> journalIds) {
        return selectList(new LambdaQueryWrapperX<Journal>()
                .inIfPresent(Journal::getId, journalIds)
        );
    }

    /**
     * 通过 id 查询是否存在
     *
     * @param journalId journalId
     * @return boolean
     */
    boolean selectExistsById(Integer journalId);

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Journal> selectPageJournals(JournalQuery param) {
        Page<Journal> page = MyBatisUtils.buildPage(param);
        Page<Journal> result = this.selectPage(page, new LambdaQueryWrapperX<Journal>()
                .eqIfPresent(Journal::getType, param.getType())
                .likeIfPresent(Journal::getSourceContent, param.getKeyword())
        );
        return MyBatisUtils.buildSimplePageResult(result);
    }


}
