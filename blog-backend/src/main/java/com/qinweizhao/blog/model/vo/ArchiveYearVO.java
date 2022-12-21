package com.qinweizhao.blog.model.vo;

import com.qinweizhao.blog.model.dto.PostSimpleDTO;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * Year archive vo.
 *
 * @author qinweizhao
 * @since 2022-03-15
 */
@Data
public class ArchiveYearVO {

    private Integer year;

    private List<PostSimpleDTO> posts;

    public static class ArchiveComparator implements Comparator<ArchiveYearVO> {

        @Override
        public int compare(ArchiveYearVO left, ArchiveYearVO right) {
            return right.getYear() - left.getYear();
        }
    }
}
