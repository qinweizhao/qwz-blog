package com.qinweizhao.blog.framework.handler.file;

import com.qinweizhao.blog.exception.FileOperationException;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.properties.MinioProperties;
import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.model.support.UploadResult;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.util.FilenameUtils;
import com.qinweizhao.blog.util.ImageUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageReader;
import java.util.Objects;


/**
 * MinIO file handler.
 *
 * @author Wh1te
 * @since 2020-10-03
 */
@Slf4j
@Component
@AllArgsConstructor
public class MinioFileHandler implements FileHandler {

    private final ConfigService configService;

    @NotNull
    @Override
    public UploadResult upload(@NotNull MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");
        // Get config
        String endpoint = configService.getByPropertyOfNonNull(MinioProperties.ENDPOINT).toString();
        String accessKey = configService.getByPropertyOfNonNull(MinioProperties.ACCESS_KEY).toString();
        String accessSecret = configService.getByPropertyOfNonNull(MinioProperties.ACCESS_SECRET).toString();
        String bucketName = configService.getByPropertyOfNonNull(MinioProperties.BUCKET_NAME).toString();
        String source = configService.getByPropertyOrDefault(MinioProperties.SOURCE, String.class, "");

        endpoint = StringUtils.appendIfMissing(endpoint, BlogConst.URL_SEPARATOR);

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, accessSecret)
                .build();

        try {
            String basename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String timestamp = String.valueOf(System.currentTimeMillis());
            String upFilePath = StringUtils.join(StringUtils.isNotBlank(source) ? source + BlogConst.URL_SEPARATOR : "",
                    basename, "_", timestamp, ".", extension);
            String filePath = StringUtils.join(endpoint, bucketName, BlogConst.URL_SEPARATOR, upFilePath);

            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .contentType(file.getContentType())
                    .bucket(bucketName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(upFilePath)
                    .build();
            minioClient.putObject(putObjectArgs);

            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(basename);
            uploadResult.setFilePath(filePath);
            uploadResult.setKey(upFilePath);
            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSuffix(extension);
            uploadResult.setSize(file.getSize());

            // Handle thumbnail
            if (FileHandler.isImageType(uploadResult.getMediaType())) {
                ImageReader image = ImageUtils.getImageReaderFromFile(file.getInputStream(), extension);
                assert image != null;
                uploadResult.setWidth(image.getWidth(0));
                uploadResult.setHeight(image.getHeight(0));
                uploadResult.setThumbPath(filePath);
            }

            return uploadResult;
        } catch (Exception e) {
            log.error("upload file to MINIO failed", e);
            throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到 MinIO 失败 ", e).setErrorData(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull String key) {
        Assert.notNull(key, "File key must not be blank");

        String endPoint = configService.getByPropertyOfNonNull(MinioProperties.ENDPOINT).toString();
        endPoint = StringUtils.appendIfMissing(endPoint, BlogConst.URL_SEPARATOR);

        String accessKey = configService.getByPropertyOfNonNull(MinioProperties.ACCESS_KEY).toString();
        String accessSecret = configService.getByPropertyOfNonNull(MinioProperties.ACCESS_SECRET).toString();
        String bucketName = configService.getByPropertyOfNonNull(MinioProperties.BUCKET_NAME).toString();

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, accessSecret)
                .build();

        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(key)
                    .build());
        } catch (Exception e) {
            log.error("delete MINIO file: [{}] failed", key, e);
            throw new FileOperationException("附件 " + key + " 从 MinIO 删除失败", e).setErrorData(e.getMessage());
        }
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.MINIO;
    }
}
