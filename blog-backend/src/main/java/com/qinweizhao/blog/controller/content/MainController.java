package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.service.SettingService;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.util.HaloUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main controller.
 *
 * @author qinweizhao
 * @since 2019-04-23
 */
@Controller
public class MainController {

    /**
     * Index redirect uri.
     */
    private static final String INDEX_REDIRECT_URI = "index.html";

    @Resource
    private UserService userService;

    @Resource
    private SettingService settingService;

    @Resource
    private MyBlogProperties myBlogProperties;


    @GetMapping("${blog.admin-path:admin}")
    public String admin() {
        return HaloUtils.ensureBoth(myBlogProperties.getAdminPath(), HaloUtils.URL_SEPARATOR) + INDEX_REDIRECT_URI;
    }

    @GetMapping("avatar")
    public void avatar(HttpServletResponse response) throws IOException {
        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));
        if (StringUtils.isNotEmpty(user.getAvatar())) {
            response.sendRedirect(HaloUtils.normalizeUrl(user.getAvatar()));
        }
    }

    @GetMapping("logo")
    public void logo(HttpServletResponse response) throws IOException {
        Object blogLogo = settingService.get("blog_logo");
        String logo = blogLogo == null ? null : String.valueOf(blogLogo);
        if (StringUtils.isNotEmpty(logo)) {
            response.sendRedirect(HaloUtils.normalizeUrl(logo));
        }
    }

    @GetMapping("favicon.ico")
    public void favicon(HttpServletResponse response) throws IOException {
        Object blogFavicon = settingService.get("blog_favicon");
        String favicon = blogFavicon == null ? null : String.valueOf(blogFavicon);

        if (StringUtils.isNotEmpty(favicon)) {
            response.sendRedirect(HaloUtils.normalizeUrl(favicon));
        }
    }
}
