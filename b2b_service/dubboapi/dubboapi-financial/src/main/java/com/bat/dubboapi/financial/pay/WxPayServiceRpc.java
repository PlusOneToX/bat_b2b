package com.bat.dubboapi.financial.pay;

import com.bat.dubboapi.financial.pay.dto.WxPayConfigQry;
import com.bat.dubboapi.financial.common.Response;

public interface WxPayServiceRpc {
    Response<WxPayConfigQry> getWxPayClientV3ByDistributorIdAndAppId(Integer distributorId, Short appType , String appId, Short accountType);
}
