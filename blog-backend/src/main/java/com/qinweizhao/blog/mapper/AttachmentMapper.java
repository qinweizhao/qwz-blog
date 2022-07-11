package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/6
 */
public interface AttachmentMapper extends BaseMapper<Attachment> {

//
//    /**
//     * 查询分页
//     *
//     * @param pageable        pageable
//     * @param attachmentQuery attachmentQuery
//     * @return Page
//     */
//    default Page<Attachment> selectPage(Pageable pageable, AttachmentQuery attachmentQuery) {
//        IPage<Attachment> page = MyBatisUtils.buildPage(pageable);
//        selectPage(page, new LambdaQueryWrapper<Attachment>()
//                .like(Attachment::getName, attachmentQuery.getKeyword())
//                .eq(Attachment::getType, attachmentQuery.getAttachmentType())
//                .eq(Attachment::getMediaType, attachmentQuery.getMediaType()));
//        return new PageImpl<>(page.getRecords(), pageable, page.getTotal());
//    }


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

    /**
     * 通过路径统计
     *
     * @param path path
     * @return long
     */
    default long countByPath(String path) {
        return selectCount(new LambdaQueryWrapper<Attachment>()
                .eq(Attachment::getPath, path)
        );
    }
}
