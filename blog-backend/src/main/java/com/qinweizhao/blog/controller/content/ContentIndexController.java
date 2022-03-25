package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.controller.content.model.PostModel;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostPermalinkType;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Blog index page controller
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-17
 */
@Slf4j
@Controller
@RequestMapping
public class ContentIndexController {

    @Resource
    private PostService postService;

    @Resource
    private OptionService optionService;

    @Resource
    private PostModel postModel;


    /**
     * Render blog index
     *
     * @param p     post id
     * @param model model
     * @return template path: themes/{theme}/index.ftl
     */
    @GetMapping
    public String index(Integer p, String token, Model model) {

        PostPermalinkType permalinkType = optionService.getPostPermalinkType();

        if (PostPermalinkType.ID.equals(permalinkType) && !Objects.isNull(p)) {
            Post post = postService.getById(p);
            System.out.println("postModel.content(post, token, model) = " + postModel.content(post, token, model));
            return postModel.content(post, token, model);
        }

        return this.index(model, 1);
    }

    /**
     * Render blog index
     *
     * @param model model
     * @param page  current page number
     * @return template path: themes/{theme}/index.ftl
     */
    @GetMapping(value = "page/{page}")
    public String index(Model model,
                        @PathVariable(value = "page") Integer page) {
        System.out.println("postModel.list(page, model) = " + postModel.list(page, model));
        return postModel.list(page, model);
    }
}
