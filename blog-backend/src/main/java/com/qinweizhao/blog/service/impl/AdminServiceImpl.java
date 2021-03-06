package com.qinweizhao.blog.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.config.properties.HaloProperties;
import com.qinweizhao.blog.framework.event.logger.LogEvent;
import com.qinweizhao.blog.exception.BadRequestException;
import com.qinweizhao.blog.exception.NotFoundException;
import com.qinweizhao.blog.exception.ServiceException;
import com.qinweizhao.blog.mail.MailService;
import com.qinweizhao.blog.model.dto.EnvironmentDTO;
import com.qinweizhao.blog.model.entity.User;
import com.qinweizhao.blog.model.enums.LogType;
import com.qinweizhao.blog.model.params.LoginParam;
import com.qinweizhao.blog.model.params.ResetPasswordParam;
import com.qinweizhao.blog.model.properties.EmailProperties;
import com.qinweizhao.blog.model.support.HaloConst;
import com.qinweizhao.blog.security.authentication.Authentication;
import com.qinweizhao.blog.security.context.SecurityContextHolder;
import com.qinweizhao.blog.security.token.AuthToken;
import com.qinweizhao.blog.security.util.AuthUtils;
import com.qinweizhao.blog.service.*;
import com.qinweizhao.blog.util.FileUtils;
import com.qinweizhao.blog.util.HaloUtils;
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
        Assert.notNull(loginParam, "????????????????????????");

        String username = loginParam.getUsername();

        String mismatchTip = "??????????????????????????????";

        final User user;

        try {
            // ??????????????????????????????????????????
            user = Validator.isEmail(username) ?
                    userService.getByEmailOfNonNull(username) : userService.getByUsernameOfNonNull(username);
        } catch (NotFoundException e) {
            log.error("??????????????????: " + username);
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        userService.mustNotExpire(user);

        if (!userService.passwordMatch(user, loginParam.getPassword())) {
            // ????????????????????? ????????????
            eventPublisher.publishEvent(new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED, loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        return user;
    }

    @Override
    public AuthToken attemptAuthentication(@NonNull final LoginParam loginParam) {
        // ??????
        final User user = this.authenticate(loginParam);

        if (SecurityContextHolder.getContext().isAuthenticated()) {
            throw new BadRequestException("????????????????????????????????????");
        }

        // todo ????????????????????????
        // ????????????????????????
        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_IN, user.getNickname()));

        // ???????????? token
        return buildAuthToken(user);
    }

    @Override
    public void clearToken() {
        // ??????????????????????????????
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new BadRequestException("????????????????????????????????????");
        }

        // Get current user
        User user = authentication.getDetail().getUser();

        // Clear access token
        cacheStore.getAny(AuthUtils.buildAccessTokenKey(user), String.class).ifPresent(accessToken -> {
            // Delete token
            cacheStore.delete(AuthUtils.buildTokenAccessKey(accessToken));
            cacheStore.delete(AuthUtils.buildAccessTokenKey(user));
        });

        // Clear refresh token
        cacheStore.getAny(AuthUtils.buildRefreshTokenKey(user), String.class).ifPresent(refreshToken -> {
            cacheStore.delete(AuthUtils.buildTokenRefreshKey(refreshToken));
            cacheStore.delete(AuthUtils.buildRefreshTokenKey(user));
        });

        eventPublisher.publishEvent(new LogEvent(this, user.getUsername(), LogType.LOGGED_OUT, user.getNickname()));

        log.info("You have been logged out, looking forward to your next visit!");
    }

    @Override
    public void sendResetPasswordCode(ResetPasswordParam param) {
        cacheStore.getAny("code", String.class).ifPresent(code -> {
            throw new ServiceException("?????????????????????????????????????????????");
        });

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("?????????????????????????????????");
        }

        // Gets random code.
        String code = RandomUtil.randomNumbers(6);

        log.info("Got reset password code:{}", code);

        // Cache code.
        cacheStore.putAny("code", code, 5, TimeUnit.MINUTES);

        Boolean emailEnabled = optionService.getByPropertyOrDefault(EmailProperties.ENABLED, Boolean.class, false);

        if (!emailEnabled) {
            throw new ServiceException("????????? SMTP ??????????????????????????????????????????????????????????????????????????????");
        }

        // Send email to administrator.
        String content = "?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????\n" + code;
        mailService.sendTextMail(param.getEmail(), "?????????????????????", content);
    }

    @Override
    public void resetPasswordByCode(ResetPasswordParam param) {
        if (StringUtils.isEmpty(param.getCode())) {
            throw new ServiceException("?????????????????????");
        }

        if (StringUtils.isEmpty(param.getPassword())) {
            throw new ServiceException("??????????????????");
        }

        if (!userService.verifyUser(param.getUsername(), param.getEmail())) {
            throw new ServiceException("?????????????????????????????????");
        }

        // verify code
        String code = cacheStore.getAny("code", String.class).orElseThrow(() -> new ServiceException("?????????????????????"));
        if (!code.equals(param.getCode())) {
            throw new ServiceException("??????????????????");
        }

        User user = userService.getCurrentUser().orElseThrow(() -> new ServiceException("????????????????????????"));

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

        Integer userId = cacheStore.getAny(AuthUtils.buildTokenRefreshKey(refreshToken), Integer.class)
                .orElseThrow(() -> new BadRequestException("???????????????????????????????????????").setErrorData(refreshToken));

        // Get user info
        User user = userService.getById(userId);

        // Remove all token
        cacheStore.getAny(AuthUtils.buildAccessTokenKey(user), String.class)
                .ifPresent(accessToken -> cacheStore.delete(AuthUtils.buildTokenAccessKey(accessToken)));
        cacheStore.delete(AuthUtils.buildTokenRefreshKey(refreshToken));
        cacheStore.delete(AuthUtils.buildAccessTokenKey(user));
        cacheStore.delete(AuthUtils.buildRefreshTokenKey(user));

        return buildAuthToken(user);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void updateAdminAssets() {
        // Request github api
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(HaloConst.HALO_ADMIN_RELEASES_LATEST, Map.class);

        if (responseEntity.getStatusCode().isError() || responseEntity.getBody() == null) {
            log.debug("Failed to request remote url: [{}]", HALO_ADMIN_RELEASES_LATEST);
            throw new ServiceException("????????????????????? Github ??? API").setErrorData(HALO_ADMIN_RELEASES_LATEST);
        }

        Object assetsObject = responseEntity.getBody().get("assets");

        if (!(assetsObject instanceof List)) {
            throw new ServiceException("Github API ??????????????????").setErrorData(assetsObject);
        }

        try {
            List<?> assets = (List<?>) assetsObject;
            Map assetMap = (Map) assets.stream()
                    .filter(assetPredicate())
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("Halo admin ?????????????????????????????????????????????"));

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
                    .orElseThrow(() -> new BadRequestException("???????????????????????????????????????????????????????????? index.html ?????????"));

            // Copy it to template/admin folder
            FileUtils.copyFolder(adminRootPath, adminPath);
        } catch (Throwable t) {
            throw new ServiceException("?????? Halo admin ?????????" + t.getMessage(), t);
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
     * ????????????????????????
     *
     * @param user ????????????????????????
     * @return ??????
     */
    private AuthToken buildAuthToken(@NonNull User user) {
        Assert.notNull(user, "??????????????????");

        // ???????????????
        AuthToken token = new AuthToken();

        token.setAccessToken(HaloUtils.randomUUIDWithoutDash());
        token.setExpiredIn(ACCESS_TOKEN_EXPIRED_SECONDS);
        token.setRefreshToken(HaloUtils.randomUUIDWithoutDash());

        // ????????????????????????????????????
        cacheStore.putAny(AuthUtils.buildAccessTokenKey(user), token.getAccessToken(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(AuthUtils.buildRefreshTokenKey(user), token.getRefreshToken(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

        // ???????????? ID ??????????????????
        cacheStore.putAny(AuthUtils.buildTokenAccessKey(token.getAccessToken()), user.getId(), ACCESS_TOKEN_EXPIRED_SECONDS, TimeUnit.SECONDS);
        cacheStore.putAny(AuthUtils.buildTokenRefreshKey(token.getRefreshToken()), user.getId(), REFRESH_TOKEN_EXPIRED_DAYS, TimeUnit.DAYS);

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
            throw new ServiceException("??????????????????", e);
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
