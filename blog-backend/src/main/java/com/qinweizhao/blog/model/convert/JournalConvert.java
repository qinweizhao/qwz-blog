package com.qinweizhao.blog.model.convert;


import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.entity.Journal;
import com.qinweizhao.blog.model.enums.JournalType;
import com.qinweizhao.blog.model.enums.ValueEnum;
import com.qinweizhao.blog.model.param.JournalParam;
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
     * @param page page
     * @return PageResult
     */
    PageResult<JournalDTO> convert(PageResult<Journal> page);


    /**
     * 状态转换
     *
     * @param type type
     * @return JournalType
     */
    default JournalType statusToEnum(Integer type) {
        return ValueEnum.valueToEnum(JournalType.class, type);
    }


    /**
     * convert
     *
     * @param param param
     * @return Journal
     */
    default Journal convert(JournalParam param) {

        if (param == null) {
            return null;
        }

        Integer type = param.getType().getValue();
        String sourceContent = param.getSourceContent();

        Journal journal = new Journal();
        journal.setSourceContent(sourceContent);
        journal.setType(type);
        return journal;
    }

    /**
     * convert
     *
     * @param journal journal
     * @return JournalDTO
     */
    JournalDTO convert(Journal journal);
}
