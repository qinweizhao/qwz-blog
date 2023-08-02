package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.model.convert.ArticleConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleDTO;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.param.ArticleQueryParam;
import com.qinweizhao.blog.service.ArticleService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.List;


/**
 * @author qinweizhao
 * @since 2019-03-21
 */
@Slf4j
@Controller
@AllArgsConstructor
public class ContentFeedController {

    private static final String UTF_8_SUFFIX = ";charset=UTF-8";

    private static final String XML_MEDIA_TYPE = MediaType.APPLICATION_XML_VALUE + UTF_8_SUFFIX;

    private final ArticleService articleService;

    private final FreeMarkerConfigurer freeMarker;

    /**
     * sitemap.xml.
     *
     * @param model model
     * @return sitemap xml content.
     * @throws IOException IOException
     */
    @GetMapping(value = {"sitemap", "sitemap.xml"}, produces = XML_MEDIA_TYPE)
    @ResponseBody
    public String sitemapXml(Model model) throws IOException, TemplateException {
        model.addAttribute("posts", buildPosts());
        Template template = freeMarker.getConfiguration().getTemplate("sitemap_xml.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    /**
     * sitemap.html.
     *
     * @param model model
     * @return template path: ftl/sitemap_html
     */
    @GetMapping(value = "sitemap.html")
    public String sitemapHtml(Model model) {
        model.addAttribute("posts", buildPosts());
        return "sitemap_html";
    }

    /**
     * robots.txt
     *
     * @param model model
     * @return robots.txt content
     * @throws IOException IOException
     */
    @GetMapping(value = "robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots(Model model) throws IOException, TemplateException {
        Template template = freeMarker.getConfiguration().getTemplate("robots.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    /**
     * 构建文章列表
     *
     * @return List
     */
    private List<ArticleDTO> buildPosts() {
        ArticleQueryParam param = new ArticleQueryParam();
        param.setStatus(ArticleStatus.PUBLISHED);
        PageResult<ArticleSimpleDTO> postPage = articleService.pageSimple(param);
        PageResult<ArticleDTO> posts = ArticleConvert.INSTANCE.convertDTO(postPage);
        return posts.getContent();
    }

}
