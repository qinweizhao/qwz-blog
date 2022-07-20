package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.AttachmentDTO;
import com.qinweizhao.blog.model.entity.Attachment;
import com.qinweizhao.blog.model.enums.AttachmentType;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.params.AttachmentParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface AttachmentConvert {

    AttachmentConvert INSTANCE = Mappers.getMapper(AttachmentConvert.class);

    /**
     * convertToDTO
     *
     * @param page page
     * @return dto
     */
    PageResult<AttachmentDTO> convertToDTO(PageResult<Attachment> page);

    /**
     * convert
     *
     * @param attachment attachment
     * @return AttachmentDTO
     */
    AttachmentDTO convert(Attachment attachment);

    /**
     * convert
     *
     * @param param param
     * @return AttachmentDTO
     */
    Attachment convert(AttachmentParam param);

    /**
     * 状态转换
     *
     * @param type type
     * @return AttachmentType
     */
    default AttachmentType statusToEnum(Integer type) {
        return ValueEnum.valueToEnum(AttachmentType.class, type);
    }

    /**
     * 状态转换
     *
     * @param type type
     * @return AttachmentType
     */
    default Integer statusToInteger(AttachmentType type) {
        if (type == null) {
            return null;
        }

        Integer attachmentType;
        switch (type) {
            case LOCAL:
                attachmentType = 0;
                break;
            case UPOSS:
                attachmentType = 1;
                break;
            case QINIUOSS:
                attachmentType = 2;
                break;
            case SMMS:
                attachmentType = 3;
                break;
            case ALIOSS:
                attachmentType = 4;
                break;
            case BAIDUBOS:
                attachmentType = 5;
                break;
            case TENCENTCOS:
                attachmentType = 6;
                break;
            case HUAWEIOBS:
                attachmentType = 7;
                break;
            case MINIO:
                attachmentType = 8;
                break;
            default:
                attachmentType = null;
        }
        return attachmentType;
    }

}
