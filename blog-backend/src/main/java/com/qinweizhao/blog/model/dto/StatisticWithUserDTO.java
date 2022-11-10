package com.qinweizhao.blog.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Statistic with user info DTO.
 *
 * @since 2019-12-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatisticWithUserDTO extends StatisticDTO {

    private UserDTO user;
}
