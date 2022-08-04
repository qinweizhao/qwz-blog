package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.post.PostDetailDTO;
import com.qinweizhao.blog.model.dto.post.PostSimpleDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.model.vo.PostDetailVO;
import com.qinweizhao.blog.model.vo.PostListVO;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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

    private final OptionService optionService;


    /**
     * 分页
     *
     * @param postQueryParam postQueryParam
     * @return PageResult
     */
    @GetMapping
    public PageResult<PostListVO> page(PostQueryParam postQueryParam) {
        PageResult<PostSimpleDTO> postPage = postService.pagePosts(postQueryParam);
        return postService.buildPostListVO(postPage);
    }

    /**
     * 最新发布
     *
     * @param top top
     * @return List
     */
    @GetMapping("latest")
    public List<PostSimpleDTO> pageLatest(@RequestParam(name = "top", defaultValue = "10") int top) {
        PostQueryParam postQueryParam = new PostQueryParam();
        postQueryParam.setSize(top);
        PageResult<PostSimpleDTO> postPage = postService.pagePosts(postQueryParam);
        return postPage.getContent();
    }

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

    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailVO
     */
    @GetMapping("{postId:\\d+}")
    public PostDetailVO get(@PathVariable("postId") Integer postId) {
        PostDetailDTO postDetail = postService.getById(postId);
        return postService.convertToDetailVo(postDetail);
    }

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

    /**
     * updateStatus
     *
     * @param postId postId
     * @param status status
     * @return Boolean
     */
    @PutMapping("{postId:\\d+}/status/{status}")
    public Boolean updateStatusBy(@PathVariable("postId") Integer postId, @PathVariable("status") PostStatus status) {
        return postService.updateStatus(status, postId);
    }
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


    /**
     * 获取帖子预览链接
     *
     * @param postId postId
     * @return String
     * @throws UnsupportedEncodingException e
     */
    @GetMapping(value = {"preview/{postId:\\d+}", "{postId:\\d+}/preview"})
    public String preview(@PathVariable("postId") Integer postId) throws UnsupportedEncodingException {
        PostDetailDTO post = postService.getById(postId);

        post.setSlug(URLEncoder.encode(post.getSlug(), StandardCharsets.UTF_8.name()));

//        PostMinimalDTO postMinimalDTO = postService.convertToMinimal(post);
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
        return null;
    }
}
