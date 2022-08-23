package com.qinweizhao.blog.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.qinweizhao.blog.config.properties.MyBlogProperties;
import com.qinweizhao.blog.framework.factory.StringToEnumConverterFactory;
import com.qinweizhao.blog.framework.security.resolver.AuthenticationArgumentResolver;
import com.qinweizhao.blog.model.support.HaloConst;
import freemarker.core.TemplateClassResolver;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.qinweizhao.blog.model.support.HaloConst.FILE_SEPARATOR;
import static com.qinweizhao.blog.util.HaloUtils.*;

/**
 * @author ryanwang
 * @author qinweizhao
 * @since 2018-01-02
 */
@Slf4j
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(MultipartProperties.class)
public class MyWebMvcConfig implements WebMvcConfigurer {

    private static final String FILE_PROTOCOL = "file:///";

    private final MyBlogProperties myBlogProperties;

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String TIME_FORMAT = "yyyy-MM-dd";

//    /**
//     *
//     * @param converters converters
//     */
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.stream()
//                .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
//                .findFirst()
//                .ifPresent(converter -> {
//                    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converter;
//                    Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
//                    JsonComponentModule module = new JsonComponentModule();
//                    SimpleModule simpleModule = new SimpleModule();
//
//                    //  LocalDateTime时间格式化
//                    simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
//
//                    // LocalDate时间格式化
//                    simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
//
//                    // LocalTime时间格式化
//                    simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));
//
//                    ObjectMapper objectMapper = builder.modules(module).build();
//
//                    objectMapper.registerModule(simpleModule);
//
//                    //Data 时间格式化
//                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                    objectMapper.setDateFormat(new SimpleDateFormat(DATE_TIME_FORMAT));
//
//                    mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
//                });
//    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationArgumentResolver());
    }

    /**
     * 配置静态资源路径
     *
     * @param registry registry
     */
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        String workDir = FILE_PROTOCOL + ensureSuffix(myBlogProperties.getWorkDir(), FILE_SEPARATOR);
        String uploadUrlPattern = ensureBoth(myBlogProperties.getUploadUrlPrefix(), URL_SEPARATOR) + "**";
        String adminPathPattern = ensureSuffix(myBlogProperties.getAdminPath(), URL_SEPARATOR) + "**";

        String frontendDirName = myBlogProperties.getFrontendDirName();

        if (myBlogProperties.isProductionEnv()) {
            log.debug("当前环境为生产环境");
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/admin/")
                    .addResourceLocations(workDir + "static/");

            registry.addResourceHandler("/themes/**")
                    .addResourceLocations(workDir + frontendDirName + "/");

            registry.addResourceHandler(uploadUrlPattern)
                    .setCacheControl(CacheControl.maxAge(7L, TimeUnit.DAYS))
                    .addResourceLocations(workDir + "image/");

        } else {
            log.debug("当前环境为开发环境");
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:/admin/")
                    .addResourceLocations(workDir + "blog-resource/static/");

            registry.addResourceHandler("/themes/**")
                    .addResourceLocations(FILE_PROTOCOL + myBlogProperties.getWorkDir() + frontendDirName + "/");

            String imageUrlPattern = ensureBoth("blog-resource/image/", URL_SEPARATOR) + "**";
            registry.addResourceHandler(imageUrlPattern)
                    .setCacheControl(CacheControl.maxAge(7L, TimeUnit.DAYS))
                    .addResourceLocations(workDir + "blog-resource/image/");

        }
        registry.addResourceHandler(adminPathPattern)
                .addResourceLocations("classpath:/admin/");
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }

    /**
     * 配置 freemarker 模板文件路径。
     *
     * @return new FreeMarkerConfigurer
     */
    @Bean
    public FreeMarkerConfigurer freemarkerConfig(MyBlogProperties myBlogProperties) throws IOException, TemplateException {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        String frontendDirName = myBlogProperties.getFrontendDirName();
        configurer.setTemplateLoaderPaths(FILE_PROTOCOL + myBlogProperties.getWorkDir() + frontendDirName + "/", "classpath:/templates/");
        configurer.setDefaultEncoding("UTF-8");

        Properties properties = new Properties();
        properties.setProperty("auto_import", "/common/macro/common_macro.ftl as common,/common/macro/global_macro.ftl as global");

        configurer.setFreemarkerSettings(properties);

        // 预定义配置
        freemarker.template.Configuration configuration = configurer.createConfiguration();

        configuration.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);

        if (myBlogProperties.isProductionEnv()) {
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        }

        configurer.setConfiguration(configuration);

        return configurer;
    }

    /**
     * 配置文件上传解析器
     *
     * @return new multipartResolver
     */
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(MultipartProperties multipartProperties) {
        MultipartConfigElement multipartConfigElement = multipartProperties.createMultipartConfig();
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        resolver.setMaxUploadSize(multipartConfigElement.getMaxRequestSize());
        resolver.setMaxUploadSizePerFile(multipartConfigElement.getMaxFileSize());

        // 惰性多部分解析，一旦应用程序尝试获取多部分文件，就会抛出解析异常
        resolver.setResolveLazily(true);

        return resolver;
    }

    /**
     * 配置视图解析器
     *
     * @param registry registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setAllowRequestOverride(false);
        resolver.setCache(false);
        resolver.setExposeRequestAttributes(false);
        resolver.setExposeSessionAttributes(false);
        resolver.setExposeSpringMacroHelpers(true);
        resolver.setSuffix(HaloConst.SUFFIX_FTL);
        resolver.setContentType("text/html; charset=UTF-8");
        registry.viewResolver(resolver);
    }

    @Bean
    WebMvcRegistrations webMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new MyRequestMappingHandlerMapping(myBlogProperties);
            }
        };
    }
}
