package com.qinweizhao.site.model.params;

import lombok.Data;
import com.qinweizhao.site.model.dto.base.InputConverter;
import com.qinweizhao.site.model.entity.Journal;
import com.qinweizhao.site.model.enums.JournalType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(max = 511, message = "内容的字符长度不能超过 {max}")
    private String sourceContent;

    private JournalType type = JournalType.PUBLIC;
}
