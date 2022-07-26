package com.qinweizhao.blog.framework.core.freemarker.tag;

import com.qinweizhao.blog.model.properties.PrimaryProperties;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.service.MenuService;
import com.qinweizhao.blog.service.OptionService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * Freemarker custom tag of menu.
 *
 * @author ryanwang
 * @date 2019-03-22
 */
@Component
public class MenuTagDirective implements TemplateDirectiveModel {

    private static final String METHOD_KEY = "method";

    private final MenuService menuService;

    private final OptionService optionService;

    public MenuTagDirective(Configuration configuration, MenuService menuService, OptionService optionService) {
        this.menuService = menuService;
        this.optionService = optionService;
        configuration.setSharedVariable("menuTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

        if (params.containsKey(HaloConst.METHOD_KEY)) {
            String method = params.get(HaloConst.METHOD_KEY).toString();
            switch (method) {
                case "list":
                    String listTeam = optionService.getByPropertyOrDefault(PrimaryProperties.DEFAULT_MENU_TEAM, String.class, "");
                    env.setVariable("menus", builder.build().wrap(menuService.listByTeam(listTeam)));
                    break;
                case "tree":
                    String treeTeam = optionService.getByPropertyOrDefault(PrimaryProperties.DEFAULT_MENU_TEAM, String.class, "");
                    env.setVariable("menus", builder.build().wrap(menuService.listByTeamAsTree(treeTeam)));
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
