package com.qinweizhao.blog.controller.admin;

import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.post.BasePostSimpleDTO;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Post controller.
 *
 * @author johnniang
 * @author ryanwang
 * @author guqing
 * @author qinweizhao
 * @date 2019-03-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/posts")
public class PostController {

    private final PostService postService;


    /**
     * 分页
     *
     * @param postQueryParam postQueryParam
     * @param more           more
     * @return PageResult
     */
    @GetMapping
    public PageResult<? extends BasePostSimpleDTO> pageBy(PostQueryParam postQueryParam, @RequestParam(value = "more", defaultValue = "true") Boolean more) {
        PageResult<BasePostSimpleDTO> postPage = postService.pagePosts(postQueryParam);
        if (more) {
            return postService.buildPostListVO(postPage);
        }

        return postPage;
    }


    /**
     * 最新发布
     * @param top top
     * @return
     */
//    @GetMapping("latest")
//    public List<BasePostMinimalDTO> pageLatest(@RequestParam(name = "top", defaultValue = "10") int top) {
//        return postService.convertToMinimal(postService.pageLatest(top).getContent());
//    }

//    @GetMapping("status/{status}")
//    @ApiOperation("Gets a page of post by post status")
//    public Page<? extends BasePostSimpleDTO> pageByStatus(@PathVariable(name = "status") PostStatus status,
//                                                          @RequestParam(value = "more", required = false, defaultValue = "false") Boolean more,
//                                                          @PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
//        Page<Post> posts = postService.pageBy(status, pageable);
//
//        if (more) {
//            return postService.convertToListVo(posts);
//        }
//
//        return postService.convertToSimple(posts);
//    }
//
//    @GetMapping("{postId:\\d+}")
//    @ApiOperation("Gets a post")
//    public PostDetailVO getBy(@PathVariable("postId") Integer postId) {
//        Post post = postService.getById(postId);
//        return postService.convertToDetailVo(post);
//    }
//
//    @PutMapping("{postId:\\d+}/likes")
//    @ApiOperation("Likes a post")
//    public void likes(@PathVariable("postId") Integer postId) {
//        postService.increaseLike(postId);
//    }
//
//    @PostMapping
//    @ApiOperation("Creates a post")
//    public PostDetailVO createBy(@Valid @RequestBody PostParam postParam,
//                                 @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
//        // Convert to
//        Post post = postParam.convertTo();
//        return postService.createBy(post, postParam.getTagIds(), postParam.getCategoryIds(), postParam.getPostMetas(), autoSave);
//    }
//
//    @PutMapping("{postId:\\d+}")
//    @ApiOperation("Updates a post")
//    public PostDetailVO updateBy(@Valid @RequestBody PostParam postParam,
//                                 @PathVariable("postId") Integer postId,
//                                 @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
//        // Get the post info
//        Post postToUpdate = postService.getById(postId);
//
//        postParam.update(postToUpdate);
//        return postService.updateBy(postToUpdate, postParam.getTagIds(), postParam.getCategoryIds(), postParam.getPostMetas(), autoSave);
//    }
//
//    @PutMapping("{postId:\\d+}/status/{status}")
//    @ApiOperation("Updates post status")
//    public BasePostMinimalDTO updateStatusBy(
//            @PathVariable("postId") Integer postId,
//            @PathVariable("status") PostStatus status) {
//        Post post = postService.updateStatus(status, postId);
//
//        return new BasePostMinimalDTO().convertFrom(post);
//    }
//
//    @PutMapping("status/{status}")
//    @ApiOperation("Updates post status in batch")
//    public List<Post> updateStatusInBatch(@PathVariable(name = "status") PostStatus status,
//                                          @RequestBody List<Integer> ids) {
//        return postService.updateStatusByIds(ids, status);
//    }
//
//    @PutMapping("{postId:\\d+}/status/draft/content")
//    @ApiOperation("Updates draft")
//    public BasePostDetailDTO updateDraftBy(
//            @PathVariable("postId") Integer postId,
//            @RequestBody PostContentParam contentParam) {
//        // Update draft content
//        Post post = postService.updateDraftContent(contentParam.getContent(), postId);
//
//        return new BasePostDetailDTO().convertFrom(post);
//    }
//
//    @DeleteMapping("{postId:\\d+}")
//    @ApiOperation("Deletes a photo permanently")
//    public void deletePermanently(@PathVariable("postId") Integer postId) {
//        postService.removeById(postId);
//    }
//
//    @DeleteMapping
//    @ApiOperation("Deletes posts permanently in batch by id array")
//    public List<Post> deletePermanentlyInBatch(@RequestBody List<Integer> ids) {
//        return postService.removeByIds(ids);
//    }
//
//    @GetMapping(value = {"preview/{postId:\\d+}", "{postId:\\d+}/preview"})
//    @ApiOperation("Gets a post preview link")
//    public String preview(@PathVariable("postId") Integer postId) throws UnsupportedEncodingException {
//        Post post = postService.getById(postId);
//
//        post.setSlug(URLEncoder.encode(post.getSlug(), StandardCharsets.UTF_8.name()));
//
//        BasePostMinimalDTO postMinimalDTO = postService.convertToMinimal(post);
//
//        String token = IdUtil.simpleUUID();
//
//        // cache preview token
//        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);
//
//        StringBuilder previewUrl = new StringBuilder();
//
//        if (!optionService.isEnabledAbsolutePath()) {
//            previewUrl.append(optionService.getBlogBaseUrl());
//        }
//
//        previewUrl.append(postMinimalDTO.getFullPath());
//
//        if (optionService.getPostPermalinkType().equals(PostPermalinkType.ID)) {
//            previewUrl.append("&token=")
//                    .append(token);
//        } else {
//            previewUrl.append("?token=")
//                    .append(token);
//        }
//
//        // build preview post url and return
//        return previewUrl.toString();
//    }
}
