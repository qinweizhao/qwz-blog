package com.qinweizhao.blog.controller.admin;

import com.qinweizhao.blog.service.OptionService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Option Controller.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/options")
public class OptionController {

    private final OptionService optionService;

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
    @ApiOperation("Lists all options with map view")
    public Map<String, Object> listAllWithMapView() {
        return optionService.listOptions();
    }

    /**
     * Lists options with map view by keys
     *
     * @param keys keys
     * @return Map
     */
    @PostMapping("map_view/keys")
    public Map<String, Object> listAllWithMapView(@RequestBody List<String> keys) {
        return optionService.listOptions(keys);
    }

//    @GetMapping("list_view")
//    @ApiOperation("Lists all options with list view")
//    public Page<OptionSimpleDTO> listAllWithListView(@PageableDefault(sort = "updateTime", direction = DESC) Pageable pageable,
//                                                     OptionQuery optionQuery) {
//        return optionService.pageDtosBy(pageable, optionQuery);
//    }
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
