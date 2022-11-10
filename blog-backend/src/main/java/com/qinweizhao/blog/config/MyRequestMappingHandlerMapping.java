package com.qinweizhao.blog.config;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

import static com.qinweizhao.blog.util.HaloUtils.URL_SEPARATOR;
import static com.qinweizhao.blog.util.HaloUtils.ensureBoth;

/**
 * @author qinweizhao
 * @since 2020-03-24
 */
@Slf4j
public class MyRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final Set<String> blackPatterns = new HashSet<>(16);

    private final PathMatcher pathMatcher;

    private final MyBlogProperties myBlogProperties;

    public MyRequestMappingHandlerMapping(MyBlogProperties myBlogProperties) {
        this.myBlogProperties = myBlogProperties;
        this.initBlackPatterns();
        pathMatcher = new AntPathMatcher();
    }

    @Override
    protected HandlerMethod lookupHandlerMethod(@NotNull String lookupPath, @NotNull HttpServletRequest request) throws Exception {
        log.debug("寻找路径: [{}]", lookupPath);
        for (String blackPattern : blackPatterns) {
            if (this.pathMatcher.match(blackPattern, lookupPath)) {
                log.debug("跳过的路径 [{}]匹配的地址为: [{}]", lookupPath, blackPattern);
                return null;
            }
        }
        return super.lookupHandlerMethod(lookupPath, request);
    }

    private void initBlackPatterns() {
        String uploadUrlPattern = ensureBoth(myBlogProperties.getUploadUrlPrefix(), URL_SEPARATOR) + "**";
        String adminPathPattern = ensureBoth(myBlogProperties.getAdminPath(), URL_SEPARATOR) + "?*/**";

        blackPatterns.add("/themes/**");
        blackPatterns.add("/js/**");
        blackPatterns.add("/images/**");
        blackPatterns.add("/fonts/**");
        blackPatterns.add("/css/**");
        blackPatterns.add("/assets/**");
        blackPatterns.add("/color.less");
        blackPatterns.add("/csrf");
        blackPatterns.add("/webjars/**");
        blackPatterns.add(uploadUrlPattern);
        blackPatterns.add(adminPathPattern);
    }

}
