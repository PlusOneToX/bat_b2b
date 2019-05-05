package com.bat.thirdparty.xxljob.service.executor;

import com.bat.thirdparty.message.service.MessageSendService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.financial.deposit.api.FinancialDepositServiceRpc;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/8/31 10:56
 */
@Component
public class FinancialJobExe {

    @DubboReference(check = false, timeout = 30000, retries = 3)
    private FinancialDepositServiceRpc financialDepositServiceRpc;

    @Autowired
    private MessageSendService messageSendService;

    public void syncDepositBalanceInfoJobHandler() {
        financialDepositServiceRpc.syncDepositBalanceInfo();
    }

    public void subAccountJobHandler() {
        messageSendService.wxSubAccount();
    }
}
