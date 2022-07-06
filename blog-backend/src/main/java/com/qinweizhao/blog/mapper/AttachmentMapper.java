package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/6
 */
public interface AttachmentMapper extends BaseMapper<Attachment> {

    /**
     * 查找所有 mediaType
     *
     * @return List
     */
    List<String> selectListMediaType();

    /**
     * 查找所有 attachment type
     *
     * @return List
     */
    List<AttachmentType> selectListType();
}
