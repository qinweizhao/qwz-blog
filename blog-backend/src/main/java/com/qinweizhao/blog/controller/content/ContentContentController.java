package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.controller.content.model.CategoryModel;
import com.qinweizhao.blog.controller.content.model.JournalModel;
import com.qinweizhao.blog.controller.content.model.PostModel;
import com.qinweizhao.blog.controller.content.model.TagModel;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.service.ConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ryanwang
 * @author qinweizhao
 * @since 2020-01-07
 */
@Slf4j
@Controller
@AllArgsConstructor
public class ContentContentController {

    private final PostModel postModel;

    private final CategoryModel categoryModel;

    private final TagModel tagModel;

    private final JournalModel journalModel;

    private final ConfigService configService;


    /**
     * 一级路径
     *
     * @param prefix prefix
     * @param model  model
     * @return String
     */
    @GetMapping("{prefix}")
    public String content(@PathVariable("prefix") String prefix,
                          Model model) {
        // 归档
        if (configService.getArchivesPrefix().equals(prefix)) {
            return postModel.archives(model);
        }
        // 分类
        if (configService.getCategoriesPrefix().equals(prefix)) {
            return categoryModel.list(model);
        }
        // 标签
        if (configService.getTagsPrefix().equals(prefix)) {
            return tagModel.list(model);
        }
        // 日志
        if (configService.getJournalsPrefix().equals(prefix)) {
            return journalModel.list(1, model);
        }

        return null;
    }

    /**
     * 一级路径分页后
     *
     * @param prefix prefix
     * @param page   page
     * @param model  model
     * @return String
     */
    @GetMapping("{prefix}/page/{page:\\d+}")
    public String content(@PathVariable("prefix") String prefix,
                          @PathVariable(value = "page") Integer page,
                          Model model) {
        if (configService.getArchivesPrefix().equals(prefix)) {
            return postModel.archives(model);
        } else if (configService.getJournalsPrefix().equals(prefix)) {
            return journalModel.list(page, model);
        } else {
            throw new NotFoundException("Not Found");
        }
    }

    /**
     * /xx/xx.html(分类或标签的具体所属文章渲染）
     *
     * @param prefix prefix
     * @param slug   slug
     * @param model  model
     * @return String
     */
    @GetMapping("{prefix}/{slug}")
    public String content(@PathVariable("prefix") String prefix, @PathVariable("slug") String slug, Model model) {
        if (configService.getCategoriesPrefix().equals(prefix)) {
            return categoryModel.listPost(model, slug, 1);
        } else if (configService.getTagsPrefix().equals(prefix)) {
            return tagModel.listPost(model, slug, 1);
        } else {
            throw new NotFoundException("Not Found");
        }
    }

    /**
     * /categories/os/page/2.html
     *
     * @param prefix prefix
     * @param slug   slug
     * @param page   page
     * @param model  model
     * @return String
     */
    @GetMapping("{prefix}/{slug}/page/{page:\\d+}")
    public String content(@PathVariable("prefix") String prefix,
                          @PathVariable("slug") String slug,
                          @PathVariable("page") Integer page,
                          Model model) {
        if (configService.getCategoriesPrefix().equals(prefix)) {
            return categoryModel.listPost(model, slug, page);
        } else if (configService.getTagsPrefix().equals(prefix)) {
            return tagModel.listPost(model, slug, page);
        } else {
            throw new NotFoundException("Not Found");
        }
    }

}
