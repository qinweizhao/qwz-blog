package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigSimpleDTO;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Option Controller.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-03-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/options")
public class ConfigController {

    private final ConfigService configService;

//    @GetMapping
//    @ApiOperation("Lists options")
//    public List<OptionDTO> listAll() {
//        return optionService.listDtos();
//    }
//
//    @PostMapping("saving")
//    @ApiOperation("Saves options")
//    @DisableOnCondition
//    public void saveOptions(@Valid @RequestBody List<OptionParam> optionParams) {
//        optionService.save(optionParams);
//    }

    @GetMapping("map_view")
    public Map<String, Object> listAllWithMapView() {
        return configService.listOptions();
    }

    /**
     * Lists options with map view by keys
     *
     * @param keys keys
     * @return Map
     */
    @PostMapping("map_view/keys")
    public Map<String, Object> listAllWithMapView(@RequestBody List<String> keys) {
        return configService.listOptions(keys);
    }

    /**
     * 开发者选项->系统变量
     *
     * @param configQueryParam optionQuery
     * @return PageResult
     */
    @GetMapping("list_view")
    public PageResult<ConfigSimpleDTO> listAllWithListView(ConfigQueryParam configQueryParam) {
        return configService.pageSimple(configQueryParam);
    }
////
////    @GetMapping("{id:\\d+}")
////    @ApiOperation("Gets option detail by id")
////    public OptionSimpleDTO getBy(@PathVariable("id") Integer id) {
////        Option option = optionService.getById(id);
////        return optionService.convertToDto(option);
////    }
//
//    @PostMapping
//    @ApiOperation("Creates option")
//    @DisableOnCondition
//    public void createBy(@RequestBody @Valid OptionParam optionParam) {
//        optionService.save(optionParam);
//    }
//
//    @PutMapping("{optionId:\\d+}")
//    @ApiOperation("Updates option")
//    @DisableOnCondition
//    public void updateBy(@PathVariable("optionId") Integer optionId,
//                         @RequestBody @Valid OptionParam optionParam) {
//        optionService.update(optionId, optionParam);
//    }
//
//    @DeleteMapping("{optionId:\\d+}")
//    @ApiOperation("Deletes option")
//    @DisableOnCondition
//    public void deletePermanently(@PathVariable("optionId") Integer optionId) {
//        optionService.removePermanently(optionId);
//    }
//
//    @PostMapping("map_view/saving")
//    @ApiOperation("Saves options by option map")
//    @DisableOnCondition
//    public void saveOptionsWithMapView(@RequestBody Map<String, Object> optionMap) {
//        optionService.save(optionMap);
//    }

}
