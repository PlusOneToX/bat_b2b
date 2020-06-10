package com.bat.flexible.interceptor;

import com.bat.flexible.Tenant.TenantContext;
import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.common.ErrorCode;
import com.bat.flexible.api.FlexibleCustomException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 前台用户接口拦截器
 */

@Component
public class AdminInterceptor implements HandlerInterceptor {

    public static String InterceptorPath = "/flexible/v1/web/admin/**";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
        throws Exception {
        // 租户编码
        String tenantNo = httpServletRequest.getHeader("tenantNo");
        if (StringUtils.isNotBlank(tenantNo)) {
            TenantContext.setTenantNo(tenantNo);
        } else {
            throw new FlexibleCustomException(MessageUtils.get(ErrorCode.TENANT_NO_NULL));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        TenantContext.removeTenantNo();
    }
}
