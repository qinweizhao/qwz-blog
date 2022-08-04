package com.qinweizhao.blog.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * User output dto.
 *
 * @author johnniang
 * @date 3/16/19
 */
@Data
public class UserDTO {

    private Integer id;

    private String username;

    private String nickname;

    private String email;

    private String avatar;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
