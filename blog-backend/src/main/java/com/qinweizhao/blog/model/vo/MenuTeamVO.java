package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.MenuDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Menu team vo.
 *
 * @since 2019/8/28
 */
@Data
@ToString
public class MenuTeamVO {

    private String team;

    private List<MenuDTO> menus;
}
