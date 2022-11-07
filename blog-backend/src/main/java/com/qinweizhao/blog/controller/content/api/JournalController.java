package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.framework.cache.lock.CacheLock;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentParam;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.JournalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;

/**
 * 前台-日志
 *
 * @author qinweizhao
 * @since 2019-04-26
 */
@RestController("ApiContentJournalController")
@AllArgsConstructor
@RequestMapping("/api/content/journal")
public class JournalController {

    private final CommentService commentService;

    private final JournalService journalService;

    /**
     * 树
     *
     * @param journalId journalId
     * @param param     param
     * @return PageResult
     */
    @GetMapping("{journalId:\\d+}/comment/tree_view")
    public PageResult<CommentDTO> listCommentsTree(@PathVariable("journalId") Integer journalId, CommentQueryParam param) {
        param.setType(CommentType.JOURNAL);
        param.setStatus(CommentStatus.PUBLISHED);
        return commentService.pageTree(journalId, param);
    }

    /**
     * 日志评论
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping("comment")
    @CacheLock(autoDelete = false, traceRequest = true)
    public Boolean comment(@RequestBody CommentParam param) {
        commentService.validateCommentBlackListStatus();
        param.setType(CommentType.JOURNAL);
        // 转义内容
        param.setContent(HtmlUtils.htmlEscape(param.getContent(), StandardCharsets.UTF_8.displayName()));
        return commentService.save(param);
    }


    /**
     * 点赞
     *
     * @param postId postId
     * @return Boolean
     */
    @PostMapping("{postId:\\d+}/likes")
    public Boolean like(@PathVariable("postId") Integer postId) {
        return journalService.increaseLike(postId);
    }

}
