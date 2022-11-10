package com.qinweizhao.blog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.framework.cache.AbstractStringCacheStore;
import com.qinweizhao.blog.framework.cache.InMemoryCacheStore;
import com.qinweizhao.blog.framework.cache.LevelCacheStore;
import com.qinweizhao.blog.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @author qinweizhao
 * @since 2022/7/4
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MyBlogProperties.class)
public class MyBlogConfiguration {

    @Resource
    private MyBlogProperties myBlogProperties;

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.failOnEmptyBeans(false);
        return builder.build();
    }

    @Bean
    public RestTemplate httpsRestTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplate httpsRestTemplate = builder.build();
        httpsRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientUtils.createHttpsClient((int) myBlogProperties.getDownloadTimeout().toMillis())));
        return httpsRestTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public AbstractStringCacheStore stringCacheStore() {
        AbstractStringCacheStore stringCacheStore;
        switch (myBlogProperties.getCache()) {
            case "level":
                stringCacheStore = new LevelCacheStore(this.myBlogProperties);
                break;
            case "memory":
            default:
                stringCacheStore = new InMemoryCacheStore();
                break;
        }
        log.info("Blog cache store load impl : [{}]", stringCacheStore.getClass());
        return stringCacheStore;

    }
}
