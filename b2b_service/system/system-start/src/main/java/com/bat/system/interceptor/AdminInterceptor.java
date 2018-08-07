package com.bat.system.interceptor;

import com.bat.system.Tenant.TenantContext;
import com.bat.system.api.base.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.bat.system.common.ErrorCode.TENANT_NO_NULL;


/**
 * 前台用户接口拦截器
 */

@Component
public class AdminInterceptor implements HandlerInterceptor {

    public static String InterceptorPath = "/system/v1/web/admin/**";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
        throws Exception {
        // 租户编码
        String tenantNo = httpServletRequest.getHeader("tenantNo");
        if (StringUtils.isNotBlank(tenantNo)) {
            TenantContext.setTenantNo(tenantNo);
        } else {
            throw SystemException.buildException(TENANT_NO_NULL);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        TenantContext.removeTenantNo();
    }
}
