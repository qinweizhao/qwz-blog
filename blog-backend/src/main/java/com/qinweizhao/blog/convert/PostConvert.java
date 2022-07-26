package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.post.PostDetailDTO;
import com.qinweizhao.blog.model.dto.post.PostMinimalDTO;
import com.qinweizhao.blog.model.dto.post.PostSimpleDTO;
import com.qinweizhao.blog.model.entity.Post;
import com.qinweizhao.blog.model.enums.PostEditorType;
import com.qinweizhao.blog.model.enums.PostStatus;
import com.qinweizhao.blog.model.vo.PostDetailVO;
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
    PostMinimalDTO convert(Post post);

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


    /**
     * convertToSimleDTO
     *
     * @param pageResult pageResult
     * @return PageResult
     */
    PageResult<PostSimpleDTO> convertToSimpleDTO(PageResult<Post> pageResult);

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
     * convertToListVO
     *
     * @param post post
     * @return PostListVO
     */
    PostListVO convertToListVO(PostSimpleDTO post);

    /**
     * convertToMinimal
     *
     * @param content content
     * @return List
     */
    List<PostMinimalDTO> convertToMinimal(List<PostSimpleDTO> content);


    /**
     * convertDTO
     *
     * @param post post
     * @return PostDetailDTO
     */
    PostDetailDTO convertDTO(Post post);

    /**
     * convertVO
     *
     * @param post post
     * @return PostDetailVO
     */
    PostDetailVO convertVO(PostDetailDTO post);

    /**
     * convertVO
     *
     * @param post post
     * @return PostDetailVO
     */
    PostDetailVO convertVO(PostMinimalDTO post);

    /**
     * convertSimpleDTO
     * @param post post
     * @return PostSimpleDTO
     */
    PostSimpleDTO convertSimpleDTO(Post post);

    /**
     * convertToSimpleDTO
     * @param posts posts
     * @return List
     */
    List<PostSimpleDTO> convertToSimpleDTO(List<Post> posts);
}
