package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.dto.JournalWithCmtCountDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.enums.JournalType;
import com.qinweizhao.blog.model.params.JournalParam;
import com.qinweizhao.blog.model.params.JournalQuery;

import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Journal service interface.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-04-24
 */
public interface JournalService extends IService<Journal> {
//
//    /**
//     * Creates a journal.
//     *
//     * @param journalParam journal param must not be null
//     * @return created journal
//     */
//    Journal createBy(JournalParam journalParam);
//
//    /**
//     * Updates a journal.
//     *
//     * @param journal journal must not be null
//     * @return updated journal
//     */
//    Journal updateBy(Journal journal);
//
//    /**
//     * Gets latest journals.
//     *
//     * @param top max size
//     * @return latest journal page
//     */
//    Page<Journal> pageLatest(int top);
//
//    /**
//     * Pages journals.
//     *
//     * @param journalQuery journal query must not be null
//     * @param pageable     page info must not be null
//     * @return a page of journal
//     */
//
//    Page<Journal> pageBy(JournalQuery journalQuery, Pageable pageable);
//
//    /**
//     * Lists by type.
//     *
//     * @param type     journal type must not be null
//     * @param pageable page info must not be null
//     * @return a page of journal
//     */
//
//    Page<Journal> pageBy(JournalType type, Pageable pageable);
//
//    /**
//     * Converts to journal dto.
//     *
//     * @param journal journal must not be null
//     * @return journal dto
//     */
//
//    JournalDTO convertTo(Journal journal);
//
//    /**
//     * Converts to journal with comment count dto list.
//     *
//     * @param journals journal list
//     * @return journal with comment count dto list
//     */
//
//    List<JournalWithCmtCountDTO> convertToCmtCountDto(@Nullable List<Journal> journals);
//
//    /**
//     * Converts to journal with comment count dto page.
//     *
//     * @param journalPage journal page must not be null
//     * @return a page of journal with comment count dto
//     */
//
//    Page<JournalWithCmtCountDTO> convertToCmtCountDto(Page<Journal> journalPage);
}
