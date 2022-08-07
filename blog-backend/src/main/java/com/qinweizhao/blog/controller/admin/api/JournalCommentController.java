package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.convert.CommentConvert;
import com.qinweizhao.blog.convert.JournalConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.param.JournalCommentParam;
import com.qinweizhao.blog.model.param.PostCommentParam;
import com.qinweizhao.blog.model.vo.JournalCommentWithJournalVO;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.JournalService;
import com.qinweizhao.blog.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 日志评论
 *
 * @author johnniang
 * @author qinweizhao
 * @date 2019-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/journals/comments")
public class JournalCommentController {

    private final CommentService commentService;

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping
    public PageResult<CommentDTO> page(CommentQueryParam param) {
        param.setType(CommentType.JOURNAL);
        return commentService.pageComment(param);
    }

    /**
     * 列出最新的日志评论
     *
     * @param top    top
     * @param status status
     * @return List
     */
    @GetMapping("latest")
    public List<CommentDTO> latest(@RequestParam(name = "top", defaultValue = "10") int top,
                                                        @RequestParam(name = "status", required = false) CommentStatus status) {
        CommentQueryParam param = new CommentQueryParam();
        param.setPage(top);
        param.setStatus(status);
        param.setType(CommentType.JOURNAL);
        return commentService.pageComment(param).getContent();
    }


    @GetMapping("{journalId:\\d+}/tree_view")
    public PageResult<CommentDTO> pageTree(@PathVariable("journalId") Integer journalId, CommentQueryParam param) {
        return commentService.pageTree(journalId, param);
    }

//    @GetMapping("{journalId:\\d+}/list_view")
//    @ApiOperation("Lists comment with list view")
//    public Page<BaseCommentWithParentVO> listComments(@PathVariable("journalId") Integer journalId,
//                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                      @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return journalCommentService.pageWithParentVoBy(journalId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//    }

    /**
     * 新增
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping
    public Boolean save(@RequestBody PostCommentParam param) {
        param.setType(CommentType.JOURNAL);
        return commentService.save(param);
    }

//    @PutMapping("{commentId:\\d+}/status/{status}")
//    public Boolean updateStatusBy(@PathVariable("commentId") Long commentId,
//                                         @PathVariable("status") CommentStatus status) {
//        // Update comment status
//        JournalComment updatedJournalComment = journalCommentService.updateStatus(commentId, status);
//        return journalCommentService.convertTo(updatedJournalComment);
//    }
//
//    @DeleteMapping("{commentId:\\d+}")
//    public Boolean deleteBy(@PathVariable("commentId") Long commentId) {
//        JournalComment deletedJournalComment = journalCommentService.removeById(commentId);
//        return journalCommentService.convertTo(deletedJournalComment);
//    }
}
