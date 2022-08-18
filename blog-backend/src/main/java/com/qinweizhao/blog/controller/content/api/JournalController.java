package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.framework.cache.lock.CacheLock;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentParam;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;

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

    private final CommentService commentService;

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

    /**
     * 日志评论
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping("comments")
    @CacheLock(autoDelete = false, traceRequest = true)
    public Boolean comment(@RequestBody CommentParam param) {
        param.setTargetId(param.getPostId());
        commentService.validateCommentBlackListStatus();
        param.setType(CommentType.JOURNAL);
        // 转义内容
        param.setContent(HtmlUtils.htmlEscape(param.getContent(), StandardCharsets.UTF_8.displayName()));
        return commentService.save(param);
    }
}
