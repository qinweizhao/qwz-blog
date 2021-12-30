package com.qinweizhao.site.model.vo;

import com.qinweizhao.site.model.dto.MenuDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * Menu team vo.
 *
 * @author ryanwang
 * @date 2019/8/28
 */
@Data
@ToString
public class MenuTeamVO {

    private String team;

    private List<MenuDTO> menus;
}
