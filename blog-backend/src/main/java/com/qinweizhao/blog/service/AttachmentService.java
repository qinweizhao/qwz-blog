package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.param.AttachmentParam;
import com.qinweizhao.blog.model.param.AttachmentQueryParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * Attachment service.
 *
 * @author johnniang
 * @since 2019-03-14
 */
public interface AttachmentService {

    /**
     * 分页
     *
     * @param param param
     * @return Page
     */
    PageResult<AttachmentDTO> page(AttachmentQueryParam param);

    /**
     * 所有附件类型
     *
     * @return List
     */
    List<String> listMediaType();

    /**
     * 附件存储位置
     *
     * @return List
     */
    List<AttachmentType> listAllType();

    /**
     * 删除附件
     *
     * @param id id
     * @return boolean
     */
    boolean removeById(Integer id);

    /**
     * 批量删除附件
     *
     * @param ids ids
     * @return boolean
     */
    boolean removeByIds(List<Integer> ids);


    /**
     * 上传文件
     *
     * @param file file
     * @return boolean
     */
    AttachmentDTO upload(MultipartFile file);

    /**
     * 更新附件
     *
     * @param id    id
     * @param param id
     * @return boolean
     */
    boolean updateById(Integer id, AttachmentParam param);

}
