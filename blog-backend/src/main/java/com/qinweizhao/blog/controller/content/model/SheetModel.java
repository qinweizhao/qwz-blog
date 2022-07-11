package com.qinweizhao.blog.controller.content.model;

import com.qinweizhao.blog.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.SheetMetaService;
import com.qinweizhao.blog.service.SheetService;
import com.qinweizhao.blog.service.ThemeService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Sheet model.
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2020-01-07
 */
@Component
public class SheetModel {

    @Resource
    private SheetService sheetService;

    @Resource
    private SheetMetaService sheetMetaService;

    @Resource
    private AbstractStringCacheStore cacheStore;

    @Resource
    private ThemeService themeService;

    @Resource
    private OptionService optionService;

    /**
     * Sheet content.
     *
     * @param sheet sheet
     * @param token token
     * @param model model
     * @return template name
     */
//    public String content(Sheet sheet, String token, Model model) {
//
//        if (StringUtils.isEmpty(token)) {
//            sheet = sheetService.getBy(PostStatus.PUBLISHED, sheet.getSlug());
//        } else {
//            // verify token
//            String cachedToken = cacheStore.getAny(token, String.class).orElseThrow(() -> new ForbiddenException("您没有该页面的访问权限"));
//            if (!cachedToken.equals(token)) {
//                throw new ForbiddenException("您没有该页面的访问权限");
//            }
//            // render markdown to html when preview sheet
//            if (sheet.getEditorType().equals(PostEditorType.MARKDOWN)) {
//                sheet.setFormatContent(MarkdownUtils.renderHtml(sheet.getOriginalContent()));
//            } else {
//                sheet.setFormatContent(sheet.getOriginalContent());
//            }
//        }
//
//        sheetService.publishVisitEvent(sheet.getId());
//
//        SheetDetailVO sheetDetailVO = sheetService.convertToDetailVo(sheet);
//
//        List<SheetMeta> metas = sheetMetaService.listBy(sheet.getId());
//
//        // Generate meta keywords.
//        if (StringUtils.isNotEmpty(sheet.getMetaKeywords())) {
//            model.addAttribute("meta_keywords", sheet.getMetaKeywords());
//        } else {
//            model.addAttribute("meta_keywords", optionService.getSeoKeywords());
//        }
//
//        // Generate meta description.
//        if (StringUtils.isNotEmpty(sheet.getMetaDescription())) {
//            model.addAttribute("meta_description", sheet.getMetaDescription());
//        } else {
//            model.addAttribute("meta_description", sheetService.generateDescription(sheet.getFormatContent()));
//        }
//
//        // sheet and post all can use
//        model.addAttribute("sheet", sheetDetailVO);
//        model.addAttribute("post", sheetDetailVO);
//        model.addAttribute("is_sheet", true);
//        model.addAttribute("metas", sheetMetaService.convertToMap(metas));
//
//        if (themeService.templateExists(ThemeService.CUSTOM_SHEET_PREFIX + sheet.getTemplate() + HaloConst.SUFFIX_FTL)) {
//            return themeService.render(ThemeService.CUSTOM_SHEET_PREFIX + sheet.getTemplate());
//        }
//        return themeService.render("sheet");
//    }
}
