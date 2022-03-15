package com.qinweizhao.blog.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.dto.JournalWithCmtCountDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.entity.JournalComment;
import com.qinweizhao.blog.model.enums.JournalType;
import com.qinweizhao.blog.model.params.JournalParam;
import com.qinweizhao.blog.model.params.JournalQuery;
import com.qinweizhao.blog.repository.JournalRepository;
import com.qinweizhao.blog.service.JournalCommentService;
import com.qinweizhao.blog.service.JournalService;
import com.qinweizhao.blog.service.base.AbstractCrudService;
import com.qinweizhao.blog.utils.MarkdownUtils;
import com.qinweizhao.blog.utils.ServiceUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Journal service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-24
 */
@Slf4j
@Service
public class JournalServiceImpl extends AbstractCrudService<Journal, Integer> implements JournalService {

    private final JournalRepository journalRepository;

    private final JournalCommentService journalCommentService;

    public JournalServiceImpl(JournalRepository journalRepository,
            JournalCommentService journalCommentService) {
        super(journalRepository);
        this.journalRepository = journalRepository;
        this.journalCommentService = journalCommentService;
    }

    @Override
    public Journal createBy(JournalParam journalParam) {
        Assert.notNull(journalParam, "Journal param must not be null");

        Journal journal = journalParam.convertTo();
        journal.setContent(MarkdownUtils.renderHtml(journal.getSourceContent()));

        return create(journal);
    }

    @Override
    public Journal updateBy(Journal journal) {
        Assert.notNull(journal, "Journal must not be null");

        journal.setContent(MarkdownUtils.renderHtml(journal.getSourceContent()));

        return update(journal);
    }

    @Override
    public Page<Journal> pageLatest(int top) {
        return listAll(ServiceUtils.buildLatestPageable(top));
    }

    @Override
    public Page<Journal> pageBy(JournalQuery journalQuery, Pageable pageable) {
        Assert.notNull(journalQuery, "Journal query must not be null");
        Assert.notNull(pageable, "Page info must not be null");
        return journalRepository.findAll(buildSpecByQuery(journalQuery), pageable);
    }

    @Override
    public Page<Journal> pageBy(JournalType type, Pageable pageable) {
        Assert.notNull(type, "Journal type must not be null");
        Assert.notNull(pageable, "Page info must not be null");
        return journalRepository.findAllByType(type, pageable);
    }

    @Override
    public Journal removeById(Integer id) {
        Assert.notNull(id, "Journal id must not be null");

        // Remove journal comments
        List<JournalComment> journalComments = journalCommentService.removeByPostId(id);
        log.debug("Removed journal comments: [{}]", journalComments);

        return super.removeById(id);
    }

    @Override
    public JournalDTO convertTo(Journal journal) {
        Assert.notNull(journal, "Journal must not be null");

        return new JournalDTO().convertFrom(journal);
    }

    @Override
    public List<JournalWithCmtCountDTO> convertToCmtCountDto(List<Journal> journals) {
        if (CollectionUtils.isEmpty(journals)) {
            return Collections.emptyList();
        }

        // Get journal ids
        Set<Integer> journalIds = ServiceUtils.fetchProperty(journals, Journal::getId);

        // Get comment count map
        Map<Integer, Long> journalCommentCountMap = journalCommentService.countByPostIds(journalIds);

        return journals.stream()
                .map(journal -> {
                    JournalWithCmtCountDTO journalWithCmtCountDTO = new JournalWithCmtCountDTO().convertFrom(journal);
                    // Set comment count
                    journalWithCmtCountDTO.setCommentCount(journalCommentCountMap.getOrDefault(journal.getId(), 0L));
                    return journalWithCmtCountDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<JournalWithCmtCountDTO> convertToCmtCountDto(Page<Journal> journalPage) {
        Assert.notNull(journalPage, "Journal page must not be null");

        // Convert
        List<JournalWithCmtCountDTO> journalWithCmtCountDTOS = convertToCmtCountDto(journalPage.getContent());

        // Build and return
        return new PageImpl<>(journalWithCmtCountDTOS, journalPage.getPageable(), journalPage.getTotalElements());
    }


    /**
     * Build specification by journal query.
     *
     * @param journalQuery query query must not be null
     * @return a query specification
     */
    @NonNull
    private Specification<Journal> buildSpecByQuery(@NonNull JournalQuery journalQuery) {
        Assert.notNull(journalQuery, "Journal query must not be null");

        return (Specification<Journal>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (journalQuery.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), journalQuery.getType()));
            }

            if (journalQuery.getKeyword() != null) {
                // Format like condition
                String likeCondition = String.format("%%%s%%", StringUtils.strip(journalQuery.getKeyword()));

                // Build like predicate
                Predicate contentLike = criteriaBuilder.like(root.get("content"), likeCondition);

                predicates.add(criteriaBuilder.or(contentLike));
            }

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
}
