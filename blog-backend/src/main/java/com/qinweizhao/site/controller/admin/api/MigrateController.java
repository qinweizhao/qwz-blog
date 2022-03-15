package com.qinweizhao.site.controller.admin.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.qinweizhao.site.exception.BadRequestException;
import com.qinweizhao.site.model.enums.MigrateType;
import com.qinweizhao.site.model.properties.PrimaryProperties;
import com.qinweizhao.site.service.MigrateService;
import com.qinweizhao.site.service.OptionService;

/**
 * Migrate controller
 *
 * @author ryanwang
 * @date 2019-10-29
 */
@RestController
@RequestMapping("/api/admin/migrations")
public class MigrateController {

    private final MigrateService migrateService;

    private final OptionService optionService;

    public MigrateController(MigrateService migrateService,
            OptionService optionService) {
        this.migrateService = migrateService;
        this.optionService = optionService;
    }

    @PostMapping("halo")
    @ApiOperation("Migrate from Halo")
    public void migrateHalo(@RequestPart("file") MultipartFile file) {
        if (optionService.getByPropertyOrDefault(PrimaryProperties.IS_INSTALLED, Boolean.class, false)) {
            throw new BadRequestException("无法在博客初始化完成之后迁移数据");
        }
        migrateService.migrate(file, MigrateType.HALO);
    }

    //    @PostMapping("wordpress")
    //    @ApiOperation("Migrate from WordPress")
    //    public void migrateWordPress(@RequestPart("file") MultipartFile file) {
    //        migrateService.migrate(file, MigrateType.WORDPRESS);
    //    }
    //
    //    @PostMapping("cnblogs")
    //    @ApiOperation("Migrate from cnblogs")
    //    public void migrateCnBlogs(@RequestPart("file") MultipartFile file) {
    //        migrateService.migrate(file, MigrateType.CNBLOGS);
    //    }
}
