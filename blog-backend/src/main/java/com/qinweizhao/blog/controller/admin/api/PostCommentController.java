package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.mapper.PostMapper;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.params.PostCommentParam;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;
import com.qinweizhao.blog.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * comment controller.
 *
 * @author johnniang
 * @author qinweizhao
 * @date 2019-03-29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/posts/comments")
public class PostCommentController {

    private final CommentService commentService;

    /**
     * 分页
     *
     * @param param param
     * @return Page
     */
    @GetMapping
    public PageResult<PostCommentWithPostVO> page(CommentQueryParam param) {
        param.setType(CommentType.POST.getValue());
        PageResult<CommentDTO> commentResult = commentService.pageComment(param);
        return commentService.buildPageResultVO(commentResult);

    }

    /**
     * 页面发布最新评论
     *
     * @param top    top
     * @param status status
     * @return List
     */
    @GetMapping("latest")
    public List<PostCommentWithPostVO> listLatest(@RequestParam(name = "top", defaultValue = "10") int top,
                                                  @RequestParam(name = "status", required = false) CommentStatus status) {
        // 构建请求参数，之所以用分页查询是因为不想再多写一个方法了。
        CommentQueryParam param = new CommentQueryParam();
        param.setPage(top);
        param.setStatus(status);

        List<CommentDTO> commentResult = commentService.pageComment(param).getContent();

        return commentService.buildResultVO(commentResult);
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
//
//    @GetMapping("{postId:\\d+}/list_view")
//    @ApiOperation("Lists post comment with list view")
//    public Page<BaseCommentWithParentVO> listComments(@PathVariable("postId") Integer postId,
//                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                      @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        return commentService.pageWithParentVoBy(postId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//    }
//

    /**
     * 新增
     *
     * @param postCommentParam postCommentParam
     * @return Boolean
     */
    @PostMapping
    public Boolean save(@RequestBody PostCommentParam postCommentParam) {
        return commentService.save(postCommentParam);
    }


    /**
     * 更新评论状态
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
     * 通过id数组批量永久删除评论
     * @param ids ids
     * @return Boolean
     */
    @DeleteMapping
    public Boolean deletePermanentlyInBatch(@RequestBody List<Long> ids) {
        return commentService.removeByIds(ids);
    }
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
