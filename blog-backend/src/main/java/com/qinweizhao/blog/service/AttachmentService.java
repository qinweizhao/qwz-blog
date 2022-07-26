package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.param.AttachmentQueryParam;

import java.util.List;


/**
 * Attachment service.
 *
 * @author johnniang
 * @date 2019-03-14
 */
public interface AttachmentService {

    /**
     * 分页
     *
     * @param attachmentQueryParam attachmentQuery
     * @return Page
     */
    PageResult<AttachmentDTO> page(AttachmentQueryParam attachmentQueryParam);

    /**
     * 所有附件类型
     * @return List
     */
    List<String> listMediaType();

    /**
     * 附件存储位置
     * @return List
     */
    List<AttachmentType> listAllType();

    /**
     * 删除附件
     * @param id id
     * @return boolean
     */
    boolean removeById(Integer id);


//    /**
//     * 上传文件
//     *
//     * @param file file
//     * @return attachment attachment
//     * @throws FileOperationException e
//     */
//    Attachment upload(MultipartFile file);
//
//    /**
//     * Removes attachment permanently.
//     *
//     * @param id attachment id must not be null
//     * @return attachment detail deleted
//     */
//    Attachment removePermanently(Integer id);
//
//    /**
//     * Removes attachment permanently in batch.
//     *
//     * @param ids attachment ids must not be null
//     * @return attachment detail list deleted
//     */
//    List<Attachment> removePermanently(Collection<Integer> ids);
//
//    /**
//     * List all media type.
//     *
//     * @return list of media type
//     */
//    List<String> listAllMediaType();
//
//    /**
//     * List all type.
//     *
//     * @return list of type.
//     */
//    List<AttachmentType> listAllType();
//
//    /**
//     * Replace attachment url in batch.
//     *
//     * @param oldUrl old blog url.
//     * @param newUrl new blog url.
//     * @return replaced attachments.
//     */
//    List<Attachment> replaceUrl(String oldUrl, String newUrl);
}
