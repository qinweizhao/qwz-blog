package com.qinweizhao.blog.controller.content.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.CommentDTO;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.CommentService;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 内容发布控制器
 *
 * @author johnniang
 * @author qinweizhao
 * @since 2019-04-02
 */
@RestController("ApiContentPostController")
@AllArgsConstructor
@RequestMapping("/api/content/posts")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    private final OptionService optionService;


    @GetMapping
    public PageResult<PostListDTO> page(PostQueryParam param) {
        return postService.page(param);
    }

    //    @PostMapping(value = "search")
//    @ApiOperation("Lists posts by keyword")
//    public Page<BasePostSimpleDTO> pageBy(@RequestParam(value = "keyword") String keyword,
//                                          @PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
//        Page<Post> postPage = postService.page(null);
//        return PostConvert.INSTANCE.convertToDTO(postPage);
//    }
//
    @GetMapping("{postId:\\d+}")
    public PostDTO get(@PathVariable("postId") Integer postId,
                       @RequestParam(value = "formatDisabled", required = false, defaultValue = "true") Boolean formatDisabled,
                       @RequestParam(value = "sourceDisabled", required = false, defaultValue = "false") Boolean sourceDisabled) {
        PostDTO result = postService.getById(postId);

        if (formatDisabled) {
            // Clear the format content
            result.setFormatContent(null);
        }

        if (sourceDisabled) {
            // Clear the original content
            result.setOriginalContent(null);
        }

//        postService.publishVisitEvent(result.getId());

        return result;
    }

//    @GetMapping("/slug")
//    @ApiOperation("Gets a post")
//    public PostDetailVO getBy(@RequestParam("slug") String slug,
//                              @RequestParam(value = "formatDisabled", required = false, defaultValue = "true") Boolean formatDisabled,
//                              @RequestParam(value = "sourceDisabled", required = false, defaultValue = "false") Boolean sourceDisabled) {
//        PostDetailVO postDetailVO = postService.convertToDetailVo(postService.getBySlug(slug));
//
//        if (formatDisabled) {
//            // Clear the format content
//            postDetailVO.setFormatContent(null);
//        }
//
//        if (sourceDisabled) {
//            // Clear the original content
//            postDetailVO.setOriginalContent(null);
//        }
//
//        postService.publishVisitEvent(postDetailVO.getId());
//
//        return postDetailVO;
//    }
//
//    @GetMapping("{postId:\\d+}/comments/top_view")
//    public Page<CommentWithHasChildrenVO> listTopComments(@PathVariable("postId") Integer postId,
//                                                          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                          @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//
//        return commentService.pageTopCommentsBy(postId, CommentStatus.PUBLISHED, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//    }
//
//
//    @GetMapping("{postId:\\d+}/comments/{commentParentId:\\d+}/children")
//    public List<BaseCommentDTO> listChildrenBy(@PathVariable("postId") Integer postId,
//                                               @PathVariable("commentParentId") Long commentParentId,
//                                               @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        // Find all children comments
//        List<Comment> postComments = commentService.listChildrenBy(postId, commentParentId, CommentStatus.PUBLISHED, sort);
//        // Convert to base comment dto
//
//        return commentService.convertTo(postComments);
//    }
//

    /**
     * 用树状视图列出评论
     *
     * @param postId postId
     * @return PageResult
     */
    @GetMapping("{postId:\\d+}/comments/tree_view")
    public PageResult<CommentDTO> listCommentsTree(@PathVariable("postId") Integer postId, CommentQueryParam param) {
        param.setType(CommentType.POST);
        return commentService.pageComment(param);
    }
//
//    @GetMapping("{postId:\\d+}/comments/list_view")
//    @ApiOperation("Lists comment with list view")
//    public Page<BaseCommentWithParentVO> listComments(@PathVariable("postId") Integer postId,
//                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
//                                                      @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
//        Page<BaseCommentWithParentVO> result = commentService.pageWithParentVoBy(postId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
//        return result;
//    }
//
//    @PostMapping("comments")
//    @ApiOperation("Comments a post")
//    @CacheLock(autoDelete = false, traceRequest = true)
//    public BaseCommentDTO comment(@RequestBody PostCommentParam postCommentParam) {
//        commentService.validateCommentBlackListStatus();
//
//        // Escape content
//        postCommentParam.setContent(HtmlUtils.htmlEscape(postCommentParam.getContent(), StandardCharsets.UTF_8.displayName()));
//        return commentService.convertTo(commentService.createBy(postCommentParam));
//    }
//
//    @PostMapping("{postId:\\d+}/likes")
//    @ApiOperation("Likes a post")
//    public void like(@PathVariable("postId") Integer postId) {
//        postService.increaseLike(postId);
//    }
}
