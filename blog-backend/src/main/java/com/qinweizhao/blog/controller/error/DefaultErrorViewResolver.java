package com.qinweizhao.blog.controller.error;

import com.qinweizhao.blog.model.support.BlogConst;
import com.qinweizhao.blog.service.ThemeService;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus.Series;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author qinweizhao
 * @since 2022/09/15
 */
@Component
@AllArgsConstructor
public class DefaultErrorViewResolver implements ErrorViewResolver {

    private static final Map<Series, String> SERIES_VIEWS;

    private final ThemeService themeService;


    static {
        EnumMap<Series, String> views = new EnumMap<>(Series.class);
        views.put(Series.CLIENT_ERROR, "4xx");
        views.put(Series.SERVER_ERROR, "5xx");
        SERIES_VIEWS = Collections.unmodifiableMap(views);

    }

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {


        //  400.ftl
        ModelAndView modelAndView = resolve(String.valueOf(status.value()), model);

        //  4xx.ftl
        if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
            modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
        }

        //  error.ftl
        if (modelAndView == null) {
            modelAndView = resolve("error", model);
        }

        if (modelAndView == null) {
            modelAndView = new ModelAndView("common/error/error", model);
        }

        return modelAndView;
    }

    private ModelAndView resolve(String viewName, Map<String, Object> model) {
        boolean flag = themeService.templateExists(viewName + BlogConst.SUFFIX_FTL);
        if (flag) {
            String errorViewName = themeService.render(viewName);
            return new ModelAndView(errorViewName, model);
        }
        return null;
    }

}
