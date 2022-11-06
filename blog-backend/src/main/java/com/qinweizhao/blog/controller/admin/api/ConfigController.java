package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.param.ConfigParam;
import com.qinweizhao.blog.model.param.ConfigQueryParam;
import com.qinweizhao.blog.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
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
@RequestMapping("/api/admin/config")
public class ConfigController {

    private final ConfigService configService;

    /**
     * 配置
     *
     * @return Map
     */
    @GetMapping("map")
    public Map<String, Object> map(@RequestParam(required = false) List<String> keys) {
        if (ObjectUtils.isEmpty(keys)){
            return configService.getMap();
        }
        return configService.listOptions(keys);
    }

    /**
     * 保存
     *
     * @param configMap optionMap
     */
    @PostMapping("map")
    @DisableOnCondition
    public void saveOptionsWithMapView(@RequestBody Map<String, Object> configMap) {
        configService.save(configMap);
    }

    /**
     * 开发者选项->系统变量
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping("page")
    public PageResult<ConfigDTO> listAllWithListView(ConfigQueryParam param) {
        return configService.pageSimple(param);
    }

    /**
     * 开发者选项->系统变量
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
    public Boolean update(@PathVariable("configId") Integer configId, @RequestBody @Valid ConfigParam param) {
        param.setId(configId);
        return configService.updateById(param);
    }

    /**
     * 删除
     *
     * @param configId configId
     * @return Boolean
     */
    @DeleteMapping("{configId:\\d+}")
    @DisableOnCondition
    public Boolean remove(@PathVariable("configId") Integer configId) {
        return configService.removeById(configId);
    }

}
