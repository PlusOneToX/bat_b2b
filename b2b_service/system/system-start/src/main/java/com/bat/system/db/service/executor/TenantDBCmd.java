package com.bat.system.db.service.executor;

import javax.annotation.Resource;

import com.bat.system.Tenant.TenantContext;
import com.bat.system.db.config.DynamicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TenantDBCmd {

    @Resource
    private DynamicDataSource dynamicDataSource;

    /**
     * 根据租户删除租户相关信息
     * 
     * @param tenantNo
     */
    public void tenantDBDelete(String tenantNo, String dbName) {
        // 删除租户信息
        TenantContext.tenantInfoMap.remove(tenantNo);
        if (StringUtils.isNotBlank(dbName)) {
            // 删除租户数据库资源
            dynamicDataSource.removeTargetDataSource(dbName);
            dynamicDataSource.afterPropertiesSet();
        }
    }

}
