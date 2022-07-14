package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.params.CategoryParam;
import com.qinweizhao.blog.model.vo.CategoryVO;
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
     * @param param param
     * @return Category
     */
    Category convert(CategoryParam param);

    /**
     * convert
     *
     * @param category category
     * @return CategoryVO
     */
    CategoryVO convertVO(Category category);

}
