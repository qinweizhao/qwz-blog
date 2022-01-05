package com.qinweizhao.site.handler.file;

import com.qinweizhao.site.exception.FileOperationException;
import com.qinweizhao.site.model.enums.AttachmentType;
import com.qinweizhao.site.model.properties.MinioProperties;
import com.qinweizhao.site.model.support.HaloConst;
import com.qinweizhao.site.model.support.UploadResult;
import com.qinweizhao.site.service.OptionService;
import com.qinweizhao.site.utils.FilenameUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


/**
 * MinIO file handler.
 *
 * @author Wh1te
 * @date 2020-10-03
 */
@Slf4j
@Component
public class MinioFileHandler implements FileHandler {

    private final OptionService optionService;

    public MinioFileHandler(OptionService optionService) {
        this.optionService = optionService;
    }

    @NonNull
    @Override
    public UploadResult upload(@NonNull MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");
        // Get config
        String endpoint = optionService.getByPropertyOfNonNull(MinioProperties.ENDPOINT).toString();
        String accessKey =
                optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_KEY).toString();
        String accessSecret =
                optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_SECRET).toString();
        String bucketName =
                optionService.getByPropertyOfNonNull(MinioProperties.BUCKET_NAME).toString();
        String source =
                optionService.getByPropertyOrDefault(MinioProperties.SOURCE, String.class, "");
        String region =
                optionService.getByPropertyOrDefault(MinioProperties.REGION, String.class, "us-east-1");

        endpoint = StringUtils.appendIfMissing(endpoint, HaloConst.URL_SEPARATOR);

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, accessSecret)
                .region(region)
                .build();

        try {
            String basename =
                    FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String timestamp = String.valueOf(System.currentTimeMillis());
            String upFilePath = StringUtils
                    .join(StringUtils.isNotBlank(source) ? source + HaloConst.URL_SEPARATOR : "",
                            basename, "_", timestamp, ".", extension);
            String filePath =
                    StringUtils.join(endpoint, bucketName, HaloConst.URL_SEPARATOR, upFilePath);

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .contentType(file.getContentType())
                    .bucket(bucketName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(upFilePath)
                    .build();
            minioClient.ignoreCertCheck();
            minioClient.putObject(putObjectArgs);

            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult.setFilePath(filePath);
            uploadResult.setKey(upFilePath);
            uploadResult
                    .setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(file.getSize());

            // Handle thumbnail
            handleImageMetadata(file, uploadResult, () -> filePath);

            return uploadResult;
        } catch (Exception e) {
            log.error("upload file to MINIO failed", e);
            throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到 MinIO 失败 ",
                    e).setErrorData(e.getMessage());
        }
    }

    @Override
    public void delete(@NonNull String key) {
        Assert.notNull(key, "File key must not be blank");

        String endPoint = optionService.getByPropertyOfNonNull(MinioProperties.ENDPOINT).toString();
        endPoint = StringUtils.appendIfMissing(endPoint, HaloConst.URL_SEPARATOR);

        String accessKey =
                optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_KEY).toString();
        String accessSecret =
                optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_SECRET).toString();
        String bucketName =
                optionService.getByPropertyOfNonNull(MinioProperties.BUCKET_NAME).toString();
        String region =
                optionService.getByPropertyOrDefault(MinioProperties.REGION, String.class, "us-east-1");

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, accessSecret)
                .region(region)
                .build();

        try {
            minioClient.ignoreCertCheck();
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .build());
        } catch (Exception e) {
            log.error("delete MINIO file: [{}] failed", key, e);
            throw new FileOperationException("附件 " + key + " 从 MinIO 删除失败", e)
                    .setErrorData(e.getMessage());
        }
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.MINIO;
    }
}
