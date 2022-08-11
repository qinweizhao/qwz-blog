package com.qinweizhao.blog.framework.freemarker.tag;

import com.qinweizhao.blog.model.enums.CommentType;
import com.qinweizhao.blog.model.param.CommentQueryParam;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.service.CommentService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Freemarker custom tag of comment.
 *
 * @author ryanwang
 * @date 2019-03-22
 */
@Component
public class CommentTagDirective implements TemplateDirectiveModel {

    private final CommentService commentService;

    public CommentTagDirective(Configuration configuration, CommentService commentService) {
        this.commentService = commentService;
        configuration.setSharedVariable("commentTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "latest":
                    int top = Integer.parseInt(params.get("top").toString());
                    CommentQueryParam param = new CommentQueryParam();
                    param.setType(CommentType.POST);
                    param.setPage(top);
                    env.setVariable("comments", builder.build().wrap(commentService.listLatest(param)));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(commentService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
}
