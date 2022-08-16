package com.qinweizhao.blog.controller.content;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.convert.PostConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostListDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.param.PostQueryParam;
import com.qinweizhao.blog.service.CategoryService;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.PostCategoryService;
import com.qinweizhao.blog.service.PostService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
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

    private final static String XML_INVALID_CHAR = "[\\x00-\\x1F\\x7F]";

    private final static String XML_MEDIA_TYPE = MediaType.APPLICATION_XML_VALUE + UTF_8_SUFFIX;

    private final PostService postService;

    private final CategoryService categoryService;

    private final PostCategoryService postCategoryService;

    private final ConfigService configService;

    private final FreeMarkerConfigurer freeMarker;

//    /**
//     * Get post rss
//     *
//     * @param model model
//     * @return rss xml content
//     * @throws IOException       throw IOException
//     * @throws TemplateException throw TemplateException
//     */
//    @GetMapping(value = {"feed", "feed.xml", "rss", "rss.xml"}, produces = XML_MEDIA_TYPE)
//    @ResponseBody
//    public String feed(Model model) throws IOException, TemplateException {
//        int rssPageSize = configService.getRssPageSize();
//        PostQueryParam param = new PostQueryParam();
//        param.setSize(rssPageSize);
//        model.addAttribute("posts", buildPosts(param));
//        Template template = freeMarker.getConfiguration().getTemplate("common/web/rss.ftl");
//        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//    }
//
//    /**
//     * Get category post rss.
//     *
//     * @param model model
//     * @param slug  slug
//     * @return rss xml content
//     * @throws IOException       throw IOException
//     * @throws TemplateException throw TemplateException
//     */
//    @GetMapping(value = {"feed/categories/{slug}", "feed/categories/{slug}.xml"}, produces = XML_MEDIA_TYPE)
//    @ResponseBody
//    public String feed(Model model, @PathVariable(name = "slug") String slug) throws IOException, TemplateException {
//        Category category = categoryService.getBySlugOfNonNull(slug);
//        CategoryDTO categoryDTO = categoryService.convertTo(category);
//        model.addAttribute("category", categoryDTO);
//        model.addAttribute("posts", buildCategoryPosts(buildPostPageable(configService.getRssPageSize()), categoryDTO));
//        Template template = freeMarker.getConfiguration().getTemplate("common/web/rss.ftl");
//        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//    }
//
//    /**
//     * Get atom.xml
//     *
//     * @param model model
//     * @return atom xml content
//     * @throws IOException       IOException
//     * @throws TemplateException TemplateException
//     */
//    @GetMapping(value = {"atom", "atom.xml"}, produces = XML_MEDIA_TYPE)
//    @ResponseBody
//    public String atom(Model model) throws IOException, TemplateException {
//        model.addAttribute("posts", buildPosts(buildPostPageable(configService.getRssPageSize())));
//        Template template = freeMarker.getConfiguration().getTemplate("common/web/atom.ftl");
//        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//    }
//
//    /**
//     * Get category posts atom.xml
//     *
//     * @param model model
//     * @param slug  slug
//     * @return atom xml content
//     * @throws IOException       throw IOException
//     * @throws TemplateException throw TemplateException
//     */
//    @GetMapping(value = {"atom/categories/{slug}", "atom/categories/{slug}.xml"}, produces = XML_MEDIA_TYPE)
//    @ResponseBody
//    public String atom(Model model, @PathVariable(name = "slug") String slug) throws IOException, TemplateException {
//        Category category = categoryService.getBySlugOfNonNull(slug);
//        CategoryDTO categoryDTO = categoryService.convertTo(category);
//        model.addAttribute("category", categoryDTO);
//        model.addAttribute("posts", buildCategoryPosts(buildPostPageable(configService.getRssPageSize()), categoryDTO));
//        Template template = freeMarker.getConfiguration().getTemplate("common/web/atom.ftl");
//        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//    }
//
//    /**
//     * Get sitemap.xml.
//     *
//     * @param model model
//     * @return sitemap xml content.
//     * @throws IOException       IOException
//     * @throws TemplateException TemplateException
//     */
//    @GetMapping(value = {"sitemap", "sitemap.xml"}, produces = XML_MEDIA_TYPE)
//    @ResponseBody
//    public String sitemapXml(Model model,
//                             @PageableDefault(size = Integer.MAX_VALUE, sort = "createTime", direction = DESC) Pageable pageable) throws IOException, TemplateException {
//        model.addAttribute("posts", buildPosts(pageable));
//        Template template = freeMarker.getConfiguration().getTemplate("common/web/sitemap_xml.ftl");
//        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//    }
//
//    /**
//     * Get sitemap.html.
//     *
//     * @param model model
//     * @return template path: common/web/sitemap_html
//     */
//    @GetMapping(value = "sitemap.html")
//    public String sitemapHtml(Model model,
//                              @PageableDefault(size = Integer.MAX_VALUE, sort = "createTime", direction = DESC) Pageable pageable) {
//        model.addAttribute("posts", buildPosts(pageable));
//        return "common/web/sitemap_html";
//    }
//
//    /**
//     * Get robots.txt
//     *
//     * @param model model
//     * @return robots.txt content
//     * @throws IOException       IOException
//     * @throws TemplateException TemplateException
//     */
//    @GetMapping(value = "robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
//    @ResponseBody
//    public String robots(Model model) throws IOException, TemplateException {
//        Template template = freeMarker.getConfiguration().getTemplate("common/web/robots.ftl");
//        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
//    }
//
//    /**
//     * Builds page info for post.
//     *
//     * @param size page size
//     * @return page info
//     */
//    @NonNull
//    private Pageable buildPostPageable(int size) {
//        return PageRequest.of(0, size, Sort.by(DESC, "createTime"));
//    }
//
//
//    /**
//     * Build posts.
//     *
//     * @return list of post detail vo
//     */
//    private List<PostDTO> buildPosts(PostQueryParam param) {
//        param.setStatus(PostStatus.PUBLISHED);
//        PageResult<PostSimpleDTO> postPage = postService.pageSimple(param);
//
//        PageResult<PostDTO> posts = PostConvert.INSTANCE.convertDTO(postPage);
//        posts.getContent().forEach(postDTO -> {
//            postDTO.setFormatContent(RegExUtils.replaceAll(postDTO.getFormatContent(), XML_INVALID_CHAR, ""));
//            postDTO.setSummary(RegExUtils.replaceAll(postDTO.getSummary(), XML_INVALID_CHAR, ""));
//        });
//        return posts.getContent();
//    }
//
//    /**
//     * Build category posts.
//     *
//     * @param pageable pageable must not be null.
//     * @param category category
//     * @return list of post detail vo.
//     */
//    private List<PostDetailVO> buildCategoryPosts(@NonNull Pageable pageable, @NonNull CategoryDTO category) {
//        Assert.notNull(pageable, "Pageable must not be null");
//        Assert.notNull(category, "Category slug must not be null");
//
//        Page<Post> postPage = postCategoryService.pagePostBy(category.getId(), PostStatus.PUBLISHED, pageable);
//        Page<PostDetailVO> posts = postService.convertToDetailVo(postPage);
//        posts.getContent().forEach(postDetailVO -> {
//            postDetailVO.setFormatContent(RegExUtils.replaceAll(postDetailVO.getFormatContent(), XML_INVALID_CHAR, ""));
//            postDetailVO.setSummary(RegExUtils.replaceAll(postDetailVO.getSummary(), XML_INVALID_CHAR, ""));
//        });
//        return posts.getContent();
//    }
}
