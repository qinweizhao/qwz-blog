package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.service.JournalCommentService;
import org.springframework.stereotype.Service;

/**
 * Journal comment service implementation.
 *
 * @author johnniang
 * @date 2019-04-25
 */
@Service
public class JournalCommentServiceImpl implements JournalCommentService {

//
//    @Override
//    public void validateTarget(Integer journalId) {
//        if (!journalRepository.existsById(journalId)) {
//            throw new NotFoundException("查询不到该日志信息").setErrorData(journalId);
//        }
//    }
//
//    @Override
//    public List<JournalCommentWithJournalVO> convertToWithJournalVo(List<JournalComment> journalComments) {
//
//        if (CollectionUtil.isEmpty(journalComments)) {
//            return Collections.emptyList();
//        }
//
//        Set<Integer> journalIds = ServiceUtils.fetchProperty(journalComments, JournalComment::getPostId);
//
//        // Get all journals
//        List<Journal> journals = journalRepository.findAllById(journalIds);
//
//        Map<Integer, Journal> journalMap = ServiceUtils.convertToMap(journals, Journal::getId);
//
//        return journalComments.stream()
//                .filter(journalComment -> journalMap.containsKey(journalComment.getPostId()))
//                .map(journalComment -> {
//                    JournalCommentWithJournalVO journalCmtWithJournalVo = new JournalCommentWithJournalVO().convertFrom(journalComment);
//                    journalCmtWithJournalVo.setJournal(new JournalDTO().convertFrom(journalMap.get(journalComment.getPostId())));
//                    return journalCmtWithJournalVo;
//                })
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Page<JournalCommentWithJournalVO> convertToWithJournalVo(Page<JournalComment> journalCommentPage) {
//        Assert.notNull(journalCommentPage, "Journal comment page must not be null");
//
//        // Convert the list
//        List<JournalCommentWithJournalVO> journalCmtWithJournalVOS = convertToWithJournalVo(journalCommentPage.getContent());
//
//        // Build and return
//        return new PageImpl<>(journalCmtWithJournalVOS, journalCommentPage.getPageable(), journalCommentPage.getTotalElements());
//    }
}
