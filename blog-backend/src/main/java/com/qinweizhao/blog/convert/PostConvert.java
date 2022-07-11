//package com.qinweizhao.blog.convert;
//
//
//import com.qinweizhao.blog.model.dto.post.BasePostSimpleDTO;
//import com.qinweizhao.blog.model.entity.Post;
//import com.qinweizhao.blog.model.params.PostParam;
//import com.qinweizhao.blog.model.vo.PostListVO;
//import org.mapstruct.Mapper;
//import org.mapstruct.factory.Mappers;
//import org.springframework.data.domain.Page;
//
///**
// * @author qinweizhao
// * @since 2022/5/27
// */
//@Mapper
//public interface PostConvert {
//
//    PostConvert INSTANCE = Mappers.getMapper(PostConvert.class);
//
//
//    /**
//     * convert
//     *
//     * @param menuParam menuParam
//     * @return Post
//     */
////    Post convert(PostParam menuParam);
//
//
//    /**
//     * convertToVO
//     *
//     * @param postPage postPage
//     * @return Post
//     */
//    Page<PostListVO> convertToVO(Page<Post> postPage);
//
//
//    /**
//     * convertToDTO
//     *
//     * @param postPage postPage
//     * @return Post
//     */
//    Page<BasePostSimpleDTO> convertToDTO(Page<Post> postPage);
//}
