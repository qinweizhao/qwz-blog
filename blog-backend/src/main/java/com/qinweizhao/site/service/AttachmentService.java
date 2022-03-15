package com.qinweizhao.site.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import com.qinweizhao.site.exception.FileOperationException;
import com.qinweizhao.site.model.dto.AttachmentDTO;
import com.qinweizhao.site.model.entity.Attachment;
import com.qinweizhao.site.model.enums.AttachmentType;
import com.qinweizhao.site.model.params.AttachmentQuery;
import com.qinweizhao.site.service.base.CrudService;

import java.util.Collection;
import java.util.List;


/**
 * Attachment service.
 *
 * @author johnniang
 * @date 2019-03-14
 */
public interface AttachmentService extends CrudService<Attachment, Integer> {

    /**
     * Pages attachment output dtos.
     *
     * @param pageable        page info must not be null
     * @param attachmentQuery attachment query param.
     * @return a page of attachment output dto
     */
    @NonNull
    Page<AttachmentDTO> pageDtosBy(@NonNull Pageable pageable, AttachmentQuery attachmentQuery);

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
    @NonNull
    Attachment removePermanently(@NonNull Integer id);

    /**
     * Removes attachment permanently in batch.
     *
     * @param ids attachment ids must not be null
     * @return attachment detail list deleted
     */
    @NonNull
    List<Attachment> removePermanently(@NonNull Collection<Integer> ids);

    /**
     * Converts to attachment output dto.
     *
     * @param attachment attachment must not be null
     * @return an attachment output dto
     */
    @NonNull
    AttachmentDTO convertToDto(@NonNull Attachment attachment);

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
    List<Attachment> replaceUrl(@NonNull String oldUrl, @NonNull String newUrl);
}
