package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.dto.JournalWithCmtCountDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.params.JournalParam;
import com.qinweizhao.blog.model.params.JournalQuery;
import com.qinweizhao.blog.service.JournalService;
import com.qinweizhao.blog.utils.ResultUtils;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Journal controller.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/journals")
public class JournalController {

    private final JournalService journalService;

    /**
     * 日志分页
     *
     * @param pageable     pageable
     * @param journalQuery journalQuery
     * @return Page
     */
    @GetMapping
    public Page<JournalWithCmtCountDTO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable,
                                               JournalQuery journalQuery) {
        Page<Journal> journalPage = journalService.pageBy(journalQuery, pageable);
        return journalService.convertToCmtCountDto(journalPage);
    }

    @GetMapping("latest")
    @ApiOperation("Gets latest journals")
    public List<JournalWithCmtCountDTO> pageLatest(@RequestParam(name = "top", defaultValue = "10") int top) {
        List<Journal> journals = journalService.pageLatest(top).getContent();
        return journalService.convertToCmtCountDto(journals);
    }

    @PostMapping
    @ApiOperation("Creates a journal")
    public JournalDTO createBy(@RequestBody @Valid JournalParam journalParam) {
        Journal createdJournal = journalService.createBy(journalParam);
        return journalService.convertTo(createdJournal);
    }

    @PutMapping("{id:\\d+}")
    @ApiOperation("Updates a Journal")
    public JournalDTO updateBy(@PathVariable("id") Integer id,
                               @RequestBody @Valid JournalParam journalParam) {
        Journal journal = journalService.getById(id);
        journalParam.update(journal);
        Journal updatedJournal = journalService.updateBy(journal);
        return journalService.convertTo(updatedJournal);
    }

    @DeleteMapping("{journalId:\\d+}")
    @ApiOperation("Delete journal")
    public JournalDTO deleteBy(@PathVariable("journalId") Integer journalId) {
        boolean b = journalService.removeById(journalId);
        Journal judge = ResultUtils.judge(b, journalService.getById(journalId));
        return journalService.convertTo(judge);
    }
}
