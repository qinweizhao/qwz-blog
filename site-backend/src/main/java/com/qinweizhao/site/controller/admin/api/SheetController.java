package com.qinweizhao.site.controller.admin.api;

import static org.springframework.data.domain.Sort.Direction.DESC;

import io.swagger.annotations.ApiOperation;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.qinweizhao.site.cache.AbstractStringCacheStore;
import com.qinweizhao.site.model.dto.IndependentSheetDTO;
import com.qinweizhao.site.model.dto.post.BasePostDetailDTO;
import com.qinweizhao.site.model.dto.post.BasePostMinimalDTO;
import com.qinweizhao.site.model.entity.Sheet;
import com.qinweizhao.site.model.enums.PostStatus;
import com.qinweizhao.site.model.params.PostContentParam;
import com.qinweizhao.site.model.params.SheetParam;
import com.qinweizhao.site.model.vo.SheetDetailVO;
import com.qinweizhao.site.model.vo.SheetListVO;
import com.qinweizhao.site.service.OptionService;
import com.qinweizhao.site.service.SheetService;
import com.qinweizhao.site.utils.HaloUtils;

/**
 * Sheet controller.
 *
 * @author johnniang
 * @author ryanwang
 * @date 19-4-24
 */
@RestController
@RequestMapping("/api/admin/sheets")
public class SheetController {

    private final SheetService sheetService;

    private final AbstractStringCacheStore cacheStore;

    private final OptionService optionService;

    public SheetController(SheetService sheetService,
        AbstractStringCacheStore cacheStore,
        OptionService optionService) {
        this.sheetService = sheetService;
        this.cacheStore = cacheStore;
        this.optionService = optionService;
    }

    @GetMapping("{sheetId:\\d+}")
    @ApiOperation("Gets a sheet")
    public SheetDetailVO getBy(@PathVariable("sheetId") Integer sheetId) {
        Sheet sheet = sheetService.getById(sheetId);
        return sheetService.convertToDetailVo(sheet);
    }

    @GetMapping
    @ApiOperation("Gets a page of sheet")
    public Page<SheetListVO> pageBy(
        @PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
        Page<Sheet> sheetPage = sheetService.pageBy(pageable);
        return sheetService.convertToListVo(sheetPage);
    }

    @GetMapping("independent")
    @ApiOperation("Lists independent sheets")
    public List<IndependentSheetDTO> independentSheets() {
        return sheetService.listIndependentSheets();
    }

    @PostMapping
    @ApiOperation("Creates a sheet")
    public SheetDetailVO createBy(@RequestBody @Valid SheetParam sheetParam,
        @RequestParam(value = "autoSave", required = false, defaultValue = "false")
            Boolean autoSave) {
        Sheet sheet =
            sheetService.createBy(sheetParam.convertTo(), sheetParam.getSheetMetas(), autoSave);
        return sheetService.convertToDetailVo(sheet);
    }

    @PutMapping("{sheetId:\\d+}")
    @ApiOperation("Updates a sheet")
    public SheetDetailVO updateBy(
        @PathVariable("sheetId") Integer sheetId,
        @RequestBody @Valid SheetParam sheetParam,
        @RequestParam(value = "autoSave", required = false, defaultValue = "false")
            Boolean autoSave) {
        Sheet sheetToUpdate = sheetService.getById(sheetId);

        sheetParam.update(sheetToUpdate);

        Sheet sheet = sheetService.updateBy(sheetToUpdate, sheetParam.getSheetMetas(), autoSave);

        return sheetService.convertToDetailVo(sheet);
    }

    @PutMapping("{sheetId:\\d+}/{status}")
    @ApiOperation("Updates a sheet")
    public void updateStatusBy(
        @PathVariable("sheetId") Integer sheetId,
        @PathVariable("status") PostStatus status) {
        Sheet sheet = sheetService.getById(sheetId);

        // Set status
        sheet.setStatus(status);

        // Update
        sheetService.update(sheet);
    }

    @PutMapping("{sheetId:\\d+}/status/draft/content")
    @ApiOperation("Updates draft")
    public BasePostDetailDTO updateDraftBy(
        @PathVariable("sheetId") Integer sheetId,
        @RequestBody PostContentParam contentParam) {
        // Update draft content
        Sheet sheet = sheetService.updateDraftContent(contentParam.getContent(), sheetId);

        return new BasePostDetailDTO().convertFrom(sheet);
    }

    @DeleteMapping("{sheetId:\\d+}")
    @ApiOperation("Deletes a sheet")
    public SheetDetailVO deleteBy(@PathVariable("sheetId") Integer sheetId) {
        Sheet sheet = sheetService.removeById(sheetId);
        return sheetService.convertToDetailVo(sheet);
    }

    @GetMapping("preview/{sheetId:\\d+}")
    @ApiOperation("Gets a sheet preview link")
    public String preview(@PathVariable("sheetId") Integer sheetId)
        throws UnsupportedEncodingException {
        Sheet sheet = sheetService.getById(sheetId);

        sheet.setSlug(URLEncoder.encode(sheet.getSlug(), StandardCharsets.UTF_8.name()));

        BasePostMinimalDTO sheetMinimalDTO = sheetService.convertToMinimal(sheet);

        String token = HaloUtils.simpleUUID();

        // cache preview token
        cacheStore.putAny(token, token, 10, TimeUnit.MINUTES);

        StringBuilder previewUrl = new StringBuilder();

        if (!optionService.isEnabledAbsolutePath()) {
            previewUrl.append(optionService.getBlogBaseUrl());
        }

        previewUrl.append(sheetMinimalDTO.getFullPath())
            .append("?token=")
            .append(token);

        // build preview post url and return
        return previewUrl.toString();
    }
}
