package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.param.PostCommentParam;
import com.qinweizhao.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    /**
     * 用树状视图列出帖子评论
     *
     * @param journalId postId
     * @param param  param
     * @return Page
     */
    @GetMapping("{journalId:\\d+}/tree_view")
    public PageResult<CommentDTO> pageTree(@PathVariable("journalId") Integer journalId, CommentQueryParam param) {
        return commentService.pageTree(journalId, param);
    }

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

    /**
     * 更新状态
     *
     * @param commentId commentId
     * @param status    status
     * @return Boolean
     */
    @PutMapping("{commentId:\\d+}/status/{status}")
    public Boolean updateStatusBy(@PathVariable("commentId") Long commentId,
                                  @PathVariable("status") CommentStatus status) {
        return commentService.updateStatus(commentId, status);
    }

    /**
     * 删除评论
     *
     * @param commentId commentId
     * @return Boolean
     */
    @DeleteMapping("{commentId:\\d+}")
    public Boolean delete(@PathVariable("commentId") Long commentId) {
        return commentService.removeById(commentId);
    }
}
