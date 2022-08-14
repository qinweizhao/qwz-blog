package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.controller.content.model.PostModel;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

/**
 * Blog index page controller
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-17
 */
@Slf4j
@Controller
@AllArgsConstructor
public class ContentIndexController {

    private final PostService postService;

    private final PostModel postModel;


    /**
     * Render blog index
     *
     * @param p     post id
     * @param model model
     * @return template path: themes/{theme}/index.ftl
     */
    @GetMapping
    public String index(Integer p, String token, Model model) {

        if (!Objects.isNull(p)) {
            PostStatus status = postService.getStatusById(p);
            return postModel.content(p, status, token, model);
        }

        return this.index(model, 1);
    }

    /**
     * 首页
     *
     * @param model model
     * @param page  current page number
     * @return template path: themes/{theme}/index.ftl
     */
    @GetMapping(value = "page/{page}")
    public String index(Model model, @PathVariable(value = "page") Integer page) {
        System.out.println("postModel.list(page, model) = " + postModel.list(page, model));
        return postModel.list(page, model);
    }
}
