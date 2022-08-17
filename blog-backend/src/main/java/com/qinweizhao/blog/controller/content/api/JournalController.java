package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.service.CommentService;
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

}
