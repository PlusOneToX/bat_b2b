package com.bat.order.interceptor;

import static com.bat.order.common.ErrorCode.TENANT_NO_NULL;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.bat.order.api.common.exception.OrderException;
import com.bat.order.tenant.TenantContext;

/**
 * 前台用户接口拦截器
 */

@Component
public class UserInterceptor implements HandlerInterceptor {

    public static String InterceptorPath = "/order/v1/web/user/**";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
        throws Exception {
        String language = httpServletRequest.getHeader("language-currency");
        if (StringUtils.isEmpty(language)) {
            language = "zh_CNY";
        }
        String[] languages = language.split("_");
        if (languages.length > 1 && languages[0].equals("en")) {
            httpServletRequest.setAttribute("language", "en");
            httpServletRequest.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
                new Locale("en", "US"));
        } else {
            httpServletRequest.setAttribute("language", "zh");
            httpServletRequest.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
                new Locale("zh", "CN"));
        }
        // 租户编码
        String tenantNo = httpServletRequest.getHeader("tenantNo");
        if (StringUtils.isNotBlank(tenantNo)) {
            TenantContext.setTenantNo(tenantNo);
        } else {
            throw OrderException.buildException(TENANT_NO_NULL);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        TenantContext.removeTenantNo();
    }
}
