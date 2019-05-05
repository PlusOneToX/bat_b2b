package com.bat.thirdparty.common.db.config;

import static com.bat.thirdparty.common.CommonErrorCode.TENANT_DB_NULL;
import static com.bat.thirdparty.common.CommonErrorCode.TENANT_NO_NULL;

import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.tenant.Tenant;
import com.bat.thirdparty.tenant.TenantContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDBRpcDTO;
import com.bat.thirdparty.ApplicationContextProvider;
import com.bat.thirdparty.common.db.service.executor.TenantDBQry;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 模块名称: 【Mysql】动态数据源实现类 模块描述: 关键在于determineCurrentLookupKey方法,就是这个方法实现了Spring动态切换数据源
 * 
 * @author bat(b2b_bat399 @ 163.com)
 * @date 2019/10/21 10:10
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 【核心】动态数据源维护的所有数据源信息 备注：Spring维护的所有数据源，实际上放在了AbstractRoutingDataSource抽象类中的targetDataSources属性中
     * 但是可能是出于安全考虑，AbstractRoutingDataSource只提供了setTargetDataSources的方法，却没有提供getTargetDataSources的方法
     * 这带来了非常大的不便，于是我们在这个子类中单独定义一个私有属性，并额外提供一个getTargetDataSources的方法 而setTargetDataSources本质上仍然是调用父级的。
     */
    private Map<Object, Object> targetDataSources;

    @Resource
    private DynamicDataSourceConfig dataSourceConfig;

    @Resource
    private TenantDBQry rpcQry;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
    }

    /**
     * 动态添加数据源
     * 
     * @param dbName
     * @param dataSource
     */
    public void addTargetDataSource(String dbName, HikariDataSource dataSource) {
        this.targetDataSources.put(dbName, dataSource);
    }

    /**
     * 动态删除数据源
     * 
     * @param dbName
     */
    public void removeTargetDataSource(String dbName) {
        this.targetDataSources.remove(dbName);
    }

    public Map<Object, Object> getTargetDataSources() {
        return this.targetDataSources;
    }

    /**
     * 如果不希望数据源在启动配置时就加载好,可以覆写定制这个方法.
     * 我们此处并没有覆写,用的仍然是父类的方法,只是针对找不到数据源的情况,多做了一层【懒加载防御】,从而实现【运行过程中】【动态更新】Spring管理的数据源,就不用重启程序了
     */
    @Override
    protected DataSource determineTargetDataSource() {
        HikariDataSource dataSource = null;
        super.setLenientFallback(false);
        boolean needReinit = false;
        String tenantNo = TenantContext.getTenantNo();
        try {
            dataSource = (HikariDataSource)super.determineTargetDataSource();
        } catch (IllegalStateException e) {
            // 捕获了这个异常说明【内存中】没有找到对应tenantNo的数据源,则触发防御机制:去[基础数据库]中查一把该租户信息
            needReinit = true;
        }
        // 【防御机制】内存动态数据源中不存在该租户对应的dataSource----------------------------防御开始
        if (needReinit) {
            DynamicDataSourceInit dynamicDataSourceInit =
                (DynamicDataSourceInit)ApplicationContextProvider.getBean("dynamicDataSourceInit");
            if (needReinit && StringUtils.isNotBlank(tenantNo) && !"null".equals(tenantNo)) {
                // 从缓存取数据库租户信息，如果未取到，再从平台服务取
                Tenant tenant = TenantContext.tenantInfoMap.get(tenantNo);
                if (tenant == null) {
                    tenant = new Tenant(tenantNo);
                    TenantContext.tenantInfoMap.put(tenantNo, tenant);
                }
                String dbName = tenant.getConfigMap().get("dbName");
                if (StringUtils.isNotBlank(dbName)) {
                    dataSource = (HikariDataSource)targetDataSources.get(dbName);
                }
                if (dataSource == null) {
                    PlatformTenantDBRpcDTO tenantDB = rpcQry.getTenantDB(tenantNo);
                    dbName = tenantDB.getDbName();
                    tenant.getConfigMap().put("dbName", dbName);
                    dataSource = dynamicDataSourceInit.getTenantDataSource(tenantDB);
                    addTargetDataSource(dbName, dataSource);
                }
                /**
                 * 设置数据源,并执行afterPropertiesSet,备注:必须执行afterPropertiesSet此操作，才会重新初始化AbstractRoutingDataSource 中的
                 * resolvedDataSources， 只有这样，Spring管理的数据源总库中才真正set了这些数据源
                 */
                super.afterPropertiesSet();
            } else {
                throw ThirdPartyException.buildException(TENANT_NO_NULL);
            }
        }
        // ------------------------------------------------------------------------------防御结束
        return dataSource;
    }

    /**
     * 如果希望所有数据源在启动配置时就加载好，这里通过设置数据源Key值来切换数据源，定制这个方法
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 如果线程上下文中没有设置tenantId,则传入默认值baseDataSource
        if (StringUtils.isNotBlank(TenantContext.getTenantNo())) {
            Tenant tenant = TenantContext.tenantInfoMap.get(TenantContext.getTenantNo());
            if (tenant == null) {
                tenant = new Tenant(TenantContext.getTenantNo());
                TenantContext.tenantInfoMap.put(TenantContext.getTenantNo(), tenant);
            }
            String dbName = tenant.getConfigMap().get("dbName");
            if (StringUtils.isBlank(dbName)) {
                PlatformTenantDBRpcDTO tenantDB = rpcQry.getTenantDB(TenantContext.getTenantNo());
                if (tenantDB == null) {
                    throw ThirdPartyException.buildException(TENANT_DB_NULL);
                }
                dbName = tenantDB.getDbName();
                tenant.getConfigMap().put("dbName", tenantDB.getDbName());
            }
            return dbName;
        } else {
            return dataSourceConfig.getBaseDbName();
        }
    }
}
