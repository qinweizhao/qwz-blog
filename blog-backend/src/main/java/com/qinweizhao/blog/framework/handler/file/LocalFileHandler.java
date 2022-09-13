package com.qinweizhao.blog.framework.handler.file;

import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.exception.FileOperationException;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.support.UploadResult;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.util.FilenameUtils;
import com.qinweizhao.blog.util.HaloUtils;
import com.qinweizhao.blog.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import static com.qinweizhao.blog.model.support.BlogConst.FILE_SEPARATOR;

/**
 * Local file handler.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 2019-03-27
 */
@Slf4j
@Component
public class LocalFileHandler implements FileHandler {

    /**
     * Upload sub directory.
     */
    private final static String UPLOAD_SUB_DIR = "image/";

    private final static String THUMBNAIL_SUFFIX = "-thumbnail";
    /**
     * Thumbnail width.
     */
    private final static int THUMB_WIDTH = 256;
    /**
     * Thumbnail height.
     */
    private final static int THUMB_HEIGHT = 256;
    private final ConfigService configService;
    private final String workDir;
    ReentrantLock lock = new ReentrantLock();
    @Resource
    private MyBlogProperties myBlogProperties;

    public LocalFileHandler(ConfigService configService,
                            MyBlogProperties myBlogProperties) {
        this.configService = configService;

        workDir = FileHandler.normalizeDirectory(myBlogProperties.getWorkDir());

        // Check work directory
        checkWorkDir();
    }

    /**
     * Check work directory.
     */
    private void checkWorkDir() {
        // Get work path
        Path workPath = Paths.get(workDir);

        // Check file type
        Assert.isTrue(Files.isDirectory(workPath), workDir + " isn't a directory");

        // Check readable
        Assert.isTrue(Files.isReadable(workPath), workDir + " isn't readable");

        // Check writable
        Assert.isTrue(Files.isWritable(workPath), workDir + " isn't writable");
    }

    @Override
    public UploadResult upload(MultipartFile file) {
        Assert.notNull(file, "Multipart file must not be null");

        // Get current time
        Calendar current = Calendar.getInstance(configService.getLocale());
        // Get month and day of month
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH) + 1;

        String monthString = month < 10 ? "0" + month : String.valueOf(month);

        // Build directory
        String subDir;
        if (myBlogProperties.isProductionEnv()) {
            subDir = UPLOAD_SUB_DIR + year + FILE_SEPARATOR + monthString + FILE_SEPARATOR;
        } else {
            String uploadSubDir = "blog-resource/image/";
            subDir = uploadSubDir + year + FILE_SEPARATOR + monthString + FILE_SEPARATOR;
        }
        String originalBasename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));

        // Get basename
        String basename = originalBasename + '-' + HaloUtils.randomUUIDWithoutDash();

        // Get extension
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        log.debug("Base name: [{}], extension: [{}] of original filename: [{}]", basename, extension, file.getOriginalFilename());

        // Build sub file path
        String subFilePath = subDir + basename + '.' + extension;

        // Get upload path
        Path uploadPath = Paths.get(workDir, subFilePath);

        log.info("Uploading file: [{}]to directory: [{}]", file.getOriginalFilename(), uploadPath);

        try {
            // TODO Synchronize here
            // Create directory
            Files.createDirectories(uploadPath.getParent());
            Files.createFile(uploadPath);

            // Upload this file
            file.transferTo(uploadPath);

            // Build upload result
            UploadResult uploadResult = new UploadResult();
            uploadResult.setFilename(originalBasename);
            uploadResult.setFilePath(subFilePath);
            System.out.println("subFilePath = " + subFilePath);
            uploadResult.setKey(subFilePath);
            uploadResult.setSuffix(extension);
            uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
            uploadResult.setSize(file.getSize());

            // TODO refactor this: if image is svg ext. extension
            boolean isSvg = "svg".equals(extension);

            // Check file type
            if (FileHandler.isImageType(uploadResult.getMediaType()) && !isSvg) {
                lock.lock();
                try (InputStream uploadFileInputStream = new FileInputStream(uploadPath.toFile())) {
                    // Upload a thumbnail
                    String thumbnailBasename = basename + THUMBNAIL_SUFFIX;
                    String thumbnailSubFilePath = subDir + thumbnailBasename + '.' + extension;
                    Path thumbnailPath = Paths.get(workDir + thumbnailSubFilePath);

                    // Read as image
                    BufferedImage originalImage = ImageUtils.getImageFromFile(uploadFileInputStream, extension);
                    // Set width and height
                    uploadResult.setWidth(originalImage.getWidth());
                    uploadResult.setHeight(originalImage.getHeight());

                    // Generate thumbnail
                    boolean result = generateThumbnail(originalImage, thumbnailPath, extension);
                    if (result) {
                        // Set thumb path
                        uploadResult.setThumbPath(thumbnailSubFilePath);
                    } else {
                        // If generate error
                        uploadResult.setThumbPath(subFilePath);
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                uploadResult.setThumbPath(subFilePath);
            }

            log.info("Uploaded file: [{}] to directory: [{}] successfully", file.getOriginalFilename(), uploadPath.toString());
            return uploadResult;
        } catch (IOException e) {
            throw new FileOperationException("上传附件失败").setErrorData(uploadPath);
        }
    }

    @Override
    public void delete(String key) {
        Assert.hasText(key, "File key must not be blank");
        // Get path
        Path path = Paths.get(workDir, key);

        // Delete the file key
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileOperationException("附件 " + key + " 删除失败", e);
        }

        // Delete thumb if necessary
        String basename = FilenameUtils.getBasename(key);
        String extension = FilenameUtils.getExtension(key);

        // Get thumbnail name
        String thumbnailName = basename + THUMBNAIL_SUFFIX + '.' + extension;

        // Get thumbnail path
        Path thumbnailPath = Paths.get(path.getParent().toString(), thumbnailName);

        // Delete thumbnail file
        try {
            boolean deleteResult = Files.deleteIfExists(thumbnailPath);
            if (!deleteResult) {
                log.warn("Thumbnail: [{}] may not exist", thumbnailPath);
            }
        } catch (IOException e) {
            throw new FileOperationException("附件缩略图 " + thumbnailName + " 删除失败", e);
        }
    }

    @Override
    public AttachmentType getAttachmentType() {
        return AttachmentType.LOCAL;
    }

    private boolean generateThumbnail(BufferedImage originalImage, Path thumbPath, String extension) {
        Assert.notNull(originalImage, "Image must not be null");
        Assert.notNull(thumbPath, "Thumb path must not be null");


        boolean result = false;
        // Create the thumbnail
        try {
            Files.createFile(thumbPath);
            // Convert to thumbnail and copy the thumbnail
            log.debug("Trying to generate thumbnail: [{}]", thumbPath);
            Thumbnails.of(originalImage).size(THUMB_WIDTH, THUMB_HEIGHT).keepAspectRatio(true).toFile(thumbPath.toFile());
            log.debug("Generated thumbnail image, and wrote the thumbnail to [{}]", thumbPath);
            result = true;
        } catch (Throwable t) {
            log.warn("Failed to generate thumbnail: " + thumbPath, t);
        } finally {
            // Disposes of this graphics context and releases any system resources that it is using.
            originalImage.getGraphics().dispose();
        }
        return result;
    }
}
