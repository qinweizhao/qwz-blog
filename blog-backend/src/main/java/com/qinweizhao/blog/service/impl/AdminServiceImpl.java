package com.qinweizhao.blog.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.config.properties.HaloProperties;
import com.qinweizhao.blog.event.logger.LogEvent;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.mail.MailService;
import com.qinweizhao.blog.model.base.BaseEntity;
import com.qinweizhao.blog.model.dto.EnvironmentDTO;
import com.qinweizhao.blog.model.dto.StatisticDTO;
import com.qinweizhao.blog.model.entity.Comment;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.CommentStatus;
import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.params.LoginParam;
import com.qinweizhao.blog.model.params.ResetPasswordParam;
import com.qinweizhao.blog.model.properties.EmailProperties;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.security.authentication.Authentication;
import com.qinweizhao.blog.security.context.SecurityContextHolder;
import com.qinweizhao.blog.security.token.AuthToken;
import com.qinweizhao.blog.security.util.SecurityUtils;
import com.qinweizhao.blog.service.*;
import com.qinweizhao.blog.utils.FileUtils;
import com.qinweizhao.blog.utils.HaloUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static com.qinweizhao.blog.model.support.HaloConst.*;

/**
 * Admin service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-04-29
 */
@Slf4j
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

//    private final PostService postService;
//
//    private final SheetService sheetService;
//
//    private final AttachmentService attachmentService;
//
//    private final CommentService<BaseMapper<Comment>, BaseEntity> commentService;
//
//    private final SheetCommentService sheetCommentService;
//
//    private final JournalCommentService journalCommentService;

    private final OptionService optionService;

    private final UserService userService;

//    private final LinkService linkService;

    private final MailService mailService;

    private final AbstractStringCacheStore cacheStore;

    private final RestTemplate restTemplate;

    private final HaloProperties haloProperties;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @NonNull
    public User authenticate(@NonNull LoginParam loginParam) {
        Assert.notNull(loginParam, "登录参数不能为空");

        String username = loginParam.getUsername();

        String mismatchTip = "用户名或者密码不正确";

        final User user;

        try {
            // 通过用户名或电子邮件获取用户
            user = Validator.isEmail(username) ?
                    userService.getByEmailOfNonNull(username) : userService.getByUsernameOfNonNull(username);
        } catch (NotFoundException e) {
            log.error("查找用户失败: " + username);
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        userService.mustNotExpire(user);

        if (!userService.passwordMatch(user, loginParam.getPassword())) {
            // 如果密码不匹配 记录日志
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        return user;
    }

    @Override
    public AuthToken attemptAuthentication(@NonNull final LoginParam loginParam) {
        // 认证
        final User user = this.authenticate(loginParam);

        if (SecurityContextHolder.getContext().isAuthenticated()) {
            throw new BadRequestException("您已登录，请不要重复登录");
        }

        // todo 使用注解记录日志
        // 日志记录登录成功
        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_IN, user.getNickname()));

        // 生成新的 token
        return buildAuthToken(user);
    }

    @Override
    public void clearToken() {
        // 检查当前是否正在登录
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new BadRequestException("您尚未登录，因此无法注销");
        }

        // Get current user
        User user = authentication.getDetail().getUser();

        // Clear access token
        cacheStore.getAny(SecurityUtils.buildAccessTokenKey(user), String.class).ifPresent(accessToken -> {
            // Delete token
            cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken));
            cacheStore.delete(SecurityUtils.buildAccessTokenKey(user));
        });

        // Clear refresh token
        cacheStore.getAny(SecurityUtils.buildRefreshTokenKey(user), String.class).ifPresent(refreshToken -> {
            cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
            cacheStore.delete(SecurityUtils.buildRefreshTokenKey(user));
        });

        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_OUT, user.getNickname()));

        log.info("You have been logged out, looking forward to your next visit!");
    }

    @Override
    public void sendResetPasswordCode(ResetPasswordParam param) {
        cacheStore.getAny("code", String.class).ifPresent(code -> {
            throw new ServiceException("已经获取过验证码，不能重复获取");
        });

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("用户名或者邮箱验证错误");
        }

        // Gets random code.
        String code = RandomUtil.randomNumbers(6);

        log.info("Got reset password code:{}", code);

        // Cache code.
        cacheStore.putAny("code", code, 5, TimeUnit.MINUTES);

        Boolean emailEnabled = optionService.getByPropertyOrDefault(EmailProperties.ENABLED, Boolean.class, false);

        if (!emailEnabled) {
            throw new ServiceException("未启用 SMTP 服务，无法发送邮件，但是你可以通过系统日志找到验证码");
        }

        // Send email to administrator.
        String content = "您正在进行密码重置操作，如不是本人操作，请尽快做好相应措施。密码重置验证码如下（五分钟有效）：\n" + code;
        mailService.sendTextMail(param.getEmail(), "找回密码验证码", content);
    }

    @Override
    public void resetPasswordByCode(ResetPasswordParam param) {
        if (StringUtils.isEmpty(param.getCode())) {
            throw new ServiceException("验证码不能为空");
        }

        if (StringUtils.isEmpty(param.getPassword())) {
            throw new ServiceException("密码不能为空");
        }

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("用户名或者邮箱验证错误");
        }

        // verify code
        String code = cacheStore.getAny("code", String.class).orElseThrow(() -> new ServiceException("未获取过验证码"));
        if (!code.equals(param.getCode())) {
            throw new ServiceException("验证码不正确");
        }

        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("未查询到博主信息"));

        // reset password
        userService.setPassword(user, param.getPassword());

        // Update this user
        userService.updateById(user);

        // clear code cache
        cacheStore.delete("code");
    }
//
//    @Override
//    @NonNull
//    public StatisticDTO getCount() {
//        StatisticDTO statisticDTO = new StatisticDTO();
//        statisticDTO.setPostCount(postService.countByStatus(PostStatus.PUBLISHED) + sheetService.countByStatus(PostStatus.PUBLISHED));
//        statisticDTO.setAttachmentCount(attachmentService.count());
//
//        // Handle comment count
//        long postCommentCount = commentService.countByStatus(CommentStatus.PUBLISHED);
//        long sheetCommentCount = sheetCommentService.countByStatus(CommentStatus.PUBLISHED);
//        long journalCommentCount = journalCommentService.countByStatus(CommentStatus.PUBLISHED);
//
//        statisticDTO.setCommentCount(postCommentCount + sheetCommentCount + journalCommentCount);
//
//        long birthday = optionService.getBirthday();
//        long days = (System.currentTimeMillis() - birthday) / (1000 * 24 * 3600);
//        statisticDTO.setEstablishDays(days);
//        statisticDTO.setBirthday(birthday);
//
//        statisticDTO.setLinkCount(linkService.count());
//
//        statisticDTO.setVisitCount(postService.countVisit() + sheetService.countVisit());
//        statisticDTO.setLikeCount(postService.countLike() + sheetService.countLike());
//        return statisticDTO;
//    }

    @Override
    @NonNull
    public EnvironmentDTO getEnvironments() {
        EnvironmentDTO environmentDTO = new EnvironmentDTO();

        // Get application start time.
        environmentDTO.setStartTime(ManagementFactory.getRuntimeMXBean().getStartTime());

        environmentDTO.setDatabase(DATABASE_PRODUCT_NAME);

        environmentDTO.setVersion(HaloConst.HALO_VERSION);

        environmentDTO.setMode(haloProperties.getMode());

        return environmentDTO;
    }

    @Override
    @NonNull
    public AuthToken refreshToken(@NonNull String refreshToken) {
        Assert.hasText(refreshToken, "Refresh token must not be blank");

        Integer userId = cacheStore.getAny(SecurityUtils.buildTokenRefreshKey(refreshToken), Integer.class)
                .orElseThrow(() -> new BadRequestException("登录状态已失效，请重新登录").setErrorData(refreshToken));

        // Get user info
        User user = userService.getById(userId);

        // Remove all token
        cacheStore.getAny(SecurityUtils.buildAccessTokenKey(user), String.class)
                .ifPresent(accessToken -> cacheStore.delete(SecurityUtils.buildTokenAccessKey(accessToken)));
        cacheStore.delete(SecurityUtils.buildTokenRefreshKey(refreshToken));
        cacheStore.delete(SecurityUtils.buildAccessTokenKey(user));
        cacheStore.delete(SecurityUtils.buildRefreshTokenKey(user));

        return buildAuthToken(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateAdminAssets() {
        // Request github api
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(HaloConst.HALO_ADMIN_RELEASES_LATEST, Map.class);

        if (responseEntity.getStatusCode().isError() || responseEntity.getBody() == null) {
            log.debug("Failed to request remote url: [{}]", HALO_ADMIN_RELEASES_LATEST);
            throw new ServiceException("系统无法访问到 Github 的 API").setErrorData(HALO_ADMIN_RELEASES_LATEST);
        }

        Object assetsObject = responseEntity.getBody().get("assets");

        if (!(assetsObject instanceof List)) {
            throw new ServiceException("Github API 返回内容有误").setErrorData(assetsObject);
        }

        try {
            List<?> assets = (List<?>) assetsObject;
            Map assetMap = (Map) assets.stream()
                    .filter(assetPredicate())
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("Halo admin 最新版暂无资源文件，请稍后再试"));

            Object browserDownloadUrl = assetMap.getOrDefault("browser_download_url", "");
            // Download the assets
            ResponseEntity<byte[]> downloadResponseEntity = restTemplate.getForEntity(browserDownloadUrl.toString(), byte[].class);

            if (downloadResponseEntity.getStatusCode().isError() || downloadResponseEntity.getBody() == null) {
                throw new ServiceException("Failed to request remote url: " + browserDownloadUrl.toString()).setErrorData(browserDownloadUrl.toString());
            }

            String adminTargetName = haloProperties.getWorkDir() + HALO_ADMIN_RELATIVE_PATH;

            Path adminPath = Paths.get(adminTargetName);
            Path adminBackupPath = Paths.get(haloProperties.getWorkDir(), HALO_ADMIN_RELATIVE_BACKUP_PATH);

            backupAndClearAdminAssetsIfPresent(adminPath, adminBackupPath);

            // Create temp folder
            Path assetTempPath = FileUtils.createTempDirectory()
                    .resolve(assetMap.getOrDefault("name", "halo-admin-latest.zip").toString());

            // Unzip
            FileUtils.unzip(downloadResponseEntity.getBody(), assetTempPath);

            // find root folder
            Path adminRootPath = FileUtils.findRootPath(assetTempPath,
                            path -> StringUtils.equalsIgnoreCase("index.html", path.getFileName().toString()))
                    .orElseThrow(() -> new BadRequestException("无法准确定位到压缩包的根路径，请确认包含 index.html 文件。"));

            // Copy it to template/admin folder
            FileUtils.copyFolder(adminRootPath, adminPath);
        } catch (Throwable t) {
            throw new ServiceException("更新 Halo admin 失败，" + t.getMessage(), t);
        }
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private Predicate<Object> assetPredicate() {
        return asset -> {
            if (!(asset instanceof Map)) {
                return false;
            }
            Map aAssetMap = (Map) asset;
            // Get content-type
            String contentType = aAssetMap.getOrDefault("content_type", "").toString();

            Object name = aAssetMap.getOrDefault("name", "");
            return name.toString().matches(HALO_ADMIN_VERSION_REGEX) && "application/zip".equalsIgnoreCase(contentType);
        };
    }

    private void backupAndClearAdminAssetsIfPresent(@NonNull Path sourcePath, @NonNull Path backupPath) throws IOException {
        Assert.notNull(sourcePath, "Source path must not be null");
        Assert.notNull(backupPath, "Backup path must not be null");

        if (!FileUtils.isEmpty(sourcePath)) {
            // Clone this assets
            Path adminPathBackup = Paths.get(haloProperties.getWorkDir(), HALO_ADMIN_RELATIVE_BACKUP_PATH);

            // Delete backup
            FileUtils.deleteFolder(backupPath);

            // Copy older assets into backup
            FileUtils.copyFolder(sourcePath, backupPath);

            // Delete older assets
            FileUtils.deleteFolder(sourcePath);
        } else {
            FileUtils.createIfAbsent(sourcePath);
        }
    }

    /**
     * 构建身份验证令牌
     *
     * @param user 用户信息不能为空
     * @return 令牌
     */
    private AuthToken buildAuthToken(@NonNull User user) {
        Assert.notNull(user, "用户不能为空");

        // 生成新令牌
        AuthToken token = new AuthToken();

        token.setAccessToken(HaloUtils.randomUUIDWithoutDash());
        token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
        token.setRefreshToken(HaloUtils.randomUUIDWithoutDash());

        // 缓存这些令牌，仅用于清除
        cacheStore.putAny(SecurityUtils.buildAccessTokenKey(user), token.getAccessToken(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(SecurityUtils.buildRefreshTokenKey(user), token.getRefreshToken(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        // 使用用户 ID 缓存这些令牌
        cacheStore.putAny(SecurityUtils.buildTokenAccessKey(token.getAccessToken()), user.getId(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(SecurityUtils.buildTokenRefreshKey(token.getRefreshToken()), user.getId(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        return token;
    }

    @Override
    public String getLogFiles(@NonNull Long lines) {
        Assert.notNull(lines, "Lines must not be null");

        File file = new File(haloProperties.getWorkDir(), LOG_PATH);

        List<String> linesArray = new ArrayList<>();

        StringBuilder result = new StringBuilder();

        if (!file.exists()) {
            return StringUtils.EMPTY;
        }
        long count = 0;

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long length = randomAccessFile.length();
            if (length == 0L) {
                return StringUtils.EMPTY;
            } else {
                long pos = length - 1;
                while (pos > 0) {
                    pos--;
                    randomAccessFile.seek(pos);
                    if (randomAccessFile.readByte() == '\n') {
                        String line = randomAccessFile.readLine();
                        linesArray.add(new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                        count++;
                        if (count == lines) {
                            break;
                        }
                    }
                }
                if (pos == 0) {
                    randomAccessFile.seek(0);
                    linesArray.add(new String(randomAccessFile.readLine().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
                }
            }
        } catch (Exception e) {
            throw new ServiceException("读取日志失败", e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Collections.reverse(linesArray);

        linesArray.forEach(line -> {
            result.append(line)
                    .append(StringUtils.LF);
        });

        return result.toString();
    }


}
