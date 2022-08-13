package com.qinweizhao.blog.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.param.AttachmentQueryParam;
import com.qinweizhao.blog.util.LambdaQueryWrapperX;
import com.qinweizhao.blog.util.MyBatisUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/7/6
 */
@Mapper
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

    /**
     * 分页查询
     *
     * @param param param
     * @return PageResult
     */
    default PageResult<Attachment> selectPageAttachments(AttachmentQueryParam param) {
        Page<Attachment> page = MyBatisUtils.buildPage(param);

        AttachmentType attachmentType = param.getAttachmentType();
        Integer typeValue = null;
        if (!ObjectUtils.isEmpty(attachmentType)) {
            typeValue = attachmentType.getValue();
        }

        Page<Attachment> attachmentPage = selectPage(page, new LambdaQueryWrapperX<Attachment>()
                .eqIfPresent(Attachment::getMediaType, param.getMediaType())
                .likeIfPresent(Attachment::getName, param.getKeyword())
                .eqIfPresent(Attachment::getType, typeValue)
                .orderByDesc(Attachment::getCreateTime)
        );
        return MyBatisUtils.buildSimplePageResult(attachmentPage);
    }
}
