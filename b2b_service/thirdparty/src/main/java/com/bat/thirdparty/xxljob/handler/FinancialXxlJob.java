package com.bat.thirdparty.xxljob.handler;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.bat.thirdparty.tenant.TenantContext;
import com.bat.thirdparty.xxljob.common.CommonUtils;
import com.bat.thirdparty.xxljob.service.executor.FinancialJobExe;
import com.xxl.job.core.handler.annotation.XxlJob;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/8/31 10:54
 */
@Component
public class FinancialXxlJob {

    @Resource
    private FinancialJobExe financialJobExe;

    @Resource
    private CommonUtils commonUtils;

    @XxlJob("syncDepositBalanceInfoJobHandler")
    public void syncDepositBalanceInfoJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        financialJobExe.syncDepositBalanceInfoJobHandler();
        TenantContext.removeTenantNo();
    }

    /**
     * 定时分账
     * 
     * @throws Exception
     */
    @XxlJob("subAccountJobHandler")
    public void subAccountJobHandler() throws Exception {
        commonUtils.getTenantNoByParam();
        financialJobExe.subAccountJobHandler();
        TenantContext.removeTenantNo();
    }
}
