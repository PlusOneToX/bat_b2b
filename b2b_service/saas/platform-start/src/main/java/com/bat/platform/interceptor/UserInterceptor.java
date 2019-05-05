package com.bat.platform.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * 前台用户接口拦截器
 */

@Component
public class UserInterceptor implements HandlerInterceptor {

    public static String InterceptorPath = "/v1/*/user/*";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
        throws Exception {
        String language = httpServletRequest.getHeader("language-currency");
        if (StringUtils.isEmpty(language)) {
            language = "zh_CNY";
        }
        String[] languages = language.split("_");
        if (languages.length > 1 && languages[0].equals("en")) {
            httpServletRequest.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
                new Locale("en", "US"));
        } else {
            httpServletRequest.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
                new Locale("zh", "CN"));
        }
        return true;
    }
}
