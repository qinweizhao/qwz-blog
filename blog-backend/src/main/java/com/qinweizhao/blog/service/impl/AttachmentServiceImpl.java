package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.framework.handler.file.FileHandlers;
import com.qinweizhao.blog.mapper.AttachmentMapper;
import com.qinweizhao.blog.model.convert.AttachmentConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.param.AttachmentParam;
import com.qinweizhao.blog.model.param.AttachmentQueryParam;
import com.qinweizhao.blog.model.properties.AttachmentProperties;
import com.qinweizhao.blog.model.support.UploadResult;
import com.qinweizhao.blog.service.AttachmentService;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.util.HaloUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * AttachmentService implementation
 *
 * @author qinweizhao
 * @since 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentMapper attachmentMapper;

    private final ConfigService configService;

    private final FileHandlers fileHandlers;

    @Override
    public PageResult<AttachmentDTO> page(AttachmentQueryParam param) {
        PageResult<Attachment> result = attachmentMapper.selectPageAttachments(param);

        String blogBaseUrl = configService.getBlogBaseUrl();


        result.getContent().stream().filter(item -> Objects.equals(item.getType(), AttachmentType.LOCAL.getValue())).forEach(item -> {
            String fullPath = StringUtils.join(blogBaseUrl, "/", item.getPath());
            item.setPath(fullPath);
            item.setThumbPath(fullPath);
        });
        return AttachmentConvert.INSTANCE.convertToDTO(result);
    }

    @Override
    public List<String> listMediaType() {
        return attachmentMapper.selectListMediaType();
    }

    @Override
    public List<AttachmentType> listAllType() {
        return attachmentMapper.selectListType();
    }

    @Override
    public boolean removeById(Integer id) {
        Attachment deletedAttachment = attachmentMapper.selectById(id);
        try {
            fileHandlers.delete(deletedAttachment);
        } catch (Exception e) {
            log.info("文件删除失败");
        }
        return attachmentMapper.deleteById(id) > 0;
    }

    @Override
    public boolean removeByIds(List<Integer> ids) {
        ids.forEach(this::removeById);
        return true;
    }

    @Override
    public AttachmentDTO upload(MultipartFile file) {
        Assert.notNull(file, "Multipart 不能为 null");

        AttachmentType attachmentType = getAttachmentType();

        log.debug("开始上传 类型: [{}], 文件: [{}]", attachmentType, file.getOriginalFilename());

        // 上传文件
        UploadResult uploadResult = fileHandlers.upload(file, attachmentType);

        log.debug("附件类型: [{}]", attachmentType);
        log.debug("上传结果: [{}]", uploadResult);

        // 构建 附件实体
        Attachment attachment = new Attachment();
        attachment.setName(uploadResult.getFilename());
        // 转换分隔符
        attachment.setPath(HaloUtils.changeFileSeparatorToUrlSeparator(uploadResult.getFilePath()));
        attachment.setFileKey(uploadResult.getKey());
        attachment.setThumbPath(uploadResult.getThumbPath());
        attachment.setMediaType(uploadResult.getMediaType().toString());
        attachment.setSuffix(uploadResult.getSuffix());
        attachment.setWidth(uploadResult.getWidth());
        attachment.setHeight(uploadResult.getHeight());
        attachment.setSize(uploadResult.getSize());
        attachment.setType(attachmentType.getValue());

        log.debug("创建的附件实体为: [{}]", attachment);
        int i = attachmentMapper.insert(attachment);
        log.debug("上传附件结果: [{}]", i > 0);

        // 创建并返回
        String blogBaseUrl = configService.getBlogBaseUrl();


        AttachmentDTO result = AttachmentConvert.INSTANCE.convert(attachment);
        if (Objects.equals(result.getType(), AttachmentType.LOCAL)) {
            String fullPath = StringUtils.join(blogBaseUrl, "/", result.getPath());
            String fullThumbPath = StringUtils.join(blogBaseUrl, "/", result.getThumbPath());

            result.setPath(fullPath);
            result.setThumbPath(fullThumbPath);
        }

        return result;
    }

    @Override
    public boolean updateById(Integer id, AttachmentParam param) {
        Attachment attachment = AttachmentConvert.INSTANCE.convert(param);
        attachment.setId(id);
        return attachmentMapper.updateById(attachment) == 1;
    }

    /**
     * 从配置中获取附件类型
     *
     * @return attachment type
     */
    private AttachmentType getAttachmentType() {
        return Objects.requireNonNull(configService.getEnumByPropertyOrDefault(AttachmentProperties.ATTACHMENT_TYPE, AttachmentType.class, AttachmentType.LOCAL));
    }
}
