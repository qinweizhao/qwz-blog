package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.JournalDTO;
import com.qinweizhao.blog.model.param.JournalParam;
import com.qinweizhao.blog.model.param.JournalQuery;
import com.qinweizhao.blog.service.JournalService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Journal controller.
 *
 * @author johnniang
 * @author ryanwang
 * @since 2019-04-25
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/journals")
public class JournalController {

    private final JournalService journalService;

    /**
     * 分页
     *
     * @param param param
     * @return Page
     */
    @GetMapping
    public PageResult<JournalDTO> page(JournalQuery param) {
        return journalService.page(param);
    }

    /**
     * 最新记录
     *
     * @param top top
     * @return List
     */
    @GetMapping("latest")
    public List<JournalDTO> pageLatest(@RequestParam(name = "top", defaultValue = "10") int top) {
        JournalQuery param = new JournalQuery();
        param.setPage(top);

        PageResult<JournalDTO> page = journalService.page(param);
        return page.getContent();
    }

    /**
     * 新增
     *
     * @param param param
     * @return Boolean
     */
    @PostMapping
    public Boolean create(@RequestBody @Valid JournalParam param) {
        return journalService.save(param);
    }

    /**
     * 更新
     *
     * @param id    id
     * @param param param
     * @return Boolean
     */
    @PutMapping("{id:\\d+}")
    public Boolean updateBy(@PathVariable("id") Integer id,
                            @RequestBody @Valid JournalParam param) {
        return journalService.updateById(id, param);
    }

    /**
     * 删除
     *
     * @param journalId journalId
     * @return Boolean
     */
    @DeleteMapping("{journalId:\\d+}")
    public Boolean deleteBy(@PathVariable("journalId") Integer journalId) {
        return journalService.removeById(journalId);
    }

}
