package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.model.dto.IndependentSheetDTO;
import com.qinweizhao.blog.service.OptionService;
import com.qinweizhao.blog.service.SheetService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Sheet controller.
 *
 * @author johnniang
 * @author ryanwang
 * @date 19-4-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/sheets")
public class SheetController {

    private final SheetService sheetService;

    private final AbstractStringCacheStore cacheStore;

    private final OptionService optionService;


    /**
     * 独立页面
     * @return List
     */
    @GetMapping("independent")
    public List<IndependentSheetDTO> independentSheets() {
        return sheetService.listIndependentSheets();
    }


//    /**
//     * 详情
//     * @param sheetId sheetId
//     * @return
//     */
//    @GetMapping("{sheetId:\\d+}")
//    public SheetDetailVO getBy(@PathVariable("sheetId") Integer sheetId) {
//        Sheet sheet = sheetService.getById(sheetId);
//        return sheetService.convertToDetailVo(sheet);
//    }
//
//    @GetMapping
//    @ApiOperation("Gets a page of sheet")
//    public Page<SheetListVO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
//        Page<Sheet> sheetPage = sheetService.pageBy(pageable);
//        return sheetService.convertToListVo(sheetPage);
//    }
//

//
//    @PostMapping
//    @ApiOperation("Creates a sheet")
//    public SheetDetailVO createBy(@RequestBody @Valid SheetParam sheetParam,
//                                  @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
//        Sheet sheet = sheetService.createBy(sheetParam.convertTo(), sheetParam.getSheetMetas(), autoSave);
//        return sheetService.convertToDetailVo(sheet);
//    }
//
//    @PutMapping("{sheetId:\\d+}")
//    @ApiOperation("Updates a sheet")
//    public SheetDetailVO updateBy(
//            @PathVariable("sheetId") Integer sheetId,
//            @RequestBody @Valid SheetParam sheetParam,
//            @RequestParam(value = "autoSave", required = false, defaultValue = "false") Boolean autoSave) {
//        Sheet sheetToUpdate = sheetService.getById(sheetId);
//
//        sheetParam.update(sheetToUpdate);
//
//        Sheet sheet = sheetService.updateBy(sheetToUpdate, sheetParam.getSheetMetas(), autoSave);
//
//        return sheetService.convertToDetailVo(sheet);
//    }
//
//    @PutMapping("{sheetId:\\d+}/{status}")
//    @ApiOperation("Updates a sheet")
//    public void updateStatusBy(
//            @PathVariable("sheetId") Integer sheetId,
//            @PathVariable("status") PostStatus status) {
//        Sheet sheet = sheetService.getById(sheetId);
//
//        // Set status
//        sheet.setStatus(status);
//
//        // Update
//        sheetService.update(sheet);
//    }
//
//    @PutMapping("{sheetId:\\d+}/status/draft/content")
//    @ApiOperation("Updates draft")
//    public BasePostDetailDTO updateDraftBy(
//            @PathVariable("sheetId") Integer sheetId,
//            @RequestBody PostContentParam contentParam) {
//        // Update draft content
//        Sheet sheet = sheetService.updateDraftContent(contentParam.getContent(), sheetId);
//
//        return new BasePostDetailDTO().convertFrom(sheet);
//    }
//
//    @DeleteMapping("{sheetId:\\d+}")
//    @ApiOperation("Deletes a sheet")
//    public SheetDetailVO deleteBy(@PathVariable("sheetId") Integer sheetId) {
//        Sheet sheet = sheetService.removeById(sheetId);
//        return sheetService.convertToDetailVo(sheet);
//    }
//
//    @GetMapping("preview/{sheetId:\\d+}")
//    @ApiOperation("Gets a sheet preview link")
//    public String preview(@PathVariable("sheetId") Integer sheetId) throws UnsupportedEncodingException {
//        Sheet sheet = sheetService.getById(sheetId);
//
//        sheet.setSlug(URLEncoder.encode(sheet.getSlug(), StandardCharsets.UTF_8.name()));
//
//        BasePostMinimalDTO sheetMinimalDTO = sheetService.convertToMinimal(sheet);
//
//        String token = IdUtil.simpleUUID();
//
//        // cache preview token
//        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);
//
//        StringBuilder previewUrl = new StringBuilder();
//
//        if (!optionService.isEnabledAbsolutePath()) {
//            previewUrl.append(optionService.getBlogBaseUrl());
//        }
//
//        previewUrl.append(sheetMinimalDTO.getFullPath())
//                .append("?token=")
//                .append(token);
//
//        // build preview post url and return
//        return previewUrl.toString();
//    }
}
