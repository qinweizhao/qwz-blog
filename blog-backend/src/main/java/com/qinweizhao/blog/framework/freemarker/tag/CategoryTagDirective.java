package com.qinweizhao.blog.framework.freemarker.tag;

import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.PostCategoryService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Freemarker custom tag of category.
 *
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-22
 */
@Component
public class CategoryTagDirective implements TemplateDirectiveModel {

    private final CategoryService categoryService;

    private final PostCategoryService postCategoryService;

    public CategoryTagDirective(Configuration configuration,
                                CategoryService categoryService,
                                PostCategoryService postCategoryService) {
        this.categoryService = categoryService;
        this.postCategoryService = postCategoryService;
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
                case "listByPostId":
                    Integer postId = Integer.parseInt(params.get("postId").toString());
                    List<CategoryDTO> categories = postCategoryService.listByPostId(postId);
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
