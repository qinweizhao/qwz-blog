package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.post.BasePostMinimalDTO;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostEditorType;
import com.qinweizhao.blog.model.enums.PostStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface PostConvert {

    PostConvert INSTANCE = Mappers.getMapper(PostConvert.class);


    /**
     * convert
     *
     * @param post post
     * @return Post
     */
    BasePostMinimalDTO convert(Post post);

    /**
     * 编辑器类型转换
     *
     * @param editorType editorType
     * @return PostEditorType
     */
    default PostEditorType editorTypeToEnum(Integer editorType) {
        if (editorType == null) {
            return null;
        }
        PostEditorType postEditorType;
        switch (editorType) {
            case 0:
                postEditorType = PostEditorType.MARKDOWN;
                break;
            case 1:
                postEditorType = PostEditorType.RICHTEXT;
                break;
            default:
                postEditorType = null;
        }
        return postEditorType;
    }


    /**
     * 状态转换
     *
     * @param status status
     * @return PostStatus
     */
    default PostStatus statusToEnum(Integer status) {
        if (status == null) {
            return null;
        }
        PostStatus postStatus;
        switch (status) {
            case 0:
                postStatus = PostStatus.PUBLISHED;
                break;
            case 1:
                postStatus = PostStatus.DRAFT;
                break;
            case 2:
                postStatus = PostStatus.RECYCLE;
                break;
            case 3:
                postStatus = PostStatus.INTIMATE;
                break;
            default:
                postStatus = null;
        }
        return postStatus;
    }


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
}