package com.qinweizhao.blog.convert;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qinweizhao.blog.model.base.PageResult;
import com.qinweizhao.blog.model.dto.PhotoDTO;
import com.qinweizhao.blog.model.entity.Photo;
import com.qinweizhao.blog.model.params.PhotoParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface PhotoConvert {

    PhotoConvert INSTANCE = Mappers.getMapper(PhotoConvert.class);


    /**
     * convert
     *
     * @param menuParam menuParam
     * @return Photo
     */
    Photo convert(PhotoParam menuParam);


    /**
     * convertToDTO
     *
     * @param photos photos
     * @return List
     */
    List<PhotoDTO> convertToDTO(List<Photo> photos);


    /**
     * convert
     *
     * @param photo photo
     * @return Photo
     */
    PhotoDTO convert(Photo photo);

    /**
     * convert
     *
     * @param photo photo
     * @return PageResult
     */
    PageResult<PhotoDTO> convert(PageResult<Photo> photo);


}
