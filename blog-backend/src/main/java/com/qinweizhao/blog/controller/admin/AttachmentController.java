package com.qinweizhao.blog.controller.admin;

import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.param.AttachmentQueryParam;
import com.qinweizhao.blog.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 附件
 *
 * @author johnniang
 * @author qinweizhao
 * @date 2019-03-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    /**
     * 分页
     *
     * @param param param
     * @return Page
     */
    @GetMapping
    public PageResult<AttachmentDTO> page(AttachmentQueryParam param) {
        return attachmentService.page(param);
    }

//    /**
//     * 通过 id 获取附件详细信息
//     *
//     * @param id id
//     * @return AttachmentDTO
//     */
//    @GetMapping("{id:\\d+}")
//    public AttachmentDTO getBy(@PathVariable("id") Integer id) {
//        Attachment attachment = attachmentService.getById(id);
//        return AttachmentConvert.INSTANCE.convert(attachment);
//    }
//
//    /**
//     * 更新附件
//     *
//     * @param attachmentId    attachmentId
//     * @param attachmentParam attachmentParam
//     * @return AttachmentDTO
//     */
//    @PutMapping("{attachmentId:\\d+}")
//    public AttachmentDTO updateBy(@PathVariable("attachmentId") Integer attachmentId,
//                                  @RequestBody @Valid AttachmentParam attachmentParam) {
//        Attachment attachment = AttachmentConvert.INSTANCE.convert(attachmentParam);
//        attachment.setId(attachmentId);
//        boolean b = attachmentService.updateById(attachment);
//        AttachmentDTO attachmentDTO = AttachmentConvert.INSTANCE.convert(attachment);
//        return ResultUtils.judge(b, attachmentDTO);
//    }
//
//
//    /**
//     * 删除附件
//     *
//     * @param id id
//     * @return AttachmentDTO
//     */
//    @DeleteMapping("{id:\\d+}")
//    public AttachmentDTO deletePermanently(@PathVariable("id") Integer id) {
//        boolean b = attachmentService.removeById(id);
//        return ResultUtils.judge(b, new AttachmentDTO());
//    }
//
//
//    /**
//     * 批量永久删除附件
//     *
//     * @param ids ids
//     * @return List
//     */
//    @DeleteMapping
//    public List<Attachment> deletePermanentlyInBatch(@RequestBody List<Integer> ids) {
//        boolean b = attachmentService.removeByIds(ids);
//        return ResultUtils.judge(b, new ArrayList<>());
//    }
//
//    /**
//     * 上传单个文件
//     *
//     * @param file file
//     * @return AttachmentDTO
//     */
//    @PostMapping("upload")
//    public AttachmentDTO uploadAttachment(@RequestPart("file") MultipartFile file) {
//        return AttachmentConvert.INSTANCE.convert(attachmentService.upload(file));
//    }
//
//
//    /**
//     * 上传多个文件
//     *
//     * @param files files
//     * @return List<AttachmentDTO>
//     */
//    @PostMapping(value = "uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public List<AttachmentDTO> uploadAttachments(@RequestPart("files") MultipartFile[] files) {
//        List<AttachmentDTO> result = new LinkedList<>();
//
//        for (MultipartFile file : files) {
//            // Upload single file
//            Attachment attachment = attachmentService.upload(file);
//            // Convert and add
//            result.add(AttachmentConvert.INSTANCE.convert(attachment));
//        }
//
//        return result;
//    }
//
    /**
     * 列出所有媒体类型
     *
     * @return List
     */
    @GetMapping("media_types")
    public List<String> listMediaTypes() {
        return attachmentService.listMediaType();
    }

    /**
     * 列出所有类型
     *
     * @return List
     */
    @GetMapping("types")
    public List<AttachmentType> listTypes() {
        return attachmentService.listAllType();
    }
}
