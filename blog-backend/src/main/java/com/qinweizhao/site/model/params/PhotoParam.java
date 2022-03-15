package com.qinweizhao.site.model.params;

import lombok.Data;
import com.qinweizhao.site.model.dto.base.InputConverter;
import com.qinweizhao.site.model.entity.Photo;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Post param.
 *
 * @author ryanwang
 * @date 2019/04/25
 */
@Data
public class PhotoParam implements InputConverter<Photo> {

    @NotBlank(message = "照片名称不能为空")
    private String name;

    private String description;

    private Date takeTime;

    private String location;

    @NotBlank(message = "照片缩略图链接地址不能为空")
    private String thumbnail;

    @NotBlank(message = "照片链接地址不能为空")
    private String url;

    private String team;
}
