package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentParam;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * comment controller.
 *
 * @author qinweizhao
 * @since 2019-03-29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/posts/comment")
public class PostCommentController {


    private final CommentService commentService;

    /**
     * 分页
     *
     * @param param param
     * @return Page
     */
    @GetMapping
    public PageResult<CommentDTO> page(CommentQueryParam param) {
        param.setType(CommentType.POST);
        return commentService.pageComment(param);

    }

    /**
     * 页面发布最新评论
     *
     * @param top    top
     * @param status status
     * @return List
     */
    @GetMapping("latest")
    public List<CommentDTO> listLatest(@RequestParam(name = "top", defaultValue = "10") int top,
                                       @RequestParam(name = "status", required = false) CommentStatus status) {
        CommentQueryParam param = new CommentQueryParam();
        param.setSize(top);
        param.setStatus(status);
        param.setType(CommentType.POST);
        return commentService.listLatest(param);
    }


    /**
     * 用树状视图列出帖子评论
     *
     * @param postId postId
     * @param param  param
     * @return Page
     */
    @GetMapping("{postId:\\d+}/tree_view")
    public PageResult<CommentDTO> pageTree(@PathVariable("postId") Integer postId, CommentQueryParam param) {
        return commentService.pageTree(postId, param);
    }


    /**
     * 新增
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping
    public Boolean save(@RequestBody CommentParam param) {
        param.setType(CommentType.POST);
        return commentService.save(param);
    }


    /**
     * 更新评论状态
     *
     * @param commentId commentId
     * @param status    status
     * @return Boolean
     */
    @PutMapping("{commentId:\\d+}/status/{status}")
    public Boolean updateStatus(@PathVariable("commentId") Long commentId,
                                @PathVariable("status") CommentStatus status) {
        return commentService.updateStatus(commentId, status);
    }

    /**
     * 批量更新评论状态
     *
     * @param status status
     * @param ids    ids
     * @return Boolean
     */
    @PutMapping("status/{status}")
    public Boolean updateStatusInBatch(@PathVariable(name = "status") CommentStatus status,
                                       @RequestBody List<Long> ids) {
        return commentService.updateStatusByIds(ids, status);
    }

    /**
     * 以递归方式永久删除帖子评论
     *
     * @param commentId commentId
     * @return Boolean
     */
    @DeleteMapping("{commentId:\\d+}")
    public Boolean deletePermanently(@PathVariable("commentId") Long commentId) {
        return commentService.removeById(commentId);
    }

    /**
     * 通过 id 数组批量永久删除评论
     *
     * @param ids ids
     * @return Boolean
     */
    @DeleteMapping
    public Boolean deletePermanentlyInBatch(@RequestBody List<Long> ids) {
        return commentService.removeByIds(ids);
    }

}
