package com.bat.thirdparty.xxljob.handler;

import javax.annotation.Resource;

import com.bat.thirdparty.xxljob.service.executor.WarehouseJobExe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.common.CommonUtils;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/7/15 20:52
 */
@Component
public class WarehouseXxlJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseXxlJob.class);

    @Resource
    private WarehouseJobExe warehouseJobExe;

    @Resource
    private CommonUtils commonUtils;
    /**
     * 库存 redis同步到mysql
     * 
     * @throws Exception
     */
    @XxlJob("warehouseRedisSyncMysqlJobHandler")
    public void warehouseRedisSyncMysqlJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        warehouseJobExe.stockRedisSyncMysqlJobHandler();
        TenantContext.removeTenantNo();
    }

    /**
     * 库存 ERP同步到B2B
     *
     * @throws Exception
     */
    @XxlJob("warehouseErpSyncB2BJobHandler")
    public void warehouseErpSyncB2BJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        warehouseJobExe.stockErpSyncB2BJobHandler();
        TenantContext.removeTenantNo();
    }
}
