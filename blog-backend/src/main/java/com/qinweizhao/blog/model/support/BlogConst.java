package com.qinweizhao.blog.model.support;

import org.springframework.http.HttpHeaders;

import java.io.File;
import java.util.Optional;

/**
 * <pre>
 *     公共常量
 * </pre>
 *
 * @author qinweizhao
 * @since 2022/09/13
 */
public class BlogConst {

    /**
     * 用户主目录
     */
    public static final String USER_HOME = System.getProperties().getProperty("user.home");
    /**
     * 临时目录
     */
    public static final String TEMP_DIR = "/tmp/com.qinweizhao.blog";
    public static final String PROTOCOL_HTTPS = "https://";
    public static final String PROTOCOL_HTTP = "http://";
    public static final String URL_SEPARATOR = "/";
    /**
     * 路径分隔符
     */
    public static final String FILE_SEPARATOR = File.separator;
    /**
     * Freemarker 模板文件的后缀
     */
    public static final String SUFFIX_FTL = ".ftl";
    /**
     * 自定义 freemarker 标签方法 key
     */
    public static final String METHOD_KEY = "method";
    /**
     * 网易云音乐短代码前缀
     */
    @Deprecated
    public static final String NETEASE_MUSIC_PREFIX = "[music:";
    /**
     * 网易云音乐 iframe 代码
     */
    @Deprecated
    public static final String NETEASE_MUSIC_IFRAME = "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=330 height=86 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=1&height=66\"></iframe>";
    /**
     * 网易云音乐短代码正则表达式
     */
    @Deprecated
    public static final String NETEASE_MUSIC_REG_PATTERN = "\\[music:(\\d+)\\]";
    /**
     * 哔哩哔哩视频短代码前缀
     */
    public static final String BILIBILI_VIDEO_PREFIX = "[bilibili:";
    /**
     * 哔哩哔哩视频 iframe 代码
     */
    public static final String BILIBILI_VIDEO_IFRAME = "<iframe height=$3 width=$2 src=\"//player.bilibili.com/player.html?aid=$1\" scrolling=\"no\" border=\"0\" frameborder=\"no\" framespacing=\"0\" allowfullscreen=\"true\"> </iframe>";
    /**
     * 哔哩哔哩视频正则表达式
     */
    public static final String BILIBILI_VIDEO_REG_PATTERN = "\\[bilibili:(\\d+)\\,(\\d+)\\,(\\d+)\\]";
    /**
     * YouTube 视频短代码前缀
     */
    public static final String YOUTUBE_VIDEO_PREFIX = "[youtube:";
    /**
     * YouTube 视频 iframe 代码
     */
    public static final String YOUTUBE_VIDEO_IFRAME = "<iframe width=$2 height=$3 src=\"https://www.youtube.com/embed/$1\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
    /**
     * YouTube 视频正则表达式
     */
    public static final String YOUTUBE_VIDEO_REG_PATTERN = "\\[youtube:(\\w+)\\,(\\d+)\\,(\\d+)\\]";

    /**
     * Admin token header name.
     */
    public static final String ADMIN_TOKEN_HEADER_NAME = "ADMIN-" + HttpHeaders.AUTHORIZATION;
    /**
     * Admin token param name.
     */
    public static final String ADMIN_TOKEN_QUERY_NAME = "admin_token";
    /**
     * Content api token param name
     */
    public static final String ONE_TIME_TOKEN_QUERY_NAME = "ott";
    public static final String ONE_TIME_TOKEN_HEADER_NAME = "ott";
    /**
     * Version constant. (Available in production environment)
     */
    public static final String HALO_VERSION;
    /**
     * Unknown version: unknown
     */
    public static final String UNKNOWN_VERSION = "unknown";
    /**
     * Database product name.
     */
    public static String DATABASE_PRODUCT_NAME = null;

    static {
        // Set version
        HALO_VERSION = Optional.ofNullable(BlogConst.class.getPackage().getImplementationVersion()).orElse(UNKNOWN_VERSION);
    }

    private BlogConst() {
    }
}
