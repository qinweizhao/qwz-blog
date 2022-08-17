package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.model.convert.PostConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.PostService;
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
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-21
 */
@Slf4j
@Controller
@AllArgsConstructor
public class ContentFeedController {

    private final static String UTF_8_SUFFIX = ";charset=UTF-8";

    private final static String XML_MEDIA_TYPE = MediaType.APPLICATION_XML_VALUE + UTF_8_SUFFIX;

    private final PostService postService;

    private final FreeMarkerConfigurer freeMarker;

    /**
     * 获取 sitemap.xml.
     *
     * @param model model
     * @return sitemap xml content.
     * @throws IOException IOException
     */
    @GetMapping(value = {"sitemap", "sitemap.xml"}, produces = XML_MEDIA_TYPE)
    @ResponseBody
    public String sitemapXml(Model model) throws IOException, TemplateException {
        model.addAttribute("posts", buildPosts());
        Template template = freeMarker.getConfiguration().getTemplate("common/web/sitemap_xml.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    /**
     * Get sitemap.html.
     *
     * @param model model
     * @return template path: common/web/sitemap_html
     */
    @GetMapping(value = "sitemap.html")
    public String sitemapHtml(Model model) {
        model.addAttribute("posts", buildPosts());
        return "common/web/sitemap_html";
    }

    /**
     * Get robots.txt
     *
     * @param model model
     * @return robots.txt content
     * @throws IOException IOException
     */
    @GetMapping(value = "robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots(Model model) throws IOException, TemplateException {
        Template template = freeMarker.getConfiguration().getTemplate("common/web/robots.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }


    /**
     * Build posts.
     *
     * @return list of post detail vo
     */
    private List<PostDTO> buildPosts() {
        PostQueryParam param = new PostQueryParam();
        param.setStatus(PostStatus.PUBLISHED);
        PageResult<PostSimpleDTO> postPage = postService.pageSimple(param);
        PageResult<PostDTO> posts = PostConvert.INSTANCE.convertDTO(postPage);
        return posts.getContent();
    }

}
