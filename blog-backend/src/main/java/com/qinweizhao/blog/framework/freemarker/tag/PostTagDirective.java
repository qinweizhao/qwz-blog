package com.qinweizhao.blog.framework.freemarker.tag;

import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.service.PostCategoryService;
import com.qinweizhao.blog.service.ArticleService;
import com.qinweizhao.blog.service.PostTagService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Freemarker custom tag of post.
 *
 * @author qinweizhoa
 * @since 2018-04-26
 */
@Component
public class PostTagDirective implements TemplateDirectiveModel {

    private final ArticleService articleService;

    private final PostTagService postTagService;

    private final PostCategoryService postCategoryService;

    public PostTagDirective(Configuration configuration,
                            ArticleService articleService,
                            PostTagService postTagService,
                            PostCategoryService postCategoryService) {
        this.articleService = articleService;
        this.postTagService = postTagService;
        this.postCategoryService = postCategoryService;
        configuration.setSharedVariable("postTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        if (params.containsKey(BlogConst.METHOD_KEY)) {
            String method = params.get(BlogConst.METHOD_KEY).toString();
            switch (method) {
                case "latest":
                    int top = Integer.parseInt(params.get("top").toString());
                    env.setVariable("posts", builder.build().wrap(articleService.listSimple(top)));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(articleService.countByStatus(ArticleStatus.PUBLISHED)));
                    break;
                case "archive":
                    String type = params.get("type").toString();
                    env.setVariable("archives", builder.build().wrap("year".equals(type) ? articleService.listYearArchives() : articleService.listMonthArchives()));
                    break;
                case "listByCategoryId":
                    Integer categoryId = Integer.parseInt(params.get("categoryId").toString());
                    env.setVariable("posts", builder.build().wrap(postCategoryService.listPostByCategoryIdAndPostStatus(categoryId, ArticleStatus.PUBLISHED)));
                    break;
                case "listByCategorySlug":
                    String categorySlug = params.get("categorySlug").toString();
                    List<ArticleSimpleDTO> posts = postCategoryService.listPostByCategorySlugAndPostStatus(categorySlug, ArticleStatus.PUBLISHED);
                    env.setVariable("posts", builder.build().wrap(posts));
                    break;
                case "listByTagId":
                    Integer tagId = Integer.parseInt(params.get("tagId").toString());
                    env.setVariable("posts", builder.build().wrap(postTagService.listPostsByTagIdAndPostStatus(tagId, ArticleStatus.PUBLISHED)));
                    break;
                case "listByTagSlug":
                    String tagSlug = params.get("tagSlug").toString();
                    env.setVariable("posts", builder.build().wrap(postTagService.listPostsByTagSlugAndPostStatus(tagSlug, ArticleStatus.PUBLISHED)));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }

}
