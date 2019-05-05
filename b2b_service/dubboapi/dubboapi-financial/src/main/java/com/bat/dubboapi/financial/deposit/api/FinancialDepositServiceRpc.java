package com.bat.dubboapi.financial.deposit.api;

import com.bat.dubboapi.financial.basesetting.dto.data.AccountBalanceChangeReq;
import com.bat.dubboapi.financial.common.Response;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 14:28
 */
public interface FinancialDepositServiceRpc {

    /**
     * erp 修改余额
     * 
     * @param req
     * @return
     */
    Response accountBalanceChange(AccountBalanceChangeReq req);

    /**
     * 全量同步ERP余额
     */
    Response syncDepositBalanceInfo();
}
