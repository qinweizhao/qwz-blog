package com.qinweizhao.site.theme;

import com.qinweizhao.site.exception.ServiceException;
import com.qinweizhao.site.exception.ThemePropertyMissingException;
import com.qinweizhao.site.handler.theme.config.support.ThemeProperty;
import com.qinweizhao.site.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.ZipInputStream;

import static com.qinweizhao.site.utils.FileUtils.unzip;

/**
 * Multipart zip file theme fetcher.
 *
 * @author johnniang
 */
@Slf4j
public class MultipartZipFileThemeFetcher implements ThemeFetcher {

    @Override
    public boolean support(Object source) {
        if (source instanceof MultipartFile) {
            final var filename = ((MultipartFile) source).getOriginalFilename();
            return filename != null && filename.endsWith(".zip");
        }
        return false;
    }

    @Override
    public ThemeProperty fetch(Object source) {
        final var file = (MultipartFile) source;

        try (var zis = new ZipInputStream(file.getInputStream())) {
            final var tempDirectory = FileUtils.createTempDirectory();
            log.info("Unzipping {} to path {}", file.getOriginalFilename(), tempDirectory);
            unzip(zis, tempDirectory);
            return ThemePropertyScanner.INSTANCE.fetchThemeProperty(tempDirectory)
                    .orElseThrow(() -> new ThemePropertyMissingException("主题配置文件缺失！请确认后重试。"));
        } catch (IOException e) {
            throw new ServiceException("主题上传失败！", e);
        }
    }

}
