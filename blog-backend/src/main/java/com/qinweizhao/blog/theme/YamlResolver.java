package com.qinweizhao.blog.theme;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Yaml resolver.
 *
 * @author johnniang
 */
public enum YamlResolver {

    /**
     * 实例
     */
    INSTANCE;

    private final ObjectMapper yamlMapper;

    YamlResolver() {
        // 创建一个默认的 yaml 映射器
        yamlMapper = new ObjectMapper(new YAMLFactory());
        yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Get yaml mapper.
     *
     * @return non-null yaml mapper
     */
    public ObjectMapper getYamlMapper() {
        return yamlMapper;
    }
}
