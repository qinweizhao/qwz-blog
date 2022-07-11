package com.qinweizhao.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qinweizhao.blog.mapper.JournalMapper;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.dto.JournalWithCmtCountDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.enums.JournalType;
import com.qinweizhao.blog.model.params.JournalParam;
import com.qinweizhao.blog.model.params.JournalQuery;
import com.qinweizhao.blog.service.JournalCommentService;
import com.qinweizhao.blog.service.JournalService;
import com.qinweizhao.blog.utils.MarkdownUtils;
import com.qinweizhao.blog.utils.ResultUtils;
import com.qinweizhao.blog.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
public class JournalServiceImpl extends ServiceImpl<JournalMapper, Journal> implements JournalService {

//    private final JournalCommentService journalCommentService;
//
//    @Override
//    public Journal createBy(JournalParam journalParam) {
//        Assert.notNull(journalParam, "请求参数不能为空");
//
//        Journal journal = new Journal();
//        // todo
//        BeanUtils.copyProperties(journalParam, journal);
//        journal.setContent(MarkdownUtils.renderHtml(journal.getSourceContent()));
//
//        // 保存
//        return ResultUtils.judge(this.baseMapper.insert(journal), journal);
//    }
//
//    @Override
//    public Journal updateBy(Journal journal) {
//        Assert.notNull(journal, "Journal must not be null");
//
//        journal.setContent(MarkdownUtils.renderHtml(journal.getSourceContent()));
//
//        this.baseMapper.updateById(journal);
//
//        return journal;
//    }
//
//    @Override
//    public Page<Journal> pageLatest(int top) {
//        Pageable pageable = ServiceUtils.buildLatestPageable(top);
//        return listAll(ServiceUtils.buildLatestPageable(top));
//    }
//
//    @Override
//    public Page<Journal> pageBy(JournalQuery journalQuery, Pageable pageable) {
//        Assert.notNull(journalQuery, "Journal query must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//        Specification<Journal> journalSpecification = buildSpecByQuery(journalQuery);
//        return journalRepository.findAll(buildSpecByQuery(journalQuery), pageable);
//    }
//
//    @Override
//    public Page<Journal> pageBy(JournalType type, Pageable pageable) {
//        Assert.notNull(type, "Journal type must not be null");
//        Assert.notNull(pageable, "Page info must not be null");
//        return this.baseMapper.selectListByType(type, pageable);
//    }
//
//    @Override
//    public Journal removeById(Integer id) {
//        Assert.notNull(id, "Journal id must not be null");
//
//        // Remove journal comments
//        List<JournalComment> journalComments = journalCommentService.removeByPostId(id);
//        log.debug("Removed journal comments: [{}]", journalComments);
//
//        return super.removeById(id);
//    }
//
//    @Override
//    public JournalDTO convertTo(Journal journal) {
//        Assert.notNull(journal, "Journal must not be null");
//
//        return new JournalDTO().convertFrom(journal);
//    }
//
//    @Override
//    public List<JournalWithCmtCountDTO> convertToCmtCountDto(List<Journal> journals) {
//        if (CollectionUtils.isEmpty(journals)) {
//            return Collections.emptyList();
//        }
//
//        // Get journal ids
//        Set<Integer> journalIds = ServiceUtils.fetchProperty(journals, Journal::getId);
//
//        // Get comment count map
//        Map<Integer, Long> journalCommentCountMap = journalCommentService.countByPostIds(journalIds);
//
//        return journals.stream()
//                .map(journal -> {
//                    JournalWithCmtCountDTO journalWithCmtCountDTO = new JournalWithCmtCountDTO().convertFrom(journal);
//                    // Set comment count
//                    journalWithCmtCountDTO.setCommentCount(journalCommentCountMap.getOrDefault(journal.getId(), 0L));
//                    return journalWithCmtCountDTO;
//                })
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Page<JournalWithCmtCountDTO> convertToCmtCountDto(Page<Journal> journalPage) {
//        Assert.notNull(journalPage, "Journal page must not be null");
//
//        // Convert
//        List<JournalWithCmtCountDTO> journalWithCmtCountDTOS = convertToCmtCountDto(journalPage.getContent());
//
//        // Build and return
//        return new PageImpl<>(journalWithCmtCountDTOS, journalPage.getPageable(), journalPage.getTotalElements());
//    }

}
