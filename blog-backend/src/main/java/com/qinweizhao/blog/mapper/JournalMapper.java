package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
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
    default List<Journal> selectListByIds(Set<Integer> journalIds){
        return selectList(new LambdaQueryWrapperX<Journal>()
                .inIfPresent(Journal::getId, journalIds)
        );
    }

    /**
     * 通过 id 查询是否存在
     * @param journalId journalId
     * @return boolean
     */
    boolean selectExistsById(Integer journalId);


//    /**
//     * 通过分类查询所有日志
//     *
//     * @param type     type
//     * @param pageable pageable
//     * @return Page
//     */
//    default Page<Journal> selectListByType(JournalType type, Pageable pageable) {
//        IPage<Journal> page = MyBatisUtils.buildPage(pageable);
//        selectPage(page, new LambdaQueryWrapper<Journal>().eq(Journal::getType, type));
//        return new PageImpl<>(page.getRecords(), pageable, page.getTotal());
//    }


}
