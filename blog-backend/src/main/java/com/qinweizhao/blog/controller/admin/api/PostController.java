package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostContentParam;
import com.qinweizhao.blog.model.param.PostParam;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
     * <p>
     * PostListDTO extends PostSimpleDTO
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping
    public PageResult<PostListDTO> page(PostQueryParam param) {
        return postService.page(param);
    }

    /**
     * 最新发布
     *
     * @param top top
     * @return List
     */
    @GetMapping("latest")
    public List<PostSimpleDTO> latest(@RequestParam(name = "top", defaultValue = "10") int top) {
        return postService.listSimple(top);
    }

    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailVO
     */
    @GetMapping("{postId:\\d+}")
    public PostDTO get(@PathVariable("postId") Integer postId) {
        return postService.getById(postId);
    }


    /**
     * 新增
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping
    public Boolean save(@Valid @RequestBody PostParam param) {
        return postService.save(param);
    }


    /**
     * 更新
     *
     * @param param  param
     * @param postId postId
     * @return Boolean
     */
    @PutMapping("{postId:\\d+}")
    public Boolean update(@Valid @RequestBody PostParam param, @PathVariable("postId") Integer postId) {
        return postService.update(postId, param);
    }

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

    /**
     * 批量更新文章状态
     * @param status status
     * @param ids ids
     * @return Boolean
     */
    @PutMapping("status/{status}")
    public Boolean updateStatusInBatch(@PathVariable(name = "status") PostStatus status,
                                          @RequestBody List<Integer> ids) {
        return postService.updateStatusByIds(ids, status);
    }


    /**
     * 更新草稿
     *
     * @param postId       postId
     * @param contentParam contentParam
     * @return Boolean
     */
    @PutMapping("{postId:\\d+}/status/draft/content")
    public Boolean updateDraftBy(@PathVariable("postId") Integer postId,@RequestBody PostContentParam contentParam) {
        return postService.updateDraftContent(contentParam.getContent(), postId);
    }


    /**
     * 删除
     *
     * @param postId postId
     * @return Boolean
     */
    @DeleteMapping("{postId:\\d+}")
    public Boolean deletePermanently(@PathVariable("postId") Integer postId) {
        return postService.removeById(postId);
    }


    /**
     * 批量删除
     *
     * @param ids ids
     * @return Boolean
     */
    @DeleteMapping
    public Boolean deletePermanentlyInBatch(@RequestBody List<Integer> ids) {
        return postService.removeByIds(ids);
    }


    /**
     * 获取帖子预览链接
     *
     * @param postId postId
     * @return String
     */
    @GetMapping(value = {"preview/{postId:\\d+}", "{postId:\\d+}/preview"})
    public String preview(@PathVariable("postId") Integer postId) {
        return postService.getPreviewUrl(postId);
    }
}
