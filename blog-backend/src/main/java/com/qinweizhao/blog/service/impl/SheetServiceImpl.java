package com.qinweizhao.blog.service.impl;


import com.qinweizhao.blog.model.dto.IndependentSheetDTO;
import com.qinweizhao.blog.service.ConfigService;
import com.qinweizhao.blog.service.SheetService;
import com.qinweizhao.blog.service.ThemeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Sheet service implementation.
 *
 * @author johnniang
 * @author ryanwang
 * @author evanwang
 * @since 2019-04-24
 */
@Slf4j
@Service
@AllArgsConstructor
public class SheetServiceImpl implements SheetService {

    private final ThemeService themeService;

    private final ConfigService configService;

    @Override
    public List<IndependentSheetDTO> listIndependentSheets() {

        String context = (configService.isEnabledAbsolutePath() ? configService.getBlogBaseUrl() : "") + "/";
//
//        IndependentSheetDTO photoSheet = new IndependentSheetDTO();
//        photoSheet.setId(2);
//        photoSheet.setTitle("图库页面");
//        photoSheet.setFullPath(context + configService.getPhotosPrefix());
//        photoSheet.setRouteName("PhotoList");
//        photoSheet.setAvailable(themeService.templateExists("photos.ftl"));

        IndependentSheetDTO journalSheet = new IndependentSheetDTO();
        journalSheet.setId(3);
        journalSheet.setTitle("日志页面");
        journalSheet.setFullPath(context + configService.getJournalsPrefix());
        journalSheet.setRouteName("JournalList");
        journalSheet.setAvailable(themeService.templateExists("journals.ftl"));

//        return Arrays.asList(photoSheet, journalSheet);
        return Collections.singletonList(journalSheet);
    }

}
