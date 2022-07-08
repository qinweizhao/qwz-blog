package com.qinweizhao.blog.controller.admin.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import com.qinweizhao.blog.model.dto.BaseCommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.params.CommentQuery;
import com.qinweizhao.blog.model.params.JournalCommentParam;
import com.qinweizhao.blog.model.vo.BaseCommentVO;
import com.qinweizhao.blog.model.vo.BaseCommentWithParentVO;
import com.qinweizhao.blog.model.vo.JournalCommentWithJournalVO;
import com.qinweizhao.blog.service.JournalCommentService;
import com.qinweizhao.blog.service.OptionService;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Journal comment controller.
 *
 * @author johnniang
 * @date 2019-04-25
 */
@RestController
@RequestMapping("/api/admin/journals/comments")
public class JournalCommentController {

    private final JournalCommentService journalCommentService;

    private final OptionService optionService;

    public JournalCommentController(JournalCommentService journalCommentService,
            OptionService optionService) {
        this.journalCommentService = journalCommentService;
        this.optionService = optionService;
    }

    @GetMapping
    @ApiOperation("Lists journal comments")
    public Page<JournalCommentWithJournalVO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable,
            CommentQuery commentQuery) {
        Page<JournalComment> journalCommentPage = journalCommentService.pageBy(commentQuery, pageable);

        return journalCommentService.convertToWithJournalVo(journalCommentPage);
    }

    @GetMapping("latest")
    @ApiOperation("Lists latest journal comments")
    public List<JournalCommentWithJournalVO> listLatest(@RequestParam(name = "top", defaultValue = "10") int top,
            @RequestParam(name = "status", required = false) CommentStatus status) {
        List<JournalComment> latestComments = journalCommentService.pageLatest(top, status).getContent();
        return journalCommentService.convertToWithJournalVo(latestComments);
    }

    @GetMapping("{journalId:\\d+}/tree_view")
    @ApiOperation("Lists comments with tree view")
    public Page<BaseCommentVO> listCommentTree(@PathVariable("journalId") Integer journalId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        return journalCommentService.pageVosAllBy(journalId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
    }

    @GetMapping("{journalId:\\d+}/list_view")
    @ApiOperation("Lists comment with list view")
    public Page<BaseCommentWithParentVO> listComments(@PathVariable("journalId") Integer journalId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        return journalCommentService.pageWithParentVoBy(journalId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
    }

    @PostMapping
    @ApiOperation("Creates a journal comment")
    public BaseCommentDTO createCommentBy(@RequestBody JournalCommentParam journalCommentParam) {
        JournalComment journalComment = journalCommentService.createBy(journalCommentParam);
        return journalCommentService.convertTo(journalComment);
    }

    @PutMapping("{commentId:\\d+}/status/{status}")
    @ApiOperation("Updates comment status")
    public BaseCommentDTO updateStatusBy(@PathVariable("commentId") Long commentId,
            @PathVariable("status") CommentStatus status) {
        // Update comment status
        JournalComment updatedJournalComment = journalCommentService.updateStatus(commentId, status);
        return journalCommentService.convertTo(updatedJournalComment);
    }

    @DeleteMapping("{commentId:\\d+}")
    @ApiOperation("Deletes comment permanently and recursively")
    public BaseCommentDTO deleteBy(@PathVariable("commentId") Long commentId) {
        JournalComment deletedJournalComment = journalCommentService.removeById(commentId);
        return journalCommentService.convertTo(deletedJournalComment);
    }
}
