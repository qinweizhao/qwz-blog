package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.param.TagParam;
import com.qinweizhao.blog.service.PostTagService;
import com.qinweizhao.blog.service.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Tag controller.
 *
 * @author johnniang
 * @since 3/20/19
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/tags")
public class TagController {

    private final TagService tagService;

    /**
     * 列表
     *
     * @return List
     */
    @GetMapping
    public List<TagDTO> listTags() {
        return tagService.list();
    }

    /**
     * 新增
     *
     * @param tagParam tagParam
     * @return Boolean
     */
    @PostMapping
    public Boolean create(@Valid @RequestBody TagParam tagParam) {
        return tagService.save(tagParam);
    }

    /**
     * 详情
     *
     * @param tagId tagId
     * @return TagDTO
     */
    @GetMapping("{tagId:\\d+}")
    public TagDTO getBy(@PathVariable("tagId") Integer tagId) {
        return tagService.getById(tagId);
    }

    /**
     * 更新
     *
     * @param tagId    tagId
     * @param tagParam tagParam
     * @return TagDTO
     */
    @PutMapping("{tagId:\\d+}")
    public Boolean updateBy(@PathVariable("tagId") Integer tagId, @Valid @RequestBody TagParam tagParam) {
        return tagService.updateById(tagId, tagParam);
    }

    /**
     * 删除标签
     *
     * @param tagId tagId
     * @return Boolean
     */
    @DeleteMapping("{tagId:\\d+}")
    public Boolean deletePermanently(@PathVariable("tagId") Integer tagId) {
        return tagService.removeById(tagId);
    }
}
