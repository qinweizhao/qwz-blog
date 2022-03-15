package com.qinweizhao.site.handler.migrate;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.qinweizhao.site.model.enums.MigrateType;
import com.qinweizhao.site.service.BackupService;

import java.io.IOException;

/**
 * @author ryanwang
 * @date 2020-03-14
 */
@Component
public class HaloMigrateHandler implements MigrateHandler {

    private final BackupService backupService;

    public HaloMigrateHandler(BackupService backupService) {
        this.backupService = backupService;
    }

    @Override
    public void migrate(MultipartFile file) {
        try {
            backupService.importData(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean supportType(MigrateType type) {
        return MigrateType.HALO.equals(type);
    }
}
