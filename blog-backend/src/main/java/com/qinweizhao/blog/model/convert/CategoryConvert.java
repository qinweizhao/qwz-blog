package com.qinweizhao.blog.model.convert;


import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.dto.CategoryWithPostCountDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.param.CategoryParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface CategoryConvert {


    CategoryConvert INSTANCE = Mappers.getMapper(CategoryConvert.class);

    /**
     * convertToDTO
     *
     * @param list list
     * @return dto
     */
    List<CategoryDTO> convertToDTO(List<Category> list);

    /**
     * convert
     *
     * @param category category
     * @return CategoryDTO
     */
    CategoryDTO convert(Category category);

    /**
     * convert
     *
     * @param category category
     * @return Category
     */
    CategoryWithPostCountDTO convertPostCountDTO(Category category);

    /**
     * convert
     *
     * @param param param
     * @return Category
     */
    Category convert(CategoryParam param);
}
