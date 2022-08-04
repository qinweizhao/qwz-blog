package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.core.PageResult;
import com.qinweizhao.blog.model.dto.PhotoDTO;
import com.qinweizhao.blog.model.params.PhotoQueryParam;
import com.qinweizhao.blog.model.params.PhotoParam;
import com.qinweizhao.blog.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Photo controller
 *
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/photos")
public class PhotoController {

    private final PhotoService photoService;

    /**
     * 列出最新照片
     *
     * @return List
     */
    @GetMapping(value = "latest")
    public List<PhotoDTO> listPhotos() {
        return photoService.list();
    }

    @GetMapping
    public PageResult<PhotoDTO> pageBy(PhotoQueryParam param) {
        return photoService.page(param);
    }

    /**
     * 详细信息
     *
     * @param photoId photoId
     * @return PhotoDTO
     */
    @GetMapping("{photoId:\\d+}")
    public PhotoDTO get(@PathVariable("photoId") Integer photoId) {
        return photoService.getById(photoId);
    }

    /**
     * 删除
     *
     * @param photoId photoId
     * @return Boolean
     */
    @DeleteMapping("{photoId:\\d+}")
    public Boolean remove(@PathVariable("photoId") Integer photoId) {
        return photoService.removeById(photoId);
    }

    /**
     * 新增
     *
     * @param photoParam photoParam
     * @return PhotoDTO
     */
    @PostMapping
    public Boolean createBy(@Valid @RequestBody PhotoParam photoParam) {
        return photoService.save(photoParam);
    }


    /**
     * 更新
     *
     * @param photoId    photoId
     * @param photoParam photoParam
     * @return PhotoDTO
     */
    @PutMapping("{photoId:\\d+}")
    public Boolean updateBy(@PathVariable("photoId") Integer photoId,
                            @RequestBody @Valid PhotoParam photoParam) {
        return photoService.updateById(photoId, photoParam);

    }

    /**
     * listTeams
     *
     * @return List
     */
    @GetMapping("teams")
    public List<String> listTeams() {
        return photoService.listTeams();
    }
}
