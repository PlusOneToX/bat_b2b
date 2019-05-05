package com.bat.dubboapi.thirdparty.erp.api;

import java.util.List;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.erp.dto.financial.BalanceInfoRpcQry;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.BalanceInfoDTO;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyErpRpcDTO;
import com.bat.dubboapi.thirdparty.erp.dto.financial.data.CurrencyRateErpRpcDTO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/4/20 10:45
 */
public interface ThirdPartyFinancialServiceErpRpc {

    /**
     * 获取币别
     * 
     * @return
     * @throws Exception
     */
    Response<List<CurrencyErpRpcDTO>> listCurrency() throws Exception;

    /**
     * 获取汇率
     * 
     * @return
     * @throws Exception
     */
    Response<List<CurrencyRateErpRpcDTO>> listCurrencyRate() throws Exception;

    /**
     * 获取余额
     * 
     * @param qry
     * @return
     */
    Response<List<BalanceInfoDTO>> listBalance(BalanceInfoRpcQry qry);

}
