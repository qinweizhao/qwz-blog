package com.qinweizhao.blog.model.vo;

import lombok.Data;
import lombok.ToString;
import com.qinweizhao.blog.model.dto.PhotoDTO;

import java.util.List;

/**
 * Link team vo.
 *
 * @author ryanwang
 * @date 2019/3/22
 */
@Data
@ToString
public class PhotoTeamVO {

    private String team;

    private List<PhotoDTO> photos;
}
