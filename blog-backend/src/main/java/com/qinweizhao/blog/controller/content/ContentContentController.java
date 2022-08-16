package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.controller.content.model.CategoryModel;
import com.qinweizhao.blog.controller.content.model.JournalModel;
import com.qinweizhao.blog.controller.content.model.PostModel;
import com.qinweizhao.blog.controller.content.model.TagModel;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostPermalinkType;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostService;
import com.qinweizhao.blog.service.SheetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    private final PostService postService;

    private final SheetService sheetService;

    private final AbstractStringCacheStore cacheStore;


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
        if (configService.getTagsPrefix().equals(prefix)) {
            return tagModel.list(model);
        }
        if (configService.getJournalsPrefix().equals(prefix)) {
            return journalModel.list(1, model);
        }

        return null;
    }

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

//    @GetMapping("{prefix}/{slug}")
//    public String content(@PathVariable("prefix") String prefix,
//                          @PathVariable("slug") String slug,
//                          @RequestParam(value = "token", required = false) String token,
//                          Model model) {
//        PostPermalinkType postPermalinkType = configService.getPostPermalinkType();
//
//        if (postPermalinkType.equals(PostPermalinkType.DEFAULT) && configService.getArchivesPrefix().equals(prefix)) {
//            Post post = postService.getBySlug(slug);
//            return postModel.content(post, token, model);
//        } else if (postPermalinkType.equals(PostPermalinkType.YEAR) && prefix.length() == 4 && StringUtils.isNumeric(prefix)) {
//            Post post = postService.getBy(Integer.parseInt(prefix), slug);
//            return postModel.content(post, token, model);
//        } else if (configService.getSheetPrefix().equals(prefix)) {
//            Sheet sheet = sheetService.getBySlug(slug);
//            return sheetModel.content(sheet, token, model);
//        } else if (configService.getCategoriesPrefix().equals(prefix)) {
//            return categoryModel.listPost(model, slug, 1);
//        } else if (configService.getTagsPrefix().equals(prefix)) {
//            return tagModel.listPost(model, slug, 1);
//        } else {
//            throw new NotFoundException("Not Found");
//        }
//    }
//
//    @GetMapping("{prefix}/{slug}/page/{page:\\d+}")
//    public String content(@PathVariable("prefix") String prefix,
//                          @PathVariable("slug") String slug,
//                          @PathVariable("page") Integer page,
//                          Model model) {
//        if (configService.getCategoriesPrefix().equals(prefix)) {
//            return categoryModel.listPost(model, slug, page);
//        } else if (configService.getTagsPrefix().equals(prefix)) {
//            return tagModel.listPost(model, slug, page);
//        } else {
//            throw new NotFoundException("Not Found");
//        }
//    }
//
//    @GetMapping("{year:\\d+}/{month:\\d+}/{slug}")
//    public String content(@PathVariable("year") Integer year,
//                          @PathVariable("month") Integer month,
//                          @PathVariable("slug") String slug,
//                          @RequestParam(value = "token", required = false) String token,
//                          Model model) {
//        PostPermalinkType postPermalinkType = configService.getPostPermalinkType();
//        if (postPermalinkType.equals(PostPermalinkType.DATE)) {
//            Post post = postService.getBy(year, month, slug);
//            return postModel.content(post, token, model);
//        } else {
//            throw new NotFoundException("Not Found");
//        }
//    }
//
//    @GetMapping("{year:\\d+}/{month:\\d+}/{day:\\d+}/{slug}")
//    public String content(@PathVariable("year") Integer year,
//                          @PathVariable("month") Integer month,
//                          @PathVariable("day") Integer day,
//                          @PathVariable("slug") String slug,
//                          @RequestParam(value = "token", required = false) String token,
//                          Model model) {
//        PostPermalinkType postPermalinkType = configService.getPostPermalinkType();
//        if (postPermalinkType.equals(PostPermalinkType.DAY)) {
//            Post post = postService.getBy(year, month, day, slug);
//            return postModel.content(post, token, model);
//        } else {
//            throw new NotFoundException("Not Found");
//        }
//    }
//
//    @PostMapping(value = "archives/{slug:.*}/password")
//    @CacheLock(traceRequest = true, expired = 2)
//    public String password(@PathVariable("slug") String slug,
//                           @RequestParam(value = "password") String password) throws UnsupportedEncodingException {
//        Post post = postService.getBy(PostStatus.INTIMATE, slug);
//
//        post.setSlug(URLEncoder.encode(post.getSlug(), StandardCharsets.UTF_8.name()));
//
//        BasePostMinimalDTO postMinimalDTO = postService.convertToMinimal(post);
//
//        StringBuilder redirectUrl = new StringBuilder();
//
//        if (!configService.isEnabledAbsolutePath()) {
//            redirectUrl.append(configService.getBlogBaseUrl());
//        }
//
//        redirectUrl.append(postMinimalDTO.getFullPath());
//
//        if (password.equals(post.getPassword())) {
//            String token = IdUtil.simpleUUID();
//            cacheStore.putAny(token, token, 10, TimeUnit.SECONDS);
//
//            if (configService.getPostPermalinkType().equals(PostPermalinkType.ID)) {
//                redirectUrl.append("&token=")
//                        .append(token);
//            } else {
//                redirectUrl.append("?token=")
//                        .append(token);
//            }
//        }
//
//        return "redirect:" + redirectUrl;
//    }
}
