package com.qinweizhao.blog.controller.content;

import com.qinweizhao.blog.config.properties.HaloProperties;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.properties.BlogProperties;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.UserService;
import com.qinweizhao.blog.utils.HaloUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main controller.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-04-23
 */
@Controller
public class MainController {

    /**
     * Index redirect uri.
     */
    private final static String INDEX_REDIRECT_URI = "index.html";

    /**
     * Install redirect uri.
     */
    private final static String INSTALL_REDIRECT_URI = INDEX_REDIRECT_URI + "#install";


    @Resource
    private UserService userService;
    @Resource
    private OptionService optionService;
    @Resource
    private HaloProperties haloProperties;


    @GetMapping("${halo.admin-path:admin}")
    public void admin(HttpServletResponse response) throws IOException {
        String adminIndexRedirectUri = HaloUtils.ensureBoth(haloProperties.getAdminPath(), HaloUtils.URL_SEPARATOR) + INDEX_REDIRECT_URI;
        response.sendRedirect(adminIndexRedirectUri);
    }

    @GetMapping("version")
    @ResponseBody
    public String version() {
        return HaloConst.HALO_VERSION;
    }

    @GetMapping("install")
    public void installation(HttpServletResponse response) throws IOException {
        String installRedirectUri = StringUtils.appendIfMissing(this.haloProperties.getAdminPath(), "/") + INSTALL_REDIRECT_URI;
        response.sendRedirect(installRedirectUri);
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
        String blogLogo = optionService.getByProperty(BlogProperties.BLOG_LOGO).orElse("").toString();
        if (StringUtils.isNotEmpty(blogLogo)) {
            response.sendRedirect(HaloUtils.normalizeUrl(blogLogo));
        }
    }

    @GetMapping("favicon.ico")
    public void favicon(HttpServletResponse response) throws IOException {
        String favicon = optionService.getByProperty(BlogProperties.BLOG_FAVICON).orElse("").toString();
        if (StringUtils.isNotEmpty(favicon)) {
            response.sendRedirect(HaloUtils.normalizeUrl(favicon));
        }
    }
}
