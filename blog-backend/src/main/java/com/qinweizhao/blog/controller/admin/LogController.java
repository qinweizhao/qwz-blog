package com.qinweizhao.blog.controller.admin;

import com.qinweizhao.blog.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Log controller.
 *
 * @author johnniang
 * @date 2019-03-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/logs")
public class LogController {

    private final LogService logService;


//    @GetMapping("latest")
//    @ApiOperation("Pages latest logs")
//    public List<LogDTO> pageLatest(@RequestParam(name = "top", defaultValue = "10") int top) {
//        return logService.pageLatest(top).getContent();
//    }
//
//    @GetMapping
//    @ApiOperation("Lists logs")
//    public Page<LogDTO> pageBy(@PageableDefault(sort = "createTime", direction = DESC) Pageable pageable) {
//        Page<Log> logPage = logService.listAll(pageable);
//        return logPage.map(log -> new LogDTO().convertFrom(log));
//    }
//
//    @GetMapping("clear")
//    @ApiOperation("Clears all logs")
//    public void clear() {
//        logService.removeAll();
//    }
}
