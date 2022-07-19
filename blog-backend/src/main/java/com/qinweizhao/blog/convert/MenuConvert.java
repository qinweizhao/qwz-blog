package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.MenuDTO;
import com.qinweizhao.blog.model.entity.Menu;
import com.qinweizhao.blog.model.params.MenuParam;
import com.qinweizhao.blog.model.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface MenuConvert {

    MenuConvert INSTANCE = Mappers.getMapper(MenuConvert.class);

    /**
     * convert
     *
     * @param menu menu
     * @return dto
     */
    MenuDTO convert(Menu menu);


    /**
     * convertToDTO
     *
     * @param list list
     * @return List
     */
    List<MenuDTO> convertToDTO(List<Menu> list);

    /**
     * convertToDO
     *
     * @param menuParams menuParams
     * @return List
     */
    List<Menu> convertToDO(List<MenuParam> menuParams);

    /**
     * convert
     *
     * @param menuParam menuParam
     * @return List
     */
    Menu convert(MenuParam menuParam);


    /**
     * convertToVO
     *
     * @param list list
     * @return List
     */
    List<MenuVO> convertToVO(List<Menu> list);

    /**
     * convertVO
     * @param menu menu
     * @return MenuVO
     */
    MenuVO convertVO(Menu menu);
}
