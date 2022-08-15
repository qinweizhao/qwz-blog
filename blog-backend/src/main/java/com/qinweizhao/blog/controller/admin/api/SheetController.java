package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.dto.IndependentSheetDTO;
import com.qinweizhao.blog.service.SheetService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Sheet controller.
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @since 19-4-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/sheets")
public class SheetController {

    private final SheetService sheetService;

    /**
     * 独立页面
     *
     * @return List
     */
    @GetMapping("independent")
    public List<IndependentSheetDTO> independentSheets() {
        return sheetService.listIndependentSheets();
    }


}
