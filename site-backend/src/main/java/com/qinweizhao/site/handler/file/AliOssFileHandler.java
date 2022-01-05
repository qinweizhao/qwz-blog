package com.qinweizhao.site.handler.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.qinweizhao.site.exception.FileOperationException;
import com.qinweizhao.site.model.enums.AttachmentType;
import com.qinweizhao.site.model.properties.AliOssProperties;
import com.qinweizhao.site.model.support.UploadResult;
import com.qinweizhao.site.service.OptionService;
import com.qinweizhao.site.utils.FilenameUtils;
import com.qinweizhao.site.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.qinweizhao.site.model.support.HaloConst.URL_SEPARATOR;

/**
 * Ali oss file handler.
 *
 * @author MyFaith
 * @author ryanwang
 * @date 2019-04-04
 */
@Slf4j
@Component
public class AliOssFileHandler implements FileHandler {

    private final OptionService optionService;

    public AliOssFileHandler(OptionService optionService) {
        this.optionService = optionService;
    }

    @Override
    public @NonNull
    UploadResult upload(@NonNull MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");

        // Get config
        String protocol =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_PROTOCOL).toString();
        String domain =
                optionService.getByPropertyOrDefault(AliOssProperties.OSS_DOMAIN, String.class, "");
        String source =
                optionService.getByPropertyOrDefault(AliOssProperties.OSS_SOURCE, String.class, "");
        String endPoint =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_ENDPOINT).toString();
        String accessKey =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_ACCESS_KEY).toString();
        String accessSecret =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_ACCESS_SECRET).toString();
        String bucketName =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_BUCKET_NAME).toString();
        String styleRule =
                optionService.getByPropertyOrDefault(AliOssProperties.OSS_STYLE_RULE, String.class, "");
        String thumbnailStyleRule = optionService
                .getByPropertyOrDefault(AliOssProperties.OSS_THUMBNAIL_STYLE_RULE, String.class, "");

        // Init OSS client
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);

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
            final String basename =
                    FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            final String timestamp = String.valueOf(System.currentTimeMillis());
            final StringBuilder upFilePath = new StringBuilder();

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
            final PutObjectResult putObjectResult = ossClient.putObject(bucketName,
                    upFilePath.toString(),
                    file.getInputStream());

            if (putObjectResult == null) {
                throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到阿里云失败 ");
            }

            // Response result
            final UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult
                    .setFilePath(StringUtils.isBlank(styleRule) ? filePath : filePath + styleRule);
            uploadResult.setKey(upFilePath.toString());
            uploadResult
                    .setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(file.getSize());

            handleImageMetadata(file, uploadResult, () -> {
                if (ImageUtils.EXTENSION_ICO.equals(extension)) {
                    return filePath;
                } else {
                    return StringUtils.isBlank(thumbnailStyleRule) ? filePath :
                            filePath + thumbnailStyleRule;
                }
            });

            log.info("Uploaded file: [{}] successfully", file.getOriginalFilename());
            return uploadResult;
        } catch (Exception e) {
            throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到阿里云失败 ", e)
                    .setErrorData(file.getOriginalFilename());
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void delete(@NonNull String key) {
        Assert.notNull(key, "File key must not be blank");

        // Get config
        String endPoint =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_ENDPOINT).toString();
        String accessKey =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_ACCESS_KEY).toString();
        String accessSecret =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_ACCESS_SECRET).toString();
        String bucketName =
                optionService.getByPropertyOfNonNull(AliOssProperties.OSS_BUCKET_NAME).toString();

        // Init OSS client
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, accessSecret);

        try {
            ossClient.deleteObject(new DeleteObjectsRequest(bucketName).withKey(key));
        } catch (Exception e) {
            throw new FileOperationException("附件 " + key + " 从阿里云删除失败", e);
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.ALIOSS;
    }

}
