package com.qinweizhao.blog.framework.handler.file;

import com.qinweizhao.blog.exception.FileOperationException;
import com.qinweizhao.blog.exception.RepeatTypeException;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.support.UploadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File handler manager.
 *
 * @author johnniang
 * @date 2019-03-27
 */
@Slf4j
@Component
public class FileHandlers {

    /**
     * File handler container.
     */
    private final ConcurrentHashMap<AttachmentType, FileHandler> fileHandlers = new ConcurrentHashMap<>(16);

    public FileHandlers(ApplicationContext applicationContext) {
        // Add all file handler
        addFileHandlers(applicationContext.getBeansOfType(FileHandler.class).values());
        log.info("Registered {} file handler(s)", fileHandlers.size());
    }

    /**
     * Uploads files.
     *
     * @param file           multipart file must not be null
     * @param attachmentType attachment type must not be null
     * @return upload result
     * @throws FileOperationException throws when fail to delete attachment or no available file handler to upload it
     */

    public UploadResult upload(MultipartFile file, AttachmentType attachmentType) {
        return getSupportedType(attachmentType).upload(file);
    }


    /**
     * 删除附件
     *
     * @param attachment attachment
     * @throws FileOperationException 当无法删除附件或没有可用的文件处理程序删除它时抛出
     */
    public void delete(Attachment attachment) {
        Assert.notNull(attachment, "Attachment must not be null");
        AttachmentType attachmentType = ValueEnum.valueToEnum(AttachmentType.class, attachment.getType());
        getSupportedType(attachmentType)
                .delete(attachment.getFileKey());
    }

    /**
     * Adds file handlers.
     *
     * @param fileHandlers file handler collection
     * @return current file handlers
     */

    public FileHandlers addFileHandlers(@Nullable Collection<FileHandler> fileHandlers) {
        if (!CollectionUtils.isEmpty(fileHandlers)) {
            for (FileHandler handler : fileHandlers) {
                if (this.fileHandlers.containsKey(handler.getAttachmentType())) {
                    throw new RepeatTypeException("Same attachment type implements must be unique");
                }
                this.fileHandlers.put(handler.getAttachmentType(), handler);
            }
        }
        return this;
    }

    private FileHandler getSupportedType(AttachmentType type) {
        FileHandler handler = fileHandlers.getOrDefault(type, fileHandlers.get(AttachmentType.LOCAL));
        if (handler == null) {
            throw new FileOperationException("没有可用的文件处理器来处理文件").setErrorData(type);
        }
        return handler;
    }
}
