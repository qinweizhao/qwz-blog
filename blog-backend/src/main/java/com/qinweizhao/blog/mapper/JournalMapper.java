package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.enums.JournalType;
import com.qinweizhao.blog.utils.MyBatisUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * @author qinweizhao
 * @since 2022/7/5
 */
@Mapper
public interface JournalMapper extends BaseMapper<Journal> {

    /**
     * 通过分类查询所有日志
     *
     * @param type     type
     * @param pageable pageable
     * @return Page
     */
    default Page<Journal> selectListByType(JournalType type, Pageable pageable) {
        IPage<Journal> page = MyBatisUtils.buildPage(pageable);
        selectPage(page, new LambdaQueryWrapper<Journal>().eq(Journal::getType, type));
        return new PageImpl<>(page.getRecords(), pageable, page.getTotal());
    }


}
