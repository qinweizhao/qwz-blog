package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.enums.JournalType;
import com.qinweizhao.blog.model.param.JournalQueryParam;
import com.qinweizhao.blog.model.properties.SheetProperties;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.JournalService;
import com.qinweizhao.blog.service.ThemeService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;


/**
 * @author ryanwang
 * @author qinweizhao
 * @since 2020-02-11
 */
@Component
public class JournalModel {

    @Resource
    private JournalService journalService;

    @Resource
    private ConfigService configService;

    @Resource
    private ThemeService themeService;


    public String list(Integer page, Model model) {

        int pageSize = configService.getByPropertyOrDefault(SheetProperties.JOURNALS_PAGE_SIZE, Integer.class, Integer.parseInt(SheetProperties.JOURNALS_PAGE_SIZE.defaultValue()));

        JournalQueryParam param = new JournalQueryParam();
        param.setPage(page);
        param.setSize(pageSize);
        param.setType(JournalType.PUBLIC);

        PageResult<JournalDTO> journals = journalService.page(param);

        model.addAttribute("is_journals", true);
        model.addAttribute("journals", journals);
        model.addAttribute("meta_keywords", configService.getSeoKeywords());
        model.addAttribute("meta_description", configService.getSeoDescription());
        return themeService.render("journals");
    }
}
