package com.bat.order.service.third.financial;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.financial.account.dto.AccountWxDistributorRpcQryDTO;
import com.bat.dubboapi.financial.basesetting.api.FinancialDistributorAccountServiceRpc;
import com.bat.dubboapi.financial.common.Response;
import com.bat.order.api.common.exception.OrderException;

@Component
public class FinancialQryExeRpc {

    @DubboReference(check = false, timeout = 8000, retries = 0)
    private FinancialDistributorAccountServiceRpc financialDistributorAccountServiceRpc;

    /**
     * 根据分销商id、appid、账户类型查询微信支付账户列表
     * 
     * @param distributorId
     * @param appId
     * @param accountType
     * @return
     */
    public List<AccountWxDistributorRpcQryDTO> listWxPayAccountByCondition(Integer distributorId, String appId,
        Short accountType) {

        Response<List<AccountWxDistributorRpcQryDTO>> response =
            financialDistributorAccountServiceRpc.listWxPayAccountByCondition(distributorId, appId, accountType);
        if (!response.isSuccess()) {
            throw OrderException.buildException(response.getErrCode(), response.getErrMessage());
        }
        return response.getData();
    }
}
