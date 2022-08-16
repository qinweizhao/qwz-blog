package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.JournalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志 controller.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-04-26l
 */
@RestController("ApiContentJournalController")
@AllArgsConstructor
@RequestMapping("/api/content/journals")
public class JournalController {

    private final JournalService journalService;

    private final CommentService commentService;

    private final ConfigService configService;

//    /**
//     * 分页
//     *
//     * @param param param
//     * @return Page
//     */
//    @GetMapping
//    public PageResult<JournalDTO> pageBy(JournalQueryParam param) {
//        return journalService.page(param);
//    }
//
//    /**
//     * 详情
//     *
//     * @param journalId journalId
//     * @return JournalDTO
//     */
//    @GetMapping("{journalId:\\d+}")
//    public JournalDTO getBy(@PathVariable("journalId") Integer journalId) {
//        return journalService.getById(journalId);
//    }

//    @GetMapping("{journalId:\\d+}/comments/top_view")
//    public Page<CommentWithHasChildrenVO> listTopComments(@PathVariable("journalId") Integer journalId,
//                                                          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                          @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return journalCommentService.pageTopCommentsBy(journalId, CommentStatus.PUBLISHED, PageRequest.of(page, configService.getCommentPageSize(), sort));
//    }

//    @GetMapping("{journalId:\\d+}/comments/{commentParentId:\\d+}/children")
//    public List<BaseCommentDTO> listChildrenBy(@PathVariable("journalId") Integer journalId,
//                                               @PathVariable("commentParentId") Long commentParentId,
//                                               @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        // Find all children comments
//        List<JournalComment> postComments = journalCommentService.listChildrenBy(journalId, commentParentId, CommentStatus.PUBLISHED, sort);
//        // Convert to base comment dto
//        return journalCommentService.convertTo(postComments);
//    }

    /**
     * 树
     *
     * @param journalId journalId
     * @param param     param
     * @return PageResult
     */
    @GetMapping("{journalId:\\d+}/comments/tree_view")
    public PageResult<CommentDTO> listCommentsTree(@PathVariable("journalId") Integer journalId, CommentQueryParam param) {
        param.setType(CommentType.JOURNAL);
        return commentService.pageTree(journalId, param);
    }

//    @GetMapping("{journalId:\\d+}/comments/list_view")
//    @ApiOperation("Lists comment with list view")
//    public Page<BaseCommentWithParentVO> listComments(@PathVariable("journalId") Integer journalId,
//                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                      @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return journalCommentService.pageWithParentVoBy(journalId, PageRequest.of(page, configService.getCommentPageSize(), sort));
//    }
//
//    @PostMapping("comments")
//    @ApiOperation("Comments a post")
//    @CacheLock(autoDelete = false, traceRequest = true)
//    public BaseCommentDTO comment(@RequestBody JournalCommentParam journalCommentParam) {
//
//        // Escape content
//        journalCommentParam.setContent(HtmlUtils.htmlEscape(journalCommentParam.getContent(), StandardCharsets.UTF_8.displayName()));
//        return journalCommentService.convertTo(journalCommentService.createBy(journalCommentParam));
//    }
}
