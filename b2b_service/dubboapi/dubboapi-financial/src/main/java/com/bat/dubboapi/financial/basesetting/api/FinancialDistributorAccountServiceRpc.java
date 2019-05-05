package com.bat.dubboapi.financial.basesetting.api;

import com.bat.dubboapi.financial.account.dto.AccountWxDistributorRpcQryDTO;
import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.basesetting.dto.data.AccountWxDistributorRpcDTO;

import java.util.List;

public interface FinancialDistributorAccountServiceRpc {
    /**
     * 根据id获取 分销商支付信息
     * 
     * @param id
     * @return
     */
    Response<AccountWxDistributorRpcDTO> getDistributorWxAccountById(Integer id);

    /**
     * 根据分销商id和appi查询微信支付配置
     * @param distributorId
     * @param appId
     * @return
     */
    Response<List<AccountWxDistributorRpcQryDTO>> listWxPayAccountByCondition(Integer distributorId, String appId, Short accountType);


}
