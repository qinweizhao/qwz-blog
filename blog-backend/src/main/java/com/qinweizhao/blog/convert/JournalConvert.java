package com.qinweizhao.blog.convert;


import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.enums.JournalType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qinweizhao
 * @since 2022/5/27
 */
@Mapper
public interface JournalConvert {

    JournalConvert INSTANCE = Mappers.getMapper(JournalConvert.class);

    /**
     * convert
     *
     * @param journal journal
     * @return JournalDTO
     */
    JournalDTO convert(Journal journal);

    /**
     * 状态转换
     *
     * @param type type
     * @return CommentStatus
     */
    default JournalType typeToEnum(Integer type) {
        if (type == null) {
            return null;
        }
        JournalType journalType;
        switch (type) {
            case 0:
                journalType = JournalType.INTIMATE;
                break;
            case 1:
                journalType = JournalType.PUBLIC;
                break;
            default:
                journalType = null;
        }
        return journalType;
    }

}
