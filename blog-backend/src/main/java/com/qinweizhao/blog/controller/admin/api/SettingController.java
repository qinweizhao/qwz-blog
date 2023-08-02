package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.framework.annotation.DisableOnCondition;
import com.qinweizhao.blog.framework.handler.theme.config.support.Group;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.ConfigDTO;
import com.qinweizhao.blog.model.param.SettingParam;
import com.qinweizhao.blog.model.param.SettingQueryParam;
import com.qinweizhao.blog.service.SettingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 主题管理
 *
 * @author qinweizhao
 * @since 2022/7/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/setting")
public class SettingController {

    private final SettingService settingService;

    /**
     * 配置（yaml)
     *
     * @return List
     */
    @GetMapping("configurations")
    public List<Group> listConfigurations() {
        return settingService.listConfig();
    }


    /**
     * 配置(db)
     *
     * @return Map
     */
    @GetMapping("/map")
    public Map<String, Object> map(@RequestParam(required = false) List<String> keys) {
        return settingService.getMap();
    }


    /**
     * 保存
     *
     * @param configs configs
     */
    @PostMapping("/map")
    @DisableOnCondition
    public void map(@RequestBody Map<String, Object> configs) {
        settingService.save(configs);
    }


    /**
     * 开发者选项->系统变量
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping("page")
    public PageResult<ConfigDTO> page(SettingQueryParam param) {
        return settingService.pageSimple(param);
    }

    /**
     * 开发者选项->系统变量
     *
     * @param param param
     */
    @PostMapping
    @DisableOnCondition
    public Boolean save(@RequestBody @Valid SettingParam param) {
        return settingService.save(param);
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
    public Boolean update(@PathVariable("configId") Integer configId, @RequestBody @Valid SettingParam param) {
        param.setId(configId);
        return settingService.updateById(param);
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
        return settingService.removeById(configId);
    }


}
