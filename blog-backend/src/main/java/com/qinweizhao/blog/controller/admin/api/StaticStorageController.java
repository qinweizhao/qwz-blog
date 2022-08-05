package com.qinweizhao.blog.controller.admin.api;

import com.qinweizhao.blog.model.param.StaticContentParam;
import com.qinweizhao.blog.model.support.StaticFile;
import com.qinweizhao.blog.service.StaticStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Static storage controller.
 *
 * @author ryanwang
 * @date 2019-12-06
 */
@RestController
@RequestMapping("/api/admin/statics")
public class StaticStorageController {

    private final StaticStorageService staticStorageService;

    public StaticStorageController(StaticStorageService staticStorageService) {
        this.staticStorageService = staticStorageService;
    }

    /**
     * 列出静态文件
     *
     * @return List
     */
    @GetMapping
    public List<StaticFile> list() {
        return staticStorageService.listStaticFolder();
    }

    /**
     * 按相对路径删除文件
     *
     * @param path path
     */
    @DeleteMapping
    public void deletePermanently(@RequestParam("path") String path) {
        staticStorageService.delete(path);
    }

    @PostMapping
    public void createFolder(String basePath,
                             @RequestParam("folderName") String folderName) {
        staticStorageService.createFolder(basePath, folderName);
    }

    /**
     * @param basePath
     * @param file
     */
    @PostMapping("upload")
    public void upload(String basePath,
                       @RequestPart("file") MultipartFile file) {
        staticStorageService.upload(basePath, file);
    }

    @PostMapping("rename")
    public void rename(String basePath,
                       String newName) {
        staticStorageService.rename(basePath, newName);
    }

    @PutMapping("files")
    public void save(@RequestBody StaticContentParam param) {
        staticStorageService.save(param.getPath(), param.getContent());
    }
}
