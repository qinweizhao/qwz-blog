package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.PostListDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

/**
 * Year archive vo.
 *
 * @author johnniang
 * @author qinweizhao
 * @date 4/2/19
 */
@Data
public class ArchiveYearVO {

    private Integer year;

    private List<PostListDTO> posts;

    public static class ArchiveComparator implements Comparator<ArchiveYearVO> {

        @Override
        public int compare(ArchiveYearVO left, ArchiveYearVO right) {
            return right.getYear() - left.getYear();
        }
    }
}
