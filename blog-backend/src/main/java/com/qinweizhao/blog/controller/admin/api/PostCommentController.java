package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.convert.CommentConvert;
import com.qinweizhao.blog.convert.PostConvert;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.post.BasePostMinimalDTO;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.vo.PostCommentWithPostVO;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.utils.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * comment controller.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-29
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/posts/comments")
public class PostCommentController {

    private final CommentService commentService;

    private final PostService postService;

//    private final OptionService optionService;

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
        List<CommentDTO> contents = commentResult.getContent();
        return new PageResult<>(this.buildResultVO(contents), commentResult.getTotal());
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

        // 构建返回的结果，在 Controller 层构建纯属是强迫症，不想让 Service 参与 VO。
        return this.buildResultVO(commentResult);
    }

    /**
     * 构建返回的 VO
     *
     * @param contents contents
     * @return List
     */
    private List<PostCommentWithPostVO> buildResultVO(List<CommentDTO> contents) {
        // 获取 id
        Set<Integer> postIds = ServiceUtils.fetchProperty(contents, CommentDTO::getPostId);

        if (ObjectUtils.isEmpty(postIds)) {
            return new ArrayList<>();
        }

        Map<Integer, Post> postMap = ServiceUtils.convertToMap(postService.listByIds(postIds), Post::getId);

        return contents.stream()
                .filter(comment -> postMap.containsKey(comment.getPostId()))
                .map(comment -> {

                    PostCommentWithPostVO postCommentWithPostVO = CommentConvert.INSTANCE.convertPostToVO(comment);

                    Post post = postMap.get(comment.getPostId());
                    BasePostMinimalDTO basePostMinimalDTO = PostConvert.INSTANCE.convert(post);

                    postCommentWithPostVO.setPost(basePostMinimalDTO);

                    return postCommentWithPostVO;
                }).collect(Collectors.toList());

    }
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
