package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PostDTO;
import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostEditorType;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.vo.PostVO;
import com.qinweizhao.blog.model.vo.PostListVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
    PostSimpleDTO convert(PostDTO post);

    /**
     * convert
     *
     * @param post post
     * @return Post
     */
    PostSimpleDTO convert(Post post);

    /**
     * convert
     *
     * @param post post
     * @return Post
     */
    PostDTO convertDetail(Post post);

    /**
     * convertToSimpleDTO
     *
     * @param pageResult pageResult
     * @return PageResult
     */
    PageResult<PostSimpleDTO> convertToSimpleDTO(PageResult<Post> pageResult);


    /**
     * convertToListVO
     *
     * @param post post
     * @return PostListVO
     */
    PostListVO convertToListVO(PostSimpleDTO post);

    /**
     * convertVO
     *
     * @param post post
     * @return PostDetailVO
     */
    PostVO convertVO(PostDTO post);

    /**
     * convertToSimpleDTO
     *
     * @param posts posts
     * @return List
     */
    List<PostSimpleDTO> convertToSimpleDTO(List<Post> posts);

    /**
     * statusToInteger
     *
     * @param status status
     * @return Integer
     */
    default Integer statusToInteger(PostStatus status) {
        if (status == null) {
            return null;
        }
        Integer postStatus;
        switch (status) {
            case PUBLISHED:
                postStatus = 0;
                break;
            case DRAFT:
                postStatus = 1;
                break;
            case RECYCLE:
                postStatus = 2;
                break;
            case INTIMATE:
                postStatus = 3;
                break;
            default:
                postStatus = null;
        }
        return postStatus;
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
}
