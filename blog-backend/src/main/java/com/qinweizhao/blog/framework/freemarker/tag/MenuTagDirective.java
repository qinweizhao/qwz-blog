package com.qinweizhao.blog.framework.freemarker.tag;

import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.MenuService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * Freemarker custom tag of menu.
 *
 * @author qinweizhoa
 * @since 2019-03-22
 */
@Component
public class MenuTagDirective implements TemplateDirectiveModel {

    private final MenuService menuService;

    private final SettingService settingService;

    public MenuTagDirective(Configuration configuration, MenuService menuService, SettingService settingService) {
        this.menuService = menuService;
        this.settingService = settingService;
        configuration.setSharedVariable("menuTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(BlogConst.METHOD_KEY)) {
            String method = params.get(BlogConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    env.setVariable("menus", builder.build().wrap(menuService.listByTeam("")));
                    break;
                case "tree":
                    env.setVariable("menus", builder.build().wrap(menuService.listByTeamAsTree("")));
                    break;
                case "listTeams":
                    env.setVariable("teams", builder.build().wrap(menuService.listTeamVO()));
                    break;
                case "listByTeam":
                    String team = params.get("team").toString();
                    env.setVariable("menus", builder.build().wrap(menuService.listByTeam(team)));
                    break;
                case "treeByTeam":
                    String treeTeamParam = params.get("team").toString();
                    env.setVariable("menus", builder.build().wrap(menuService.listByTeamAsTree(treeTeamParam)));
                    break;
                case "count":
                    env.setVariable("count", builder.build().wrap(menuService.count()));
                    break;
                default:
                    break;
            }
        }
        body.render(env.getOut());
    }
}
