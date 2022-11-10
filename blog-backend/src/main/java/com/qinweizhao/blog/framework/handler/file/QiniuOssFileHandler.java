package com.qinweizhao.blog.framework.handler.file;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qinweizhao.blog.exception.FileOperationException;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.properties.QiniuOssProperties;
import com.qinweizhao.blog.model.support.UploadResult;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.util.FilenameUtils;
import com.qinweizhao.blog.util.ImageUtils;
import com.qinweizhao.blog.util.JsonUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static com.qinweizhao.blog.framework.handler.file.FileHandler.isImageType;
import static com.qinweizhao.blog.model.support.BlogConst.*;
import static com.qinweizhao.blog.util.HaloUtils.ensureSuffix;

/**
 * Qiniu oss file handler.
 *
 * @since 2019-03-27
 */
@Slf4j
@Component
public class QiniuOssFileHandler implements FileHandler {

    private final ConfigService configService;

    public QiniuOssFileHandler(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public UploadResult upload(MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");

        Region region = configService.getQiniuRegion();

        String accessKey = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_ACCESS_KEY).toString();
        String secretKey = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_SECRET_KEY).toString();
        String bucket = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_BUCKET).toString();
        String protocol = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_PROTOCOL).toString();
        String domain = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_DOMAIN).toString();
        String source = configService.getByPropertyOrDefault(QiniuOssProperties.OSS_SOURCE, String.class, "");
        String styleRule = configService.getByPropertyOrDefault(QiniuOssProperties.OSS_STYLE_RULE, String.class, "");
        String thumbnailStyleRule = configService.getByPropertyOrDefault(QiniuOssProperties.OSS_THUMBNAIL_STYLE_RULE, String.class, "");

        // Create configuration
        Configuration configuration = new Configuration(region);

        // Create auth
        Auth auth = Auth.create(accessKey, secretKey);
        // Build put plicy
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"size\":$(fsize),\"width\":$(imageInfo.width),\"height\":$(imageInfo.height)}");
        // Get upload token
        String uploadToken = auth.uploadToken(bucket, null, 60 * 60, putPolicy);

        // Create temp path
        Path tmpPath = Paths.get(ensureSuffix(TEMP_DIR, FILE_SEPARATOR), bucket);

        StringBuilder basePath = new StringBuilder(protocol)
                .append(domain)
                .append(URL_SEPARATOR);

        try {
            String basename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String timestamp = String.valueOf(System.currentTimeMillis());
            StringBuilder upFilePath = new StringBuilder();
            if (StringUtils.isNotEmpty(source)) {
                upFilePath.append(source)
                        .append(URL_SEPARATOR);
            }
            upFilePath.append(basename)
                    .append("_")
                    .append(timestamp)
                    .append(".")
                    .append(extension);

            // Get file recorder for temp directory
            FileRecorder fileRecorder = new FileRecorder(tmpPath.toFile());
            // Get upload manager
            UploadManager uploadManager = new UploadManager(configuration, fileRecorder);
            // Put the file
            Response response = uploadManager.put(file.getInputStream(), upFilePath.toString(), uploadToken, null, null);

            if (log.isDebugEnabled()) {
                log.debug("Qiniu oss response: [{}]", response.toString());
                log.debug("Qiniu oss response body: [{}]", response.bodyString());
            }

            // Convert response
            PutSet putSet = JsonUtils.jsonToObject(response.bodyString(), PutSet.class);

            // Get file full path
            String filePath = StringUtils.join(basePath.toString(), upFilePath.toString());

            // Build upload result
            UploadResult result = new UploadResult();
            result.setFilename(basename);
            result.setFilePath(StringUtils.isBlank(styleRule) ? filePath : filePath + styleRule);
            result.setKey(upFilePath.toString());
            result.setSuffix(extension);
            result.setWidth(putSet.getWidth());
            result.setHeight(putSet.getHeight());
            result.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            result.setSize(file.getSize());

            if (isImageType(result.getMediaType())) {
                if (ImageUtils.EXTENSION_ICO.equals(extension)) {
                    result.setThumbPath(filePath);
                } else {
                    result.setThumbPath(StringUtils.isBlank(thumbnailStyleRule) ? filePath : filePath + thumbnailStyleRule);
                }
            }

            return result;
        } catch (IOException e) {
            if (e instanceof QiniuException) {
                log.error("Qiniu oss error response: [{}]", ((QiniuException) e).response);
            }

            throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到七牛云失败", e);
        }
    }

    @Override
    public void delete(String key) {
        Assert.notNull(key, "File key must not be blank");

        Region region = configService.getQiniuRegion();

        String accessKey = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_ACCESS_KEY).toString();
        String secretKey = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_SECRET_KEY).toString();
        String bucket = configService.getByPropertyOfNonNull(QiniuOssProperties.OSS_BUCKET).toString();

        // Create configuration
        Configuration configuration = new Configuration(region);

        // Create auth
        Auth auth = Auth.create(accessKey, secretKey);

        BucketManager bucketManager = new BucketManager(auth, configuration);

        try {
            Response response = bucketManager.delete(bucket, key);
            if (!response.isOK()) {
                log.warn("附件 " + key + " 从七牛云删除失败");
            }
        } catch (QiniuException e) {
            log.error("Qiniu oss error response: [{}]", e.response);
            throw new FileOperationException("附件 " + key + " 从七牛云删除失败", e);
        }
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.QINIUOSS;
    }

    @Data
    @NoArgsConstructor
    private static class PutSet {

        public String hash;

        public String key;

        private Long size;

        private Integer width;

        private Integer height;
    }
}

