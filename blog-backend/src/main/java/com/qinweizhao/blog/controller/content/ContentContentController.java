package com.qinweizhao.blog.controller.content;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.qinweizhao.blog.controller.content.model.CategoryModel;
import com.qinweizhao.blog.controller.content.model.PostModel;
import com.qinweizhao.blog.controller.content.model.TagModel;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.model.constant.SystemConstant;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
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

    private final ConfigService configService;

    private final PostService postService;


    /**
     * 一级路径
     *
     * @param prefix prefix
     * @param model  model
     * @return String
     */
    @GetMapping("{prefix}")
    public String content(@PathVariable("prefix") String prefix, Model model) {
        log.debug("执行");
        // 归档
        if (SystemConstant.ARCHIVES_PREFIX.equals(prefix)) {
            return postModel.archives(model);
        }
        // 分类
        if (SystemConstant.CATEGORIES_PREFIX.equals(prefix)) {
            return categoryModel.list(model);
        }
        // 标签
        if (SystemConstant.TAGS_PREFIX.equals(prefix)) {
            return tagModel.list(model);
        }

        throw buildPathNotFoundException();
    }


    /**
     * 构建路径不存在异常
     *
     * @return NotFoundException
     */
    private NotFoundException buildPathNotFoundException() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        String requestUri = "";
        if (requestAttributes instanceof ServletRequestAttributes) {
            requestUri = ((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI();
        }
        return new NotFoundException("无法定位到该路径：" + requestUri);
    }


    /**
     * @param prefix prefix
     * @param target target
     * @param model  model
     * @return String
     */
    @GetMapping("{prefix}/{target}")
    public String content(@PathVariable("prefix") String prefix, @PathVariable("target") String target, @RequestParam(value = "token", required = false) String token, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page, Model model) {
        if (SystemConstant.CATEGORIES_PREFIX.equals(prefix)) {
            return categoryModel.listPost(model, target, page);
        } else if (SystemConstant.TAGS_PREFIX.equals(prefix)) {
            return tagModel.listPost(model, target, page);
        } else if (SystemConstant.ARTICLE_PREFIX.equals(prefix)) {
            PostStatus status;
            try {
                int postId = Integer.parseInt(target);
                status = postService.getStatusById(postId);
                Assert.notNull(status, "文章不存在");
            } catch (Exception e) {
                throw new NotFoundException("Not Found");
            }
            return postModel.content(Integer.parseInt(target), status, token, model);
        } else {
            throw new NotFoundException("Not Found");
        }
    }


}
