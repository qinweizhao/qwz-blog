package com.qinweizhao.blog.framework.freemarker.tag;

import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.ArticleCategoryService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Freemarker custom tag of category.
 *
 * @author qinweizhao
 * @since 2019-03-22
 */
@Component
public class CategoryTagDirective implements TemplateDirectiveModel {

    private final CategoryService categoryService;

    private final ArticleCategoryService articleCategoryService;

    public CategoryTagDirective(Configuration configuration,
                                CategoryService categoryService,
                                ArticleCategoryService articleCategoryService) {
        this.categoryService = categoryService;
        this.articleCategoryService = articleCategoryService;
        configuration.setSharedVariable("categoryTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(BlogConst.METHOD_KEY)) {
            String method = params.get(BlogConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    env.setVariable("categories", builder.build().wrap(categoryService.list(true)));
                    break;
                case "tree":
                    env.setVariable("categories", builder.build().wrap(categoryService.listAsTree()));
                    break;
                case "listByarticleId":
                    Integer articleId = Integer.parseInt(params.get("articleId").toString());
                    List<CategoryDTO> categories = articleCategoryService.listByarticleId(articleId);
                    env.setVariable("categories", builder.build().wrap(categories));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(categoryService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
}
