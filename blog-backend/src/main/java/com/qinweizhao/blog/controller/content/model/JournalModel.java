//package com.qinweizhao.blog.controller.content.model;
//
//import com.qinweizhao.blog.model.entity.Journal;
//import com.qinweizhao.blog.model.enums.JournalType;
//import com.qinweizhao.blog.model.properties.SheetProperties;
//import com.qinweizhao.blog.service.JournalService;
//import com.qinweizhao.blog.service.OptionService;
//import com.qinweizhao.blog.service.ThemeService;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//
//import javax.annotation.Resource;
//
//import static org.springframework.data.domain.Sort.Direction.DESC;
//
///**
// * @author ryanwang
// * @author qinweizhao
// * @date 2020-02-11
// */
//@Component
//public class JournalModel {
//
//    @Resource
//    private JournalService journalService;
//
//    @Resource
//    private OptionService optionService;
//
//    @Resource
//    private ThemeService themeService;
//
//
//    public String list(Integer page, Model model) {
//
//        int pageSize = optionService.getByPropertyOrDefault(SheetProperties.JOURNALS_PAGE_SIZE, Integer.class, Integer.parseInt(SheetProperties.JOURNALS_PAGE_SIZE.defaultValue()));
//
//        Pageable pageable = PageRequest.of(page >= 1 ? page - 1 : page, pageSize, Sort.by(DESC, "createTime"));
//
//        Page<Journal> journals = journalService.pageBy(JournalType.PUBLIC, pageable);
//
//        model.addAttribute("is_journals", true);
//        model.addAttribute("journals", journalService.convertToCmtCountDto(journals));
//        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        model.addAttribute("meta_description", optionService.getSeoDescription());
//        return themeService.render("journals");
//    }
//}
