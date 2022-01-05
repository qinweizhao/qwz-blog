package com.qinweizhao.site.model.params;

import com.qinweizhao.site.model.dto.base.InputConverter;
import com.qinweizhao.site.model.entity.Journal;
import com.qinweizhao.site.model.enums.JournalType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Journal param.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-4-25
 */
@Data
public class JournalParam implements InputConverter<Journal> {

    @NotBlank(message = "内容不能为空")
    private String sourceContent;

    private JournalType type = JournalType.PUBLIC;
}
