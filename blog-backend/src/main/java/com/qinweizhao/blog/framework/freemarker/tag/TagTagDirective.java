package com.qinweizhao.blog.framework.freemarker.tag;

import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.ArticleTagService;
import com.qinweizhao.blog.service.TagService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Freemarker custom tag of tag.
 *
 * @author qinweizhoa
 * @since 2019-03-22
 */
@Component
public class TagTagDirective implements TemplateDirectiveModel {

    private final TagService tagService;
    private final SettingService settingService;

    private final ArticleTagService articleTagService;

    public TagTagDirective(Configuration configuration,
                           TagService tagService,
                           SettingService settingService,
                           ArticleTagService articleTagService) {
        this.tagService = tagService;
        this.settingService = settingService;
        this.articleTagService = articleTagService;
        configuration.setSharedVariable("tagTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(BlogConst.METHOD_KEY)) {
            String method = params.get(BlogConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    env.setVariable("tags", builder.build().wrap(tagService.list()));
                    break;
                case "listByPostId":
                    Integer postId = Integer.parseInt(params.get("postId").toString());
                    List<TagDTO> tags = articleTagService.listTagsByPostId(postId);
                    tags.forEach(item -> item.setFullPath(settingService.buildFullPath(postId)));
                    env.setVariable("tags", builder.build().wrap(tags));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(tagService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
}
