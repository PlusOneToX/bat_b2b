package com.bat.financial.pay;

import java.util.List;

import com.bat.financial.common.error.DistributorAccountErrorCode;
import com.bat.financial.dao.distributoraccount.dataobject.AccountWxDistributorDO;
import com.bat.financial.distributoraccount.executor.AccountWxDistributorQryExc;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.bat.dubboapi.financial.common.Response;
import com.bat.dubboapi.financial.pay.WxPayServiceRpc;
import com.bat.dubboapi.financial.pay.dto.WxPayConfigQry;
import com.bat.financial.api.base.MessageUtils;

@DubboService
public class WxPayServiceRpcImpl implements WxPayServiceRpc {

    @Autowired
    private AccountWxDistributorQryExc accountWxDistributorQryExc;

    /**
     * 根据分销商ID和APPID查询微信支付V3版本客户端
     * 
     * @param distributorId
     * @param appId
     * @return
     */
    @Override
    public Response<WxPayConfigQry> getWxPayClientV3ByDistributorIdAndAppId(Integer distributorId, Short appType,
        String appId, Short accountType) {
        List<AccountWxDistributorDO> wxDistributorDOList =
            accountWxDistributorQryExc.listByCondition(distributorId, appType, appId, accountType);
        if (wxDistributorDOList == null || wxDistributorDOList.size() == 0) {
            return Response.buildFailure(DistributorAccountErrorCode.P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL,
                MessageUtils.get(DistributorAccountErrorCode.P_ACCOUNT_WXPAY_DISTRIBUTOR_ACCOUNT_NULL));
        }
        AccountWxDistributorDO accountWxDistributorDO = wxDistributorDOList.get(0);

        WxPayConfigQry wxConfig = new WxPayConfigQry();
        wxConfig.setAppId(accountWxDistributorDO.getAppId());
        wxConfig.setAccountNo(accountWxDistributorDO.getAccountNo());
        wxConfig.setAppKey(accountWxDistributorDO.getAppKey());
        wxConfig.setApiclientKey(accountWxDistributorDO.getApiclientKey());
        wxConfig.setSerialNumber(accountWxDistributorDO.getSerialNumber());
        return Response.of(wxConfig);
    }
}
