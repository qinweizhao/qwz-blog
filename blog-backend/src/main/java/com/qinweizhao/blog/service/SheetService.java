package com.qinweizhao.blog.service;

import com.qinweizhao.blog.model.dto.IndependentSheetDTO;

import java.util.List;

/**
 * @author qinweizhao
 * @since 2022/8/12
 */
public interface SheetService {


    /**
     * 独立页面
     *
     * @return List
     */
    List<IndependentSheetDTO> listIndependentSheets();


}
