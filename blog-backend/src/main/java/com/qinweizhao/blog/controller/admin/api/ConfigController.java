package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigSimpleDTO;
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
 * @author qinweizhao
 * @since 2019-03-20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/options")
public class ConfigController {

    private final ConfigService configService;

    /**
     * 配置
     *
     * @return Map
     */
    @GetMapping("map_view")
    public Map<String, Object> listAsMapView() {
        return configService.listOptions();
    }

    /**
     * 配置
     *
     * @param keys keys
     * @return Map
     */
    @PostMapping("map_view/keys")
    public Map<String, Object> listAsMapView(@RequestBody List<String> keys) {
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

    /**
     * 新增
     *
     * @param param param
     */
    @PostMapping
    @DisableOnCondition
    public Boolean save(@RequestBody @Valid ConfigParam param) {
        return configService.save(param);
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
        param.setId(configId);
        return configService.updateById(param);
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


    /**
     * 保存
     *
     * @param configMap optionMap
     */
    @PostMapping("map_view/saving")
    @DisableOnCondition
    public void saveOptionsWithMapView(@RequestBody Map<String, Object> configMap) {
        configService.save(configMap);
    }

}
