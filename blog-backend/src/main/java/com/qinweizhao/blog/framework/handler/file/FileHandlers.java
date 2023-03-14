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
 * 文件处理器管理
 *
 * @author qinweizhao
 * @since 2019-03-27
 */
@Slf4j
@Component
public class FileHandlers {

    /**
     * 文件处理器容器
     */
    private final ConcurrentHashMap<AttachmentType, FileHandler> fileHandlerMap = new ConcurrentHashMap<>(16);

    public FileHandlers(ApplicationContext applicationContext) {
        // 添加所有文件处理器
        addFileHandlers(applicationContext.getBeansOfType(FileHandler.class).values());
        log.info("已添加{}个文件处理器", fileHandlerMap.size());
    }

    /**
     * 上传文件
     *
     * @param file           file
     * @param attachmentType attachmentType
     * @return upload result
     */
    public UploadResult upload(MultipartFile file, AttachmentType attachmentType) {
        return getSupportedType(attachmentType).upload(file);
    }


    /**
     * 删除附件
     *
     * @param attachment attachment
     */
    public void delete(Attachment attachment) {
        Assert.notNull(attachment, "附件不能为空");
        AttachmentType attachmentType = ValueEnum.valueToEnum(AttachmentType.class, attachment.getType());
        getSupportedType(attachmentType).delete(attachment.getFileKey());
    }

    /**
     * 添加文件处理器
     *
     * @param fileHandlers fileHandlers
     */
    public void addFileHandlers(@Nullable Collection<FileHandler> fileHandlers) {
        if (!CollectionUtils.isEmpty(fileHandlers)) {
            for (FileHandler handler : fileHandlers) {
                if (this.fileHandlerMap.containsKey(handler.getAttachmentType())) {
                    throw new RepeatTypeException("该类型的文件处理器已经存在");
                }
                this.fileHandlerMap.put(handler.getAttachmentType(), handler);
            }
        }
    }

    /**
     * 获取对应类型的文件处理器
     * @param type 类型
     * @return FileHandler
     */
    private FileHandler getSupportedType(AttachmentType type) {
        FileHandler handler = fileHandlerMap.getOrDefault(type, fileHandlerMap.get(AttachmentType.LOCAL));
        if (handler == null) {
            throw new FileOperationException("没有可用的文件处理器来处理文件").setErrorData(type);
        }
        return handler;
    }
}
