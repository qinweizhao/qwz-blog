package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.CategoryDTO;
import com.qinweizhao.blog.model.entity.Category;
import com.qinweizhao.blog.model.params.CategoryParam;
import com.qinweizhao.blog.model.vo.CategoryVO;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.List;

import static com.qinweizhao.blog.model.support.HaloConst.URL_SEPARATOR;

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
     * @param page page
     * @return dto
     */
    Page<CategoryDTO> convertToDTO(Page<Category> page);

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
     * @return CategoryDTO
     */
    CategoryDTO convert(Category category);

    /**
     * convert
     *
     * @param category category
     * @return CategoryVO
     */
    CategoryVO convertVO(Category category);


    /**
     * Converts to category dto.
     *
     * @param category category must not be null
     * @return category dto
     */
     CategoryDTO convertTo(Category category);


    /**
     * Converts to category dto list.
     *
     * @param categories category list
     * @return a list of category dto
     */
    List<CategoryDTO> convertTo(List<Category> categories);
}
