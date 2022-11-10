package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.param.AttachmentParam;
import com.qinweizhao.blog.model.param.AttachmentQueryParam;
import com.qinweizhao.blog.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 附件
 *
 * @author qinweizhao
 * @since 2019-03-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/attachment")
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

    /**
     * 列出所有媒体类型
     *
     * @return List
     */
    @GetMapping("media-types")
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

    /**
     * 更新附件
     *
     * @param id    id
     * @param param param
     * @return AttachmentDTO
     */
    @PutMapping("{attachmentId:\\d+}")
    public Boolean updateBy(@PathVariable("attachmentId") Integer id, @RequestBody @Valid AttachmentParam param) {
        return attachmentService.updateById(id, param);
    }

    /**
     * 删除附件
     *
     * @param id id
     * @return AttachmentDTO
     */
    @DeleteMapping("{id:\\d+}")
    public Boolean deletePermanently(@PathVariable("id") Integer id) {
        return attachmentService.removeById(id);
    }

    /**
     * 批量永久删除附件
     *
     * @param ids ids
     * @return Boolean
     */
    @DeleteMapping
    public Boolean deletePermanentlyInBatch(@RequestBody List<Integer> ids) {
        return attachmentService.removeByIds(ids);
    }

    /**
     * 上传单个文件
     *
     * @param file file
     * @return AttachmentDTO
     */
    @PostMapping("upload")
    public AttachmentDTO uploadAttachment(@RequestPart("file") MultipartFile file) {
        return attachmentService.upload(file);
    }

    /**
     * 上传多个文件
     *
     * @param files files
     * @return List<AttachmentDTO>
     */
    @PostMapping(value = "uploads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<AttachmentDTO> uploadAttachments(@RequestPart("files") MultipartFile[] files) {
        List<AttachmentDTO> result = new ArrayList<>();
        for (MultipartFile file : files) {
            AttachmentDTO attachmentDTO = attachmentService.upload(file);
            result.add(attachmentDTO);
        }
        return result;
    }

}
