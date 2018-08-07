package com.bat.distributor.tenant;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 模块名称: 【Mysql】动态数据源key值维护类 模块描述: 在线程上下文对象中维护当前租户的key,实际上key=tenantId
 * 
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2018/05/21 10:12
 */
@Slf4j
public class TenantContext {
    /**
     * 【维护租户信息的全局核心Map】以tenantNo为键,以Tenant对象为值,在调用DynamicDataSourceInit内方法初始化数据源的时候会将租户信息备份到该Map,可用于后续判断租户数据源是否存在等
     */
    public static Map<String, Tenant> tenantInfoMap = new LinkedHashMap<>();

    private static ThreadLocal<String> currentTenantNo = new ThreadLocal<>();

    /**
     * 切换当前租户id
     *
     * @param tenantNo
     */
    public static void setTenantNo(String tenantNo) {
        // 【防御机制】tenantId=字符串的"null"，也认定为null
        if ("null".equals(tenantNo)) {
            tenantNo = null;
        }
        if (log.isDebugEnabled()) {
            log.debug("租户上下文类TenantContext:执行setTenantId方法,当前租户id切换过程:{}-->{}", currentTenantNo.get(), tenantNo);
        }
        currentTenantNo.set(tenantNo);
    }

    /**
     * 获取当前租户NO
     *
     * @return
     */
    public static String getTenantNo() {
        String tenantNo = currentTenantNo.get();
        // 【防御机制】tenantId=字符串的"null"，也认定为null
        if ("null".equals(tenantNo)) {
            tenantNo = null;
        }
        // 为空(null或者空串)统一返回null
        return StringUtils.isNotBlank(tenantNo) ? tenantNo : null;
    }

    /**
     * 重置租户NO
     */
    public static void removeTenantNo() {
        currentTenantNo.remove();
        if (log.isDebugEnabled()) {
            log.debug("租户上下文类TenantContext:执行removeTenantId方法,重置后动态数据源key值:{}", currentTenantNo.get());
        }
    }

    /**
     * 判断当前租户上下文维护的租户信息中是否有某个租户id
     *
     * @param tenantNo
     * @return
     */
    public static boolean containTenantNo(String tenantNo) {
        return tenantInfoMap.containsKey(tenantNo);
    }
}
