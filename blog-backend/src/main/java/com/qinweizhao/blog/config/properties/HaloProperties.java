package com.qinweizhao.blog.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.qinweizhao.blog.model.enums.Mode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.qinweizhao.blog.model.support.HaloConst.*;
import static com.qinweizhao.blog.utils.HaloUtils.ensureSuffix;


/**
 * 配置属性
 *
 * @author johnniang
 * @author ryanwang
 * @author qinweizhao
 * @date 2019-03-15
 */
@Data
@ConfigurationProperties("halo")
public class HaloProperties {

    /**
     * Doc api disabled. (Default is true)
     */
    private boolean docDisabled = true;

    /**
     * Production env. (Default is true)
     */
    private boolean productionEnv = true;

    /**
     * Authentication enabled
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
     * 工作目录
     */
    private String workDir = ensureSuffix(USER_HOME, FILE_SEPARATOR) + FILE_SEPARATOR;

    /**
     * 备份目录。（不建议修改此配置）
     */
    private String backupDir = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "halo-backup" + FILE_SEPARATOR;

    /**
     * Halo 数据导出目录
     */
    private String dataExportDir = ensureSuffix(TEMP_DIR, FILE_SEPARATOR) + "halo-data-export" + FILE_SEPARATOR;

    /**
     * 上传前缀
     */
    private String uploadUrlPrefix = "upload";

    /**
     * 下载超时
     */
    private Duration downloadTimeout = Duration.ofSeconds(30);

    /**
     * 缓存存储实现
     * memory
     * level
     */
    private String cache = "memory";

    private List<String> cacheRedisNodes = new ArrayList<>();

    private String cacheRedisPassword = "";

    /**
     * hazelcast cache store impl
     * memory
     * level
     */
    private List<String> hazelcastMembers = new ArrayList<>();

    private String hazelcastGroupName;

    private int initialBackoffSeconds = 5;
}
