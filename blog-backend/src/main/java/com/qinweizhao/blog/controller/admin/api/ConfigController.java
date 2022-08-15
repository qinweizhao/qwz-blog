package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.model.convert.ConfigConvert;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigSimpleDTO;
import com.qinweizhao.blog.model.entity.Config;
import com.qinweizhao.blog.model.param.ConfigParam;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
//    public List<OptionDTO> listAll() {
//        return optionService.listDtos();
//    }
//
//    @PostMapping("saving")
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

    /**
     * 新增
     *
     * @param param param
     */
    @PostMapping
    @DisableOnCondition
    public Boolean create(@RequestBody @Valid ConfigParam param) {
        Config config = ConfigConvert.INSTANCE.convert(param);
        return configService.save(config);
    }

    /**
     * 更新
     *
     * @param configId configId
     * @param param    param
     * @return Boolean
     */
    @PutMapping("{configId:\\d+}")
    @DisableOnCondition
    public Boolean updateBy(@PathVariable("configId") Integer configId,
                            @RequestBody @Valid ConfigParam param) {
        Config config = ConfigConvert.INSTANCE.convert(param);
        config.setId(configId);
        return configService.updateById(config);
    }


    /**
     * 删除
     *
     * @param optionId optionId
     * @return Boolean
     */
    @DeleteMapping("{optionId:\\d+}")
    @DisableOnCondition
    public Boolean deletePermanently(@PathVariable("optionId") Integer optionId) {
        return configService.removeById(optionId);
    }

//
//    @PostMapping("map_view/saving")
//    @ApiOperation("Saves options by option map")
//    @DisableOnCondition
//    public void saveOptionsWithMapView(@RequestBody Map<String, Object> optionMap) {
//        optionService.save(optionMap);
//    }

}
