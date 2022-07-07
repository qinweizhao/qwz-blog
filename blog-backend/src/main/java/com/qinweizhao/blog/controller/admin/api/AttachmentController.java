package com.qinweizhao.blog.controller.admin.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.params.AttachmentParam;
import com.qinweizhao.blog.model.params.AttachmentQuery;
import com.qinweizhao.blog.service.AttachmentService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * 附件
 *
 * @author johnniang
 * @author qinweizhao
 * @date 2019-03-21
 */
@RestController
@RequestMapping("/api/admin/attachments")
public class AttachmentController {

    @Resource
    private AttachmentService attachmentService;


    @GetMapping
    public Page<AttachmentDTO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable,
            AttachmentQuery attachmentQuery) {
        return attachmentService.pageDtosBy(pageable, attachmentQuery);
    }

    /**
     * 通过 id 获取附件详细信息
     * @param id
     * @return
     */
    @GetMapping("{id:\\d+}")
    public AttachmentDTO getBy(@PathVariable("id") Integer id) {
        Attachment attachment = attachmentService.getById(id);
        return attachmentService.convertToDto(attachment);
    }

    /**
     * 更新附件
     * @param attachmentId
     * @param attachmentParam
     * @return
     */
    @PutMapping("{attachmentId:\\d+}")
    public AttachmentDTO updateBy(@PathVariable("attachmentId") Integer attachmentId,
            @RequestBody @Valid AttachmentParam attachmentParam) {
        Attachment attachment = attachmentService.getById(attachmentId);
        attachmentParam.update(attachment);
        return new AttachmentDTO().convertFrom(attachmentService.update(attachment));
    }


    /**
     * 按 id 永久删除附件
     * @param id id
     * @return AttachmentDTO
     */
    @DeleteMapping("{id:\\d+}")
    public AttachmentDTO deletePermanently(@PathVariable("id") Integer id) {
        return attachmentService.convertToDto(attachmentService.removePermanently(id));
    }


    /**
     * 通过 id 数组批量永久删除附件
     * @param ids ids
     * @return List
     */
    @DeleteMapping
    public List<Attachment> deletePermanentlyInBatch(@RequestBody List<Integer> ids) {
        return attachmentService.removePermanently(ids);
    }

    /**
     * 上传单个文件
     * @param file file
     * @return AttachmentDTO
     */
    @PostMapping("upload")
    public AttachmentDTO uploadAttachment(@RequestPart("file") MultipartFile file) {
        return attachmentService.convertToDto(attachmentService.upload(file));
    }

    @PostMapping(value = "uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("Uploads multi files (Invalid in Swagger UI)")
    public List<AttachmentDTO> uploadAttachments(@RequestPart("files") MultipartFile[] files) {
        List<AttachmentDTO> result = new LinkedList<>();

        for (MultipartFile file : files) {
            // Upload single file
            Attachment attachment = attachmentService.upload(file);
            // Convert and add
            result.add(attachmentService.convertToDto(attachment));
        }

        return result;
    }

    @GetMapping("media_types")
    @ApiOperation("Lists all of media types")
    public List<String> listMediaTypes() {
        return attachmentService.listAllMediaType();
    }

    @GetMapping("types")
    @ApiOperation("Lists all of types.")
    public List<AttachmentType> listTypes() {
        return attachmentService.listAllType();
    }
}
