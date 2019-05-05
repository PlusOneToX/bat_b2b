package com.bat.thirdparty.xxljob.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.common.CommonUtils;
import com.bat.thirdparty.xxljob.service.executor.OrderGoodsJobExe;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * 沙漠
 */
@Component
public class OrderGoodsXxlJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderGoodsXxlJob.class);

    @Resource
    private OrderGoodsJobExe orderGoodsJobExe;

    @Resource
    private CommonUtils commonUtils;

    /**
     * 库存 redis同步到mysql
     * 
     * @throws Exception
     */
    @XxlJob("orderGoodsAutoUpdateSaleStatus")
    public void orderGoodsAutoUpdateSaleStatus() throws Exception {
        commonUtils.getTenantNoByParam();
        orderGoodsJobExe.autoUpdateSaleStatus(TenantContext.getTenantNo());
        TenantContext.removeTenantNo();
    }

}
