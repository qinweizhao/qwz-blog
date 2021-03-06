//package com.qinweizhao.blog.controller.content.api;
//
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.qinweizhao.blog.convert.PostConvert;
//import com.qinweizhao.blog.model.base.BaseEntity;
//import com.qinweizhao.blog.model.dto.post.BasePostSimpleDTO;
//import com.qinweizhao.blog.model.entity.Comment;
//import com.qinweizhao.blog.model.entity.Post;
//import com.qinweizhao.blog.model.vo.PostDetailVO;
//import com.qinweizhao.blog.model.vo.PostListVO;
//import com.qinweizhao.blog.service.CommentService;
//import com.qinweizhao.blog.service.OptionService;
//import com.qinweizhao.blog.service.PostService;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.web.bind.annotation.*;
//
//import static org.springframework.data.domain.Sort.Direction.DESC;
//
///**
// * 内容发布控制器
// *
// * @author johnniang
// * @author qinweizhao
// * @date 2019-04-02
// */
//@RestController("ApiContentPostController")
//@AllArgsConstructor
//@RequestMapping("/api/content/posts")
//public class PostController {
//
//    private final PostService postService;
//
//    private final CommentService<BaseMapper<Comment>, BaseEntity> commentService;
//
//    private final OptionService optionService;
//
//
//    @GetMapping
//    @ApiOperation("Lists posts")
//    public Page<PostListVO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
//        Page<Post> postPage = postService.page(null);
//        return PostConvert.INSTANCE.convertToVO(postPage);
//    }
//
//    @PostMapping(value = "search")
//    @ApiOperation("Lists posts by keyword")
//    public Page<BasePostSimpleDTO> pageBy(@RequestParam(value = "keyword") String keyword,
//                                          @PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
//        Page<Post> postPage = postService.page(null);
//        return PostConvert.INSTANCE.convertToDTO(postPage);
//    }
//
//    @GetMapping("{postId:\\d+}")
//    @ApiOperation("Gets a post")
//    public PostDetailVO getBy(@PathVariable("postId") Integer postId,
//                              @RequestParam(value = "formatDisabled", required = false, defaultValue = "true") Boolean formatDisabled,
//                              @RequestParam(value = "sourceDisabled", required = false, defaultValue = "false") Boolean sourceDisabled) {
//        PostDetailVO postDetailVO = postService.convertToDetailVo(postService.getById(postId));
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
////    @GetMapping("/slug")
////    @ApiOperation("Gets a post")
////    public PostDetailVO getBy(@RequestParam("slug") String slug,
////                              @RequestParam(value = "formatDisabled", required = false, defaultValue = "true") Boolean formatDisabled,
////                              @RequestParam(value = "sourceDisabled", required = false, defaultValue = "false") Boolean sourceDisabled) {
////        PostDetailVO postDetailVO = postService.convertToDetailVo(postService.getBySlug(slug));
////
////        if (formatDisabled) {
////            // Clear the format content
////            postDetailVO.setFormatContent(null);
////        }
////
////        if (sourceDisabled) {
////            // Clear the original content
////            postDetailVO.setOriginalContent(null);
////        }
////
////        postService.publishVisitEvent(postDetailVO.getId());
////
////        return postDetailVO;
////    }
////
////    @GetMapping("{postId:\\d+}/comments/top_view")
////    public Page<CommentWithHasChildrenVO> listTopComments(@PathVariable("postId") Integer postId,
////                                                          @RequestParam(name = "page", required = false, defaultValue = "0") int page,
////                                                          @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
////
////        return commentService.pageTopCommentsBy(postId, CommentStatus.PUBLISHED, PageRequest.of(page, optionService.getCommentPageSize(), sort));
////    }
////
////
////    @GetMapping("{postId:\\d+}/comments/{commentParentId:\\d+}/children")
////    public List<BaseCommentDTO> listChildrenBy(@PathVariable("postId") Integer postId,
////                                               @PathVariable("commentParentId") Long commentParentId,
////                                               @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
////        // Find all children comments
////        List<Comment> postComments = commentService.listChildrenBy(postId, commentParentId, CommentStatus.PUBLISHED, sort);
////        // Convert to base comment dto
////
////        return commentService.convertTo(postComments);
////    }
////
////    @GetMapping("{postId:\\d+}/comments/tree_view")
////    @ApiOperation("Lists comments with tree view")
////    public Page<BaseCommentVO> listCommentsTree(@PathVariable("postId") Integer postId,
////                                                @RequestParam(name = "page", required = false, defaultValue = "0") int page,
////                                                @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
////        return commentService.pageVosBy(postId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
////    }
////
////    @GetMapping("{postId:\\d+}/comments/list_view")
////    @ApiOperation("Lists comment with list view")
////    public Page<BaseCommentWithParentVO> listComments(@PathVariable("postId") Integer postId,
////                                                      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
////                                                      @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
////        Page<BaseCommentWithParentVO> result = commentService.pageWithParentVoBy(postId, PageRequest.of(page, optionService.getCommentPageSize(), sort));
////        return result;
////    }
////
////    @PostMapping("comments")
////    @ApiOperation("Comments a post")
////    @CacheLock(autoDelete = false, traceRequest = true)
////    public BaseCommentDTO comment(@RequestBody PostCommentParam postCommentParam) {
////        commentService.validateCommentBlackListStatus();
////
////        // Escape content
////        postCommentParam.setContent(HtmlUtils.htmlEscape(postCommentParam.getContent(), StandardCharsets.UTF_8.displayName()));
////        return commentService.convertTo(commentService.createBy(postCommentParam));
////    }
////
////    @PostMapping("{postId:\\d+}/likes")
////    @ApiOperation("Likes a post")
////    public void like(@PathVariable("postId") Integer postId) {
////        postService.increaseLike(postId);
////    }
//}
