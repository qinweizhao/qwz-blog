//package com.qinweizhao.blog.controller.admin.api;
//
//import com.qinweizhao.blog.convert.AttachmentConvert;
//import com.qinweizhao.blog.model.dto.AttachmentDTO;
//import com.qinweizhao.blog.model.entity.Attachment;
//import com.qinweizhao.blog.model.enums.AttachmentType;
//import com.qinweizhao.blog.model.params.AttachmentParam;
//import com.qinweizhao.blog.model.params.AttachmentQuery;
//import com.qinweizhao.blog.service.AttachmentService;
//import com.qinweizhao.blog.utils.ResultUtils;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//import static org.springframework.data.domain.Sort.Direction.DESC;
//
///**
// * 附件
// *
// * @author johnniang
// * @author qinweizhao
// * @date 2019-03-21
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping("/api/admin/attachments")
//public class AttachmentController {
//
//    private final AttachmentService attachmentService;
//
//    /**
//     * 分页
//     *
//     * @param pageable        pageable
//     * @param attachmentQuery attachmentQuery
//     * @return Page
//     */
//    @GetMapping
//    public Page<Attachment> page(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable,
//                                    AttachmentQuery attachmentQuery) {
//        Page<Attachment> page = attachmentService.page(pageable, attachmentQuery);
//        return page;
//    }
//
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
//    /**
//     * 列出所有媒体类型
//     *
//     * @return List
//     */
//    @GetMapping("media_types")
//    public List<String> listMediaTypes() {
//        return attachmentService.listAllMediaType();
//    }
//
//    /**
//     * 列出所有类型
//     *
//     * @return List
//     */
//    @GetMapping("types")
//    public List<AttachmentType> listTypes() {
//        return attachmentService.listAllType();
//    }
//}
