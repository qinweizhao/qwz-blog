package com.qinweizhao.blog.util;

import cn.hutool.core.util.URLUtil;
import com.qinweizhao.blog.model.support.BlogConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.qinweizhao.blog.model.support.BlogConst.FILE_SEPARATOR;

/**
 * Common utils
 *
 * @since 2017-12-22
 */
@Slf4j
public class HaloUtils {

    public static final String URL_SEPARATOR = "/";
    private static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";


    public static String ensureBoth(String string, String bothfix) {
        return ensureBoth(string, bothfix, bothfix);
    }


    public static String ensureBoth(String string, String prefix, String suffix) {
        return ensureSuffix(ensurePrefix(string, prefix), suffix);
    }

    /**
     * Ensures the string contain prefix.
     *
     * @param string string must not be blank
     * @param prefix prefix must not be blank
     * @return string contain prefix specified
     */

    public static String ensurePrefix(String string, String prefix) {
        Assert.hasText(string, "String must not be blank");
        Assert.hasText(prefix, "Prefix must not be blank");

        return prefix + StringUtils.removeStart(string, prefix);
    }


    /**
     * Ensures the string contain suffix.
     *
     * @param string string must not be blank
     * @param suffix suffix must not be blank
     * @return string contain suffix specified
     */

    public static String ensureSuffix(String string, String suffix) {
        Assert.hasText(string, "String must not be blank");
        Assert.hasText(suffix, "Suffix must not be blank");

        return StringUtils.removeEnd(string, suffix) + suffix;
    }


    /**
     * Desensitizes the plain text.
     *
     * @param plainText plain text must not be null
     * @param leftSize  left size
     * @param rightSize right size
     * @return desensitization
     */
    public static String desensitize(String plainText, int leftSize, int rightSize) {
        Assert.hasText(plainText, "Plain text must not be blank");

        if (leftSize < 0) {
            leftSize = 0;
        }

        if (leftSize > plainText.length()) {
            leftSize = plainText.length();
        }

        if (rightSize < 0) {
            rightSize = 0;
        }

        if (rightSize > plainText.length()) {
            rightSize = plainText.length();
        }

        if (plainText.length() < leftSize + rightSize) {
            rightSize = plainText.length() - leftSize;
        }

        int remainSize = plainText.length() - rightSize - leftSize;

        String left = StringUtils.left(plainText, leftSize);
        String right = StringUtils.right(plainText, rightSize);
        return StringUtils.rightPad(left, remainSize + leftSize, '*') + right;
    }

    /**
     * 将文件分隔符更改为 url 分隔符
     *
     * @param pathname full path name must not be blank.
     * @return text with url separator
     */
    public static String changeFileSeparatorToUrlSeparator(String pathname) {
        Assert.hasText(pathname, "Path name must not be blank");

        return pathname.replace(FILE_SEPARATOR, "/");
    }

    /**
     * Time format.
     *
     * @param totalSeconds seconds
     * @return formatted time
     */

    public static String timeFormat(long totalSeconds) {
        if (totalSeconds <= 0) {
            return "0 second";
        }

        StringBuilder timeBuilder = new StringBuilder();

        long hours = totalSeconds / 3600;
        long minutes = totalSeconds % 3600 / 60;
        long seconds = totalSeconds % 3600 % 60;

        if (hours > 0) {
            if (StringUtils.isNotBlank(timeBuilder)) {
                timeBuilder.append(", ");
            }
            timeBuilder.append(pluralize(hours, "hour", "hours"));
        }

        if (minutes > 0) {
            if (StringUtils.isNotBlank(timeBuilder)) {
                timeBuilder.append(", ");
            }
            timeBuilder.append(pluralize(minutes, "minute", "minutes"));
        }

        if (seconds > 0) {
            if (StringUtils.isNotBlank(timeBuilder)) {
                timeBuilder.append(", ");
            }
            timeBuilder.append(pluralize(seconds, "second", "seconds"));
        }

        return timeBuilder.toString();
    }

    /**
     * Pluralize the times label format.
     *
     * @param times       times
     * @param label       label
     * @param pluralLabel plural label
     * @return pluralized format
     */

    public static String pluralize(long times, String label, String pluralLabel) {
        Assert.hasText(label, "Label must not be blank");
        Assert.hasText(pluralLabel, "Plural label must not be blank");

        if (times <= 0) {
            return "no " + pluralLabel;
        }

        if (times == 1) {
            return times + " " + label;
        }

        return times + " " + pluralLabel;
    }

    /**
     * Gets random uuid without dash.
     *
     * @return random uuid without dash
     */

    public static String randomUUIDWithoutDash() {
        return StringUtils.remove(UUID.randomUUID().toString(), '-');
    }

    /**
     * Normalize url
     *
     * @param originalUrl original url
     * @return normalized url.
     */

    public static String normalizeUrl(String originalUrl) {
        Assert.hasText(originalUrl, "Original Url must not be blank");

        if (StringUtils.startsWithAny(originalUrl, URL_SEPARATOR, BlogConst.PROTOCOL_HTTPS, BlogConst.PROTOCOL_HTTP) && !StringUtils.startsWith(originalUrl, "//")) {
            return originalUrl;
        }

        return URLUtil.normalize(originalUrl);
    }


    /**
     * Clean all html tag
     *
     * @param content html document
     * @return text before cleaned
     */
    public static String cleanHtmlTag(String content) {
        if (StringUtils.isEmpty(content)) {
            return StringUtils.EMPTY;
        }
        return content.replaceAll(RE_HTML_MARK, StringUtils.EMPTY);
    }

}
