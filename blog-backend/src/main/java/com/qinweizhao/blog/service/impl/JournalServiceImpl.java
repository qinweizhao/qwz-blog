package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.mapper.JournalMapper;
import com.qinweizhao.blog.model.convert.JournalConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.param.JournalParam;
import com.qinweizhao.blog.model.param.JournalQuery;
import com.qinweizhao.blog.service.JournalService;
import com.qinweizhao.blog.util.MarkdownUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Journal service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-04-24
 */
@Slf4j
@Service
@AllArgsConstructor
public class JournalServiceImpl implements JournalService {

    private final JournalMapper journalMapper;

    @Override
    public PageResult<JournalDTO> page(JournalQuery journalQuery) {
        PageResult<Journal> page = journalMapper.selectPageJournals(journalQuery);
        return JournalConvert.INSTANCE.convert(page);
    }

    @Override
    public boolean save(JournalParam param) {

        String sourceContent = param.getSourceContent();
        Journal journal = JournalConvert.INSTANCE.convert(param);
        journal.setContent(MarkdownUtils.renderHtml(sourceContent));

        return journalMapper.insert(journal) != 1;
    }

    @Override
    public boolean updateById(Integer id, JournalParam param) {

        Journal journal = JournalConvert.INSTANCE.convert(param);
        journal.setId(id);
        journal.setContent(MarkdownUtils.renderHtml(journal.getSourceContent()));

        return journalMapper.updateById(journal) > 0;
    }

    @Override
    public boolean removeById(Integer journalId) {
        return journalMapper.deleteById(journalId) > 0;
    }

    @Override
    public Long count() {
        return journalMapper.selectCount(Wrappers.emptyWrapper());
    }

    @Override
    public JournalDTO getById(Integer journalId) {
        Journal journal = journalMapper.selectById(journalId);
        return JournalConvert.INSTANCE.convert(journal);
    }


}
