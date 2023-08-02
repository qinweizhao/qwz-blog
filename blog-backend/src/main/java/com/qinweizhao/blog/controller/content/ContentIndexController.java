package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.controller.content.model.ArticleModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Blog index page controller
 *
 * @author qinweizhao
 * @since 2019-03-17
 */
@Slf4j
@Controller
@AllArgsConstructor
public class ContentIndexController {

    private final ArticleModel articleModel;

    /**
     * 渲染主页
     *
     * @param page  page
     * @param model model
     * @return template : index.ftl
     */
    @GetMapping
    public String index(@RequestParam(required = false, defaultValue = "1") Integer page, Model model) {
        return articleModel.list(page, model);
    }

}
