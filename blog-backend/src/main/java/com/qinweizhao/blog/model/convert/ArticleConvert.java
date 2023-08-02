package com.qinweizhao.blog.model.convert;


import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ArticleDTO;
import com.qinweizhao.blog.model.dto.ArticleListDTO;
import com.qinweizhao.blog.model.dto.ArticleSimpleDTO;
import com.qinweizhao.blog.model.entity.Article;
import com.qinweizhao.blog.model.enums.ArticleStatus;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.param.ArticleParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface ArticleConvert {

    ArticleConvert INSTANCE = Mappers.getMapper(ArticleConvert.class);


    /**
     * convert
     *
     * @param post post
     * @return Post
     */
    ArticleSimpleDTO convert(ArticleDTO post);

    /**
     * convert
     *
     * @param param param
     * @return Post
     */
    Article convert(ArticleParam param);

    /**
     * convert
     *
     * @param article post
     * @return Post
     */
    ArticleDTO convert(Article article);

    /**
     * convertToListVO
     *
     * @param post post
     * @return PostListVO
     */
    ArticleListDTO convertToListDTO(ArticleSimpleDTO post);

    /**
     * convertToSimpleDTO
     *
     * @param articles posts
     * @return ListHibernate
     */
    List<ArticleSimpleDTO> convertToSimpleDTO(List<Article> articles);

    /**
     * convertSimpleDTO
     *
     * @param article post
     * @return PostSimpleDTO
     */
    ArticleSimpleDTO convertSimpleDTO(Article article);

    /**
     * statusToInteger
     *
     * @param status status
     * @return Integer
     */
    default Integer statusToInteger(ArticleStatus status) {
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
    default ArticleStatus statusToEnum(Integer status) {
        return ValueEnum.valueToEnum(ArticleStatus.class, status);
    }


    /**
     * convertToListVO
     *
     * @param postList postList
     * @return List
     */
    List<ArticleListDTO> convertToListDTO(List<ArticleSimpleDTO> postList);


    /**
     * convertDTO
     *
     * @param postPage postPage
     * @return PageResult
     */
    PageResult<ArticleDTO> convertDTO(PageResult<ArticleSimpleDTO> postPage);

}
