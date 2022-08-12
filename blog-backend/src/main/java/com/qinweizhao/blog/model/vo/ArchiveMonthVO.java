package com.qinweizhao.blog.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;

/**
 * Month archive vo.
 *
 * @author johnniang
 * @author qinweizhao
 * @since 4/2/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArchiveMonthVO extends ArchiveYearVO {

    private Integer month;

    public static class ArchiveComparator implements Comparator<ArchiveMonthVO> {

        @Override
        public int compare(ArchiveMonthVO left, ArchiveMonthVO right) {
            int compare = right.getYear() - left.getYear();

            if (compare != 0) {
                return compare;
            }

            return right.getMonth() - left.getMonth();
        }
    }
}
