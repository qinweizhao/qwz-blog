package com.qinweizhao.blog.framework.handler.file;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import com.qinweizhao.blog.exception.FileOperationException;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.properties.HuaweiObsProperties;
import com.qinweizhao.blog.model.support.UploadResult;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.util.FilenameUtils;
import com.qinweizhao.blog.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageReader;
import java.io.IOException;
import java.util.Objects;

import static com.qinweizhao.blog.model.support.BlogConst.URL_SEPARATOR;

/**
 * Huawei obs file handler.
 *
 * @author qilin
 * @since 2020-04-03
 */
@Slf4j
@Component
public class HuaweiObsFileHandler implements FileHandler {

    private final ConfigService configService;

    public HuaweiObsFileHandler(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public @NotNull UploadResult upload(@NotNull MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");

        // Get config
        String protocol = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_PROTOCOL).toString();
        String domain = configService.getByPropertyOrDefault(HuaweiObsProperties.OSS_DOMAIN, String.class, "");
        String source = configService.getByPropertyOrDefault(HuaweiObsProperties.OSS_SOURCE, String.class, "");
        String endPoint = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_ENDPOINT).toString();
        String accessKey = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_ACCESS_KEY).toString();
        String accessSecret = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_ACCESS_SECRET).toString();
        String bucketName = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_BUCKET_NAME).toString();
        String styleRule = configService.getByPropertyOrDefault(HuaweiObsProperties.OSS_STYLE_RULE, String.class, "");
        String thumbnailStyleRule = configService.getByPropertyOrDefault(HuaweiObsProperties.OSS_THUMBNAIL_STYLE_RULE, String.class, "");

        // Init OSS client
        final ObsClient obsClient = new ObsClient(accessKey, accessSecret, endPoint);

        StringBuilder basePath = new StringBuilder(protocol);

        if (StringUtils.isNotEmpty(domain)) {
            basePath.append(domain)
                    .append(URL_SEPARATOR);
        } else {
            basePath.append(bucketName)
                    .append(".")
                    .append(endPoint)
                    .append(URL_SEPARATOR);
        }

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

            String filePath = StringUtils.join(basePath.toString(), upFilePath.toString());

            log.info(basePath.toString());

            // Upload
            PutObjectResult putObjectResult = obsClient.putObject(bucketName, upFilePath.toString(), file.getInputStream());
            if (putObjectResult == null) {
                throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到华为云失败 ");
            }

            // Response result
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult.setFilePath(StringUtils.isBlank(styleRule) ? filePath : filePath + styleRule);
            uploadResult.setKey(upFilePath.toString());
            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(file.getSize());

            // Handle thumbnail
            if (FileHandler.isImageType(uploadResult.getMediaType())) {
                ImageReader image = ImageUtils.getImageReaderFromFile(file.getInputStream(), extension);
                assert image != null;
                uploadResult.setWidth(image.getWidth(0));
                uploadResult.setHeight(image.getHeight(0));
                if (ImageUtils.EXTENSION_ICO.equals(extension)) {
                    uploadResult.setThumbPath(filePath);
                } else {
                    uploadResult.setThumbPath(StringUtils.isBlank(thumbnailStyleRule) ? filePath : filePath + thumbnailStyleRule);
                }
            }

            log.info("Uploaded file: [{}] successfully", file.getOriginalFilename());
            return uploadResult;
        } catch (Exception e) {
            throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到华为云失败 ", e).setErrorData(file.getOriginalFilename());
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void delete(@NotNull String key) {
        Assert.notNull(key, "File key must not be blank");

        // Get config
        String endPoint = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_ENDPOINT).toString();
        String accessKey = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_ACCESS_KEY).toString();
        String accessSecret = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_ACCESS_SECRET).toString();
        String bucketName = configService.getByPropertyOfNonNull(HuaweiObsProperties.OSS_BUCKET_NAME).toString();

        // Init OSS client
        final ObsClient obsClient = new ObsClient(accessKey, accessSecret, endPoint);

        try {
            obsClient.deleteObject(bucketName, key);
        } catch (Exception e) {
            throw new FileOperationException("附件 " + key + " 从华为云删除失败", e);
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.HUAWEIOBS;
    }

}
