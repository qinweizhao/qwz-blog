package com.qinweizhao.blog.framework.freemarker.tag;

import cn.hutool.core.util.PageUtil;
import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.model.support.Pagination;
import com.qinweizhao.blog.model.support.RainbowPage;
import com.qinweizhao.blog.service.ConfigService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.qinweizhao.blog.model.support.BlogConst.URL_SEPARATOR;

/**
 * @author ryanwang
 * @since 2020-03-07
 */
@Component
public class PaginationTagDirective implements TemplateDirectiveModel {

    private static final String URL_PAGE_PARAM = "?page=";

    private final ConfigService configService;

    public PaginationTagDirective(Configuration configuration, ConfigService configService) {
        this.configService = configService;
        configuration.setSharedVariable("paginationTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        if (params.containsKey(BlogConst.METHOD_KEY)) {
            // 获取参数
            String method = params.get(BlogConst.METHOD_KEY).toString();

            int page = Integer.parseInt(params.get("page").toString());
            int total = Integer.parseInt(params.get("total").toString());
            int display = Integer.parseInt(params.get("display").toString());

            Pagination pagination = new Pagination();

            // 生成下一页完整路径和前一页完整路径
            StringBuilder nextPageFullPath = new StringBuilder();
            StringBuilder prevPageFullPath = new StringBuilder();

            nextPageFullPath.append(configService.getBlogBaseUrl());
            prevPageFullPath.append(configService.getBlogBaseUrl());

            int[] rainbow = PageUtil.rainbow(page, total, display);

            List<RainbowPage> rainbowPages = new ArrayList<>();
            StringBuilder fullPath = new StringBuilder();

            fullPath.append(configService.getBlogBaseUrl());

            switch (method) {
                case "index":

                    nextPageFullPath.append(URL_PAGE_PARAM).append(page + 1);

                    if (page == 1) {
                        prevPageFullPath.append(URL_SEPARATOR);
                    } else {
                        prevPageFullPath.append(URL_PAGE_PARAM).append(page - 1);
                    }

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(URL_PAGE_PARAM + current);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "search":
                    String keyword = params.get("keyword").toString();

                    nextPageFullPath.append(URL_SEPARATOR).append("search");
                    prevPageFullPath.append(URL_SEPARATOR).append("search");

                    nextPageFullPath.append(URL_PAGE_PARAM).append(page + 1).append("&keyword=").append(keyword);

                    if (page == 1) {
                        prevPageFullPath.append("?keyword=").append(keyword);
                    } else {
                        prevPageFullPath.append(URL_PAGE_PARAM).append(page - 1).append("&keyword=").append(keyword);
                    }

                    fullPath.append(URL_SEPARATOR).append("search");

                    fullPath.append(URL_PAGE_PARAM);

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + "&keyword=" + keyword);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "tagPosts":
                    String tagSlug = params.get("slug").toString();

                    nextPageFullPath.append(URL_SEPARATOR).append(configService.getTagsPrefix()).append(URL_SEPARATOR).append(tagSlug);
                    prevPageFullPath.append(URL_SEPARATOR).append(configService.getTagsPrefix()).append(URL_SEPARATOR).append(tagSlug);

                    nextPageFullPath.append(URL_PAGE_PARAM).append(page + 1);

                    if (page != 1) {

                        prevPageFullPath.append(URL_PAGE_PARAM).append(page - 1);
                    }

                    fullPath.append(URL_SEPARATOR).append(configService.getTagsPrefix()).append(URL_SEPARATOR).append(tagSlug);

                    fullPath.append(URL_PAGE_PARAM);

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "categoryPosts":
                    String categorySlug = params.get("slug").toString();

                    nextPageFullPath.append(URL_SEPARATOR).append(configService.getCategoriesPrefix()).append(URL_SEPARATOR).append(categorySlug);
                    prevPageFullPath.append(URL_SEPARATOR).append(configService.getCategoriesPrefix()).append(URL_SEPARATOR).append(categorySlug);

                    nextPageFullPath.append(URL_PAGE_PARAM).append(page + 1);

                    if (page != 1) {

                        prevPageFullPath.append(URL_PAGE_PARAM).append(page - 1);
                    }

                    fullPath.append(URL_SEPARATOR).append(configService.getCategoriesPrefix()).append(URL_SEPARATOR).append(categorySlug);

                    fullPath.append(URL_PAGE_PARAM);

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "journals":

                    nextPageFullPath.append(URL_SEPARATOR).append(configService.getJournalsPrefix());
                    prevPageFullPath.append(URL_SEPARATOR).append(configService.getJournalsPrefix());

                    nextPageFullPath.append(URL_PAGE_PARAM).append(page + 1);

                    if (page != 1) {
                        prevPageFullPath.append(URL_PAGE_PARAM).append(page - 1);
                    }

                    fullPath.append(URL_SEPARATOR).append(configService.getJournalsPrefix());

                    fullPath.append(URL_PAGE_PARAM);

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                default:
                    break;
            }
            pagination.setNextPageFullPath(nextPageFullPath.toString());
            pagination.setPrevPageFullPath(prevPageFullPath.toString());
            pagination.setRainbowPages(rainbowPages);
            pagination.setHasNext(total != page);
            pagination.setHasPrev(page != 1);
            env.setVariable("pagination", builder.build().wrap(pagination));
        }
        body.render(env.getOut());
    }
}
