package com.qinweizhao.blog.framework.freemarker.tag;

import com.qinweizhao.blog.model.dto.TagDTO;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostTagService;
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
 * @author ryanwang
 * @since 2019-03-22
 */
@Component
public class TagTagDirective implements TemplateDirectiveModel {

    private final TagService tagService;
    private final ConfigService configService;

    private final PostTagService postTagService;

    public TagTagDirective(Configuration configuration,
                           TagService tagService,
                           ConfigService configService,
                           PostTagService postTagService) {
        this.tagService = tagService;
        this.configService = configService;
        this.postTagService = postTagService;
        configuration.setSharedVariable("tagTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    env.setVariable("tags", builder.build().wrap(tagService.list()));
                    break;
                case "listByPostId":
                    Integer postId = Integer.parseInt(params.get("postId").toString());
                    List<TagDTO> tags = postTagService.listTagsByPostId(postId);
                    tags.forEach(item -> item.setFullPath(configService.buildFullPath(postId)));
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
