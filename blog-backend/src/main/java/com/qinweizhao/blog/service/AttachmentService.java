package com.qinweizhao.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qinweizhao.blog.exception.FileOperationException;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.params.AttachmentQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;


/**
 * Attachment service.
 *
 * @author johnniang
 * @date 2019-03-14
 */
public interface AttachmentService extends IService<Attachment> {

    /**
     * 分页
     *
     * @param pageable        pageable
     * @param attachmentQuery attachmentQuery
     * @return Page
     */
    Page<Attachment> page(Pageable pageable, AttachmentQuery attachmentQuery);

    /**
     * 上传文件
     *
     * @param file file
     * @return attachment attachment
     * @throws FileOperationException e
     */
    Attachment upload(MultipartFile file);

    /**
     * Removes attachment permanently.
     *
     * @param id attachment id must not be null
     * @return attachment detail deleted
     */
    Attachment removePermanently(Integer id);

    /**
     * Removes attachment permanently in batch.
     *
     * @param ids attachment ids must not be null
     * @return attachment detail list deleted
     */
    List<Attachment> removePermanently(Collection<Integer> ids);

    /**
     * List all media type.
     *
     * @return list of media type
     */
    List<String> listAllMediaType();

    /**
     * List all type.
     *
     * @return list of type.
     */
    List<AttachmentType> listAllType();

    /**
     * Replace attachment url in batch.
     *
     * @param oldUrl old blog url.
     * @param newUrl new blog url.
     * @return replaced attachments.
     */
    List<Attachment> replaceUrl(String oldUrl, String newUrl);
}
