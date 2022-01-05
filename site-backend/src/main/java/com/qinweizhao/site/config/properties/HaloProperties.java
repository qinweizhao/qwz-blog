package com.qinweizhao.site.config.properties;

import com.qinweizhao.site.model.enums.Mode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

import static com.qinweizhao.site.model.support.HaloConst.*;
import static com.qinweizhao.site.utils.HaloUtils.ensureSuffix;


/**
 * Halo configuration properties.
 *
 * @author johnniang
 * @author ryanwang
 * @date 2019-03-15
 */
@Data
@ConfigurationProperties("halo")
public class HaloProperties {

    /**
     * Authentication enabled.
     */
    private boolean authEnabled = true;

    /**
     * Halo startup mode.
     */
    private Mode mode = Mode.PRODUCTION;

    /**
     * Admin path.
     */
    private String adminPath = "admin";

    /**
     * Work directory.
     */
    private String workDir = ensureSuffix(USER_HOME, FILE_SEPARATOR) + ".halo" + FILE_SEPARATOR;

    /**
     * Halo backup directory.(Not recommended to modify this config);
     */
    private String backupDir =
            ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "halo-backup" + FILE_SEPARATOR;

    /**
     * Halo backup markdown directory.(Not recommended to modify this config);
     */
    private String backupMarkdownDir =
            ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "halo-backup-markdown" + FILE_SEPARATOR;

    /**
     * Halo data export directory.
     */
    private String dataExportDir =
            ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "halo-data-export" + FILE_SEPARATOR;

    /**
     * Upload prefix.
     */
    private String uploadUrlPrefix = "upload";

    /**
     * Download Timeout.
     */
    private Duration downloadTimeout = Duration.ofSeconds(30);

    /**
     * cache store impl
     * memory
     * level
     */
    private String cache = "memory";
}
