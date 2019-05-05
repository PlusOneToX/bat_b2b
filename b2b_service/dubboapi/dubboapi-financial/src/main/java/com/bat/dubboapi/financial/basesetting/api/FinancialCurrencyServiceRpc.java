package com.bat.dubboapi.financial.basesetting.api;

import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRateRpcDTO;
import com.bat.dubboapi.financial.basesetting.dto.data.CurrencyRpcDO;

/**
 * @author: lim
 * @description: TODO
 * @date: 2019/5/11 14:28
 */
public interface FinancialCurrencyServiceRpc {
    /**
     * 获取币别通过币别编码
     * 
     * @param CurrencyCode
     * @return
     */
    Response<CurrencyRpcDO> getCurrencyByCurrencyCode(String CurrencyCode);

    /**
     * 获取币别通过ERP币别编码
     *
     * @param erpNo
     * @return
     */
    Response<CurrencyRpcDO> getCurrencyByErpNo(String erpNo);

    /**
     * 根据本位币 与目标币获取汇率
     * 
     * @param forCurrencyCode
     * @param toCurrencyCode
     * @return
     */
    Response<CurrencyRateRpcDTO> getCurrencyRate(String forCurrencyCode, String toCurrencyCode);
}
