package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.ThemeService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.annotation.Resource;

/**
 * @author ryanwang
 * @date 2020-03-04
 */
@Component
public class LinkModel {

    @Resource
    private ThemeService themeService;

    @Resource
    private OptionService optionService;


    public String list(Model model) {
        model.addAttribute("is_links", true);
        model.addAttribute("meta_keywords", optionService.getSeoKeywords());
        model.addAttribute("meta_description", optionService.getSeoDescription());
        return themeService.render("links");
    }
}
