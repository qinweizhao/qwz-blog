package com.qinweizhao.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Statistic with user info DTO.
 *
 * @author qinweizhao
 * @since 2022-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatisticWithUserDTO extends StatisticDTO {

    private UserDTO user;
}
