//package com.qinweizhao.blog.convert;
//
//
//import com.qinweizhao.blog.model.dto.AttachmentDTO;
//import com.qinweizhao.blog.model.entity.Attachment;
//import com.qinweizhao.blog.model.params.AttachmentParam;
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;
//import org.springframework.data.domain.Page;
//
///**
// * @author qinweizhao
// * @since 2022/5/27
// */
//@Mapper
//public interface AttachmentConvert {
//
//    AttachmentConvert INSTANCE = Mappers.getMapper(AttachmentConvert.class);
//
//    /**
//     * convertToDTO
//     *
//     * @param page page
//     * @return dto
//     */
////    Page<AttachmentDTO> convertToDTO(Page<Attachment> page);
//
//
//    /**
//     * convert
//     *
//     * @param attachment attachment
//     * @return AttachmentDTO
//     */
//    AttachmentDTO convert(Attachment attachment);
////    default AttachmentDTO convert(Attachment attachment){
////        Assert.notNull(attachment, "Attachment must not be null");
////
////        // Get blog base url
////        String blogBaseUrl = optionService.getBlogBaseUrl();
////
////        Boolean enabledAbsolutePath = optionService.isEnabledAbsolutePath();
////
////        // Convert to output dto
////        AttachmentDTO attachmentDTO = new AttachmentDTO().convertFrom(attachment);
////
////        if (Objects.equals(attachmentDTO.getType(), AttachmentType.LOCAL)) {
////            // Append blog base url to path and thumbnail
////            String fullPath = StringUtils.join(enabledAbsolutePath ? blogBaseUrl : "", "/", attachmentDTO.getPath());
////            String fullThumbPath = StringUtils.join(enabledAbsolutePath ? blogBaseUrl : "", "/", attachmentDTO.getThumbPath());
////
////            // Set full path and full thumb path
////            attachmentDTO.setPath(fullPath);
////            attachmentDTO.setThumbPath(fullThumbPath);
////        }
////
////        return attachmentDTO;
////    }
//
//    /**
//     * convert
//     *
//     * @param param param
//     * @return AttachmentDTO
//     */
//    Attachment convert(AttachmentParam param);
//
//
//}
