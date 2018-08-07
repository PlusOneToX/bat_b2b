package com.bat.financial.deposit.executor;

import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.AutoReleaseLock;
import com.bat.dubboapi.thirdparty.erp.api.ThirdPartyFinancialServiceErpRpc;
import com.bat.dubboapi.thirdparty.erp.dto.financial.BalanceInfoRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.BalanceInfoDTO;
import com.bat.financial.Tenant.TenantContext;
import com.bat.financial.api.base.FinancialException;
import com.bat.financial.api.deposit.dto.BalanceInfoSyncQry;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: lim
 * @description: 该类的主要作用是 避免 Async 同类调用 失效
 * @date: 2018/5/27 15:01
 */
@Component
@Slf4j
public class DepositBalanceCmdAsyncExc {

    @Resource
    private DepositBalanceCmdExc depositCmdExc;

    @DubboReference(check = false, timeout = 180000)
    private ThirdPartyFinancialServiceErpRpc thirdPartyFinancialServiceErpRpc;

    @Async
    public void asyncDepositBalanceInfo(BalanceInfoSyncQry syncCmd, String tenantNo, AutoReleaseLock autoReleaseLock) {
        TenantContext.setTenantNo(tenantNo);
        try {
            log.info("financial sync deposit balance start");
            BalanceInfoRpcQry balanceInfoRpcQry = new BalanceInfoRpcQry();
            balanceInfoRpcQry.setFCUSTID(syncCmd.getFCUSTID());
            com.bat.dubboapi.thirdparty.common.Response<List<BalanceInfoDTO>> listResponse =
                thirdPartyFinancialServiceErpRpc.listBalance(balanceInfoRpcQry);
            if (listResponse.isSuccess()) {
                List<BalanceInfoDTO> data = listResponse.getData();
                log.info("financial sync deposit balance response json:{}", JSON.toJSONString(data));
                depositCmdExc.syncDepositBalanceInfo(data);
                log.info("余额全量同步成功");
            } else {
                throw FinancialException.buildException(listResponse.getErrCode(), listResponse.getErrMessage());
            }
        } finally {
            TenantContext.removeTenantNo();
            autoReleaseLock.close();
        }
    }
}
