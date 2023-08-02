package com.qinweizhao.blog.controller.admin.api;

import com.google.common.collect.Lists;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleDTO;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleContentParam;
import com.qinweizhao.blog.model.param.ArticleParam;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * Post controller.
 *
 * @author qinweizhao
 * @since 2019-03-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/post")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 分页
     * <p>
     * PostListDTO extends PostSimpleDTO
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping
    public PageResult<ArticleListDTO> page(ArticleQueryParam param) {
        return articleService.page(param);
    }

    /**
     * 最新发布
     *
     * @param top top
     * @return List
     */
    @GetMapping("latest")
    public List<ArticleSimpleDTO> latest(@RequestParam(name = "top", defaultValue = "10") int top) {
        return articleService.listSimple(top);
    }

    /**
     * 详情
     *
     * @param postId postId
     * @return PostDetailVO
     */
    @GetMapping("{postId:\\d+}")
    public ArticleDTO get(@PathVariable("postId") Integer postId) {
        return articleService.getById(postId);
    }

    /**
     * 新增
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping
    public Integer save(@Valid @RequestBody ArticleParam param) {
        return articleService.save(param);
    }

    /**
     * 更新
     *
     * @param postId postId
     * @param param  param
     * @return Boolean
     */
    @PutMapping("{postId:\\d+}")
    public Boolean update(@PathVariable("postId") Integer postId, @Valid @RequestBody ArticleParam param) {
        return articleService.update(postId, param);
    }

    /**
     * updateStatus
     *
     * @param postId postId
     * @param status status
     * @return Boolean
     */
    @PutMapping("{postId:\\d+}/status/{status}")
    public Boolean updateStatusBy(@PathVariable("postId") Integer postId, @PathVariable("status") ArticleStatus status) {
        return articleService.updateStatus(status, postId);
    }

    /**
     * 批量更新文章状态
     *
     * @param status status
     * @param ids    ids
     * @return Boolean
     */
    @PutMapping("status/{status}")
    public Boolean updateStatusInBatch(@PathVariable(name = "status") ArticleStatus status, @RequestBody List<Integer> ids) {
        return articleService.updateStatusByIds(ids, status);
    }

    /**
     * 更新草稿
     *
     * @param postId       postId
     * @param contentParam contentParam
     * @return Boolean
     */
    @PutMapping("{postId:\\d+}/status/draft/content")
    public Boolean updateDraftBy(@PathVariable("postId") Integer postId, @RequestBody ArticleContentParam contentParam) {
        return articleService.updateDraftContent(contentParam.getContent(), postId);
    }

    /**
     * 删除
     *
     * @param postId postId
     * @return Boolean
     */
    @DeleteMapping("{postId:\\d+}")
    public Boolean deletePermanently(@PathVariable("postId") Integer postId) {
        return articleService.removeById(postId);
    }

    /**
     * 批量删除
     *
     * @param ids ids
     * @return Boolean
     */
    @DeleteMapping
    public Boolean deletePermanentlyInBatch(@RequestBody List<Integer> ids) {
        return articleService.removeByIds(ids);
    }

    /**
     * 获取帖子预览链接
     *
     * @param postId postId
     * @return String
     */
    @GetMapping("preview/{postId:\\d+}")
    public String preview(@PathVariable("postId") Integer postId) {
        return articleService.getPreviewUrl(postId);
    }

    @PostMapping(value = "markdown/import")
    public Boolean importMarkdowns(@RequestPart("file") MultipartFile file) {
        List<String> supportType = Lists.newArrayList("md", "markdown", "mdown");
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            throw new BadRequestException("文件名不可为空").setErrorData(filename);
        }
        String extension = FilenameUtils.getExtension(filename).toLowerCase();
        if (!supportType.contains(extension)) {
            throw new BadRequestException("不支持" + (StringUtils.isNotEmpty(extension) ? extension : "未知") + "格式的文件上传").setErrorData(filename);
        }
        return articleService.importMarkdown(file);
    }
}
