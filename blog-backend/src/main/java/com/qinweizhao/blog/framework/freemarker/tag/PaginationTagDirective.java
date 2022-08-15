package com.qinweizhao.blog.framework.freemarker.tag;

import cn.hutool.core.util.PageUtil;
import com.qinweizhao.blog.model.support.HaloConst;
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

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

/**
 * @author ryanwang
 * @since 2020-03-07
 */
@Component
public class PaginationTagDirective implements TemplateDirectiveModel {

    private final ConfigService configService;

    public PaginationTagDirective(Configuration configuration,
                                  ConfigService configService) {
        this.configService = configService;
        configuration.setSharedVariable("paginationTag", this);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        final DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
        if (params.containsKey(HaloConst.METHOD_KEY)) {
            // 获取参数
            String method = params.get(HaloConst.METHOD_KEY).toString();

            int page = Integer.parseInt(params.get("page").toString());
            int total = Integer.parseInt(params.get("total").toString());
            int display = Integer.parseInt(params.get("display").toString());

            Pagination pagination = new Pagination();

            // 生成下一页完整路径和前一页完整路径
            StringBuilder nextPageFullPath = new StringBuilder();
            StringBuilder prevPageFullPath = new StringBuilder();

            if (configService.isEnabledAbsolutePath()) {
                nextPageFullPath.append(configService.getBlogBaseUrl());
                prevPageFullPath.append(configService.getBlogBaseUrl());
            }

            int[] rainbow = PageUtil.rainbow(page, total, display);

            List<RainbowPage> rainbowPages = new ArrayList<>();
            StringBuilder fullPath = new StringBuilder();

            if (configService.isEnabledAbsolutePath()) {
                fullPath.append(configService.getBlogBaseUrl());
            }

            String pathSuffix = configService.getPathSuffix();

            switch (method) {
                case "index":

                    nextPageFullPath.append("/page/")
                            .append(page + 1)
                            .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(URL_SEPARATOR);
                    } else {
                        prevPageFullPath.append("/page/")
                                .append(page)
                                .append(pathSuffix);
                    }

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "archives":

                    nextPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getArchivesPrefix());
                    prevPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getArchivesPrefix());

                    nextPageFullPath.append("/page/")
                            .append(page + 1)
                            .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.
                                append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                                .append(page)
                                .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(configService.getArchivesPrefix());

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "search":
                    String keyword = params.get("keyword").toString();

                    nextPageFullPath.append(URL_SEPARATOR)
                            .append("search");
                    prevPageFullPath.append(URL_SEPARATOR)
                            .append("search");

                    nextPageFullPath.append("/page/")
                            .append(page)
                            .append(pathSuffix)
                            .append("?keyword=")
                            .append(keyword);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix)
                                .append("?keyword=")
                                .append(keyword);
                    } else {
                        prevPageFullPath.append("/page/")
                                .append(page)
                                .append(pathSuffix)
                                .append("?keyword=")
                                .append(keyword);
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append("search");

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix + "?keyword=" + keyword);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "tagPosts":
                    String tagSlug = params.get("slug").toString();

                    nextPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getTagsPrefix())
                            .append(URL_SEPARATOR)
                            .append(tagSlug);
                    prevPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getTagsPrefix())
                            .append(URL_SEPARATOR)
                            .append(tagSlug);

                    nextPageFullPath.append("/page/")
                            .append(page + 1)
                            .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                                .append(page)
                                .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(configService.getTagsPrefix())
                            .append(URL_SEPARATOR)
                            .append(tagSlug);

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "categoryPosts":
                    String categorySlug = params.get("slug").toString();

                    nextPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getCategoriesPrefix())
                            .append(URL_SEPARATOR)
                            .append(categorySlug);
                    prevPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getCategoriesPrefix())
                            .append(URL_SEPARATOR)
                            .append(categorySlug);

                    nextPageFullPath.append("/page/")
                            .append(page + 1)
                            .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                                .append(page)
                                .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(configService.getCategoriesPrefix())
                            .append(URL_SEPARATOR)
                            .append(categorySlug);

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "photos":

                    nextPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getPhotosPrefix());
                    prevPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getPhotosPrefix());

                    nextPageFullPath.append("/page/")
                            .append(page + 1)
                            .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                                .append(page)
                                .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(configService.getPhotosPrefix());

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
                        rainbowPage.setIsCurrent(page == current);
                        rainbowPages.add(rainbowPage);
                    }
                    break;
                case "journals":

                    nextPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getJournalsPrefix());
                    prevPageFullPath.append(URL_SEPARATOR)
                            .append(configService.getJournalsPrefix());

                    nextPageFullPath.append("/page/")
                            .append(page + 1)
                            .append(pathSuffix);

                    if (page == 1) {
                        prevPageFullPath.append(pathSuffix);
                    } else {
                        prevPageFullPath.append("/page/")
                                .append(page)
                                .append(pathSuffix);
                    }

                    fullPath.append(URL_SEPARATOR)
                            .append(configService.getJournalsPrefix());

                    fullPath.append("/page/");

                    for (int current : rainbow) {
                        RainbowPage rainbowPage = new RainbowPage();
                        rainbowPage.setPage(current);
                        rainbowPage.setFullPath(fullPath.toString() + current + pathSuffix);
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
            pagination.setHasPrev(page != 0);
            env.setVariable("pagination", builder.build().wrap(pagination));
        }
        body.render(env.getOut());
    }
}
