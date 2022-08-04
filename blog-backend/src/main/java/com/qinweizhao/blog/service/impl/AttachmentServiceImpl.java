package com.qinweizhao.blog.service.impl;

import com.qinweizhao.blog.convert.AttachmentConvert;
import com.qinweizhao.blog.framework.handler.file.FileHandlers;
import com.qinweizhao.blog.mapper.AttachmentMapper;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.params.AttachmentQueryParam;
import com.qinweizhao.blog.service.AttachmentService;
import com.qinweizhao.blog.service.OptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AttachmentService implementation
 *
 * @author ryanwang
 * @author johnniang
 * @date 2019-03-14
 */
@Slf4j
@Service
@AllArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {


    private final AttachmentMapper attachmentMapper;

    private final OptionService optionService;

    private final FileHandlers fileHandlers;

    @Override
    public PageResult<AttachmentDTO> page(AttachmentQueryParam param) {
        PageResult<Attachment> result = attachmentMapper.selectPageAttachments(param);
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
        fileHandlers.delete(deletedAttachment);
        return attachmentMapper.deleteById(id) > 0;
    }

//    @Override
//    public Attachment upload(MultipartFile file) {
//        Assert.notNull(file, "Multipart 不能为 null");
//
//        AttachmentType attachmentType = getAttachmentType();
//
//        log.debug("开始上传 类型: [{}], 文件: [{}]", attachmentType, file.getOriginalFilename());
//
//        // 上传文件
//        UploadResult uploadResult = fileHandlers.upload(file, attachmentType);
//
//        log.debug("附件类型: [{}]", attachmentType);
//        log.debug("上传结果: [{}]", uploadResult);
//
//        // 构建 附件实体
//        Attachment attachment = new Attachment();
//        attachment.setName(uploadResult.getFilename());
//        // 转换分隔符
//        attachment.setPath(HaloUtils.changeFileSeparatorToUrlSeparator(uploadResult.getFilePath()));
//        attachment.setFileKey(uploadResult.getKey());
//        attachment.setThumbPath(uploadResult.getThumbPath());
//        attachment.setMediaType(uploadResult.getMediaType().toString());
//        attachment.setSuffix(uploadResult.getSuffix());
//        attachment.setWidth(uploadResult.getWidth());
//        attachment.setHeight(uploadResult.getHeight());
//        attachment.setSize(uploadResult.getSize());
//        attachment.setType(attachmentType);
//
//        log.debug("创建的附件实体为: [{}]", attachment);
//
//        // 创建并返回
//        return create(attachment);
//    }
//
//    @Override
//    public Attachment removePermanently(Integer id) {
//        Attachment deletedAttachment = this.baseMapper.selectById(id);
//        // 从数据库中删除它
//        this.baseMapper.deleteById(id);
//
//        // 删除文件
//        fileHandlers.delete(deletedAttachment);
//
//        log.debug("已删除的附件: [{}]", deletedAttachment);
//
//        return deletedAttachment;
//    }
//
//    @Override
//    public List<Attachment> removePermanently(@Nullable Collection<Integer> ids) {
//        if (CollectionUtils.isEmpty(ids)) {
//            return Collections.emptyList();
//        }
//
//        return ids.stream().map(this::removePermanently).collect(Collectors.toList());
//    }
//
//
//    @Override
//    public List<String> listAllMediaType() {
//        return this.baseMapper.selectListMediaType();
//    }
//
//    @Override
//    public List<AttachmentType> listAllType() {

//    }
//
//    @Override
//    public List<Attachment> replaceUrl(@NotNull String oldUrl, @NotNull String newUrl) {
//        List<Attachment> attachments = this.list();
//
//        List<Attachment> replaced = new ArrayList<>();
//        attachments.forEach(attachment -> {
//            if (StringUtils.isNotEmpty(attachment.getPath())) {
//                attachment.setPath(attachment.getPath().replaceAll(oldUrl, newUrl));
//            }
//            if (StringUtils.isNotEmpty(attachment.getThumbPath())) {
//                attachment.setThumbPath(attachment.getThumbPath().replaceAll(oldUrl, newUrl));
//            }
//            replaced.add(attachment);
//        });
//
//        boolean b = this.updateBatchById(replaced);
//
//        return ResultUtils.judge(b, replaced);
//    }
//
//    public Attachment create(Attachment attachment) {
//        Assert.notNull(attachment, "附件不能为空");
//
//        // 检查附件路径
//        pathMustNotExist(attachment);
//        boolean b = this.save(attachment);
//
//        return ResultUtils.judge(b, attachment);
//    }
//
//    /**
//     * Attachment path must not be exist.
//     *
//     * @param attachment attachment must not be null
//     */
//    private void pathMustNotExist(@NonNull Attachment attachment) {
//        Assert.notNull(attachment, "Attachment must not be null");
//
//        long pathCount = this.baseMapper.countByPath(attachment.getPath());
//
//        if (pathCount > 0) {
//            throw new AlreadyExistsException("附件路径为 " + attachment.getPath() + " 已经存在");
//        }
//    }
//
//    /**
//     * Get attachment type from options.
//     *
//     * @return attachment type
//     */
//    @NonNull
//    private AttachmentType getAttachmentType() {
//        return Objects.requireNonNull(optionService.getEnumByPropertyOrDefault(AttachmentProperties.ATTACHMENT_TYPE, AttachmentType.class, AttachmentType.LOCAL));
//    }
}
