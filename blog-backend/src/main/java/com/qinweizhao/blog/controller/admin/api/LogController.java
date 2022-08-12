package com.qinweizhao.blog.controller.admin.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.LogDTO;
import com.qinweizhao.blog.model.param.LogQueryParam;
import com.qinweizhao.blog.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Log controller.
 *
 * @author johnniang
 * @author qinweizhao
 * @since 2019-03-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/logs")
public class LogController {

    private final LogService logService;

    /**
     * 分页
     *
     * @param param param
     * @return PageResult
     */
    @GetMapping
    public PageResult<LogDTO> page(LogQueryParam param) {
        return logService.pageLogs(param);
    }

    /**
     * 最新数据
     *
     * @param top top
     * @return List
     */
    @GetMapping("latest")
    public List<LogDTO> pageLatest(@RequestParam(name = "top", defaultValue = "10") int top) {
        LogQueryParam param = new LogQueryParam();
        param.setSize(top);
        PageResult<LogDTO> result = logService.pageLogs(param);
        return result.getContent();
    }

    /**
     * 清除所有日志
     */
    @GetMapping("clear")
    public Boolean clear() {
        return logService.remove(Wrappers.emptyWrapper());
    }
}
