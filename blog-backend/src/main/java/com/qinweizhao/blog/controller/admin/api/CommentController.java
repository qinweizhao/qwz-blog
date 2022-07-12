package com.qinweizhao.blog.controller.admin.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * comment controller.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/posts/comments")
public class CommentController {

    private final CommentService commentService;
//
//    private final OptionService optionService;

    /**
     * 分页
     *
     * @param param param
     * @return Page
     */
    @GetMapping
//    public Page<PostCommentWithPostVO> page(CommentQueryParam param) {
    public Page<Comment> page(CommentQueryParam param) {
        Page<Comment> commentPage = commentService.pageComment(param);
        return commentPage;
    }
//
//    @GetMapping("latest")
//    @ApiOperation("Pages post latest comments")
//    public List<PostCommentWithPostVO> listLatest(@RequestParam(name = "top", defaultValue = "10") int top,
//                                                  @RequestParam(name = "status", required = false) CommentStatus status) {
//        // Get latest comment
//        List<Comment> content = commentService.pageLatest(top, status).getContent();
//
//        // Convert and return
//        return CommentConvert.INSTANCE.convertToWithPostVo(content);
//    }
//
//    /**
//     * 用树状视图列出帖子评论
//     *
//     * @param postId postId
//     * @param page   postId
//     * @param sort   sort
//     * @return Page
//     */
//    @GetMapping("{postId:\\d+}/tree_view")
//    public Page<BaseCommentVO> listCommentTree(@PathVariable("postId") Integer postId,
//                                               @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                               @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return commentService.pageVosAllBy(postId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//    }
//
//    @GetMapping("{postId:\\d+}/list_view")
//    @ApiOperation("Lists post comment with list view")
//    public Page<BaseCommentWithParentVO> listComments(@PathVariable("postId") Integer postId,
//                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                      @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return commentService.pageWithParentVoBy(postId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//    }
//
//    @PostMapping
//    @ApiOperation("Creates a post comment (new or reply)")
//    public BaseCommentDTO createBy(@RequestBody PostCommentParam postCommentParam) {
//        PostComment createdPostComment = commentService.createBy(postCommentParam);
//        return commentService.convertTo(createdPostComment);
//    }
//
//    @PutMapping("{commentId:\\d+}/status/{status}")
//    @ApiOperation("Updates post comment status")
//    public BaseCommentDTO updateStatusBy(@PathVariable("commentId") Long commentId,
//                                         @PathVariable("status") CommentStatus status) {
//        // Update comment status
//        PostComment updatedPostComment = commentService.updateStatus(commentId, status);
//        return commentService.convertTo(updatedPostComment);
//    }
//
//    @PutMapping("status/{status}")
//    @ApiOperation("Updates post comment status in batch")
//    public List<BaseCommentDTO> updateStatusInBatch(@PathVariable(name = "status") CommentStatus status,
//                                                    @RequestBody List<Long> ids) {
//        List<PostComment> comments = commentService.updateStatusByIds(ids, status);
//        return commentService.convertTo(comments);
//    }
//
//    @DeleteMapping("{commentId:\\d+}")
//    @ApiOperation("Deletes post comment permanently and recursively")
//    public BaseCommentDTO deletePermanently(@PathVariable("commentId") Long commentId) {
//        PostComment deletedPostComment = commentService.removeById(commentId);
//        return commentService.convertTo(deletedPostComment);
//    }
//
//    @DeleteMapping
//    @ApiOperation("Delete post comments permanently in batch by id array")
//    public List<PostComment> deletePermanentlyInBatch(@RequestBody List<Long> ids) {
//        return commentService.removeByIds(ids);
//    }
//
//    @GetMapping("{commentId:\\d+}")
//    @ApiOperation("Gets a post comment by comment id")
//    public PostCommentWithPostVO getBy(@PathVariable("commentId") Long commentId) {
//        PostComment comment = commentService.getById(commentId);
//        return commentService.convertToWithPostVo(comment);
//    }
//
//    @PutMapping("{commentId:\\d+}")
//    @ApiOperation("Updates a post comment")
//    public BaseCommentDTO updateBy(@Valid @RequestBody PostCommentParam commentParam,
//                                   @PathVariable("commentId") Long commentId) {
//        PostComment commentToUpdate = commentService.getById(commentId);
//
//        commentParam.update(commentToUpdate);
//
//        return commentService.convertTo(commentService.update(commentToUpdate));
//    }
}
