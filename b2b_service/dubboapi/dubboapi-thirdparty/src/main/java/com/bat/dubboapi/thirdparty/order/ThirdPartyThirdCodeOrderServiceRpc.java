package com.bat.dubboapi.thirdparty.order;

import com.bat.dubboapi.thirdparty.common.Response;

public interface ThirdPartyThirdCodeOrderServiceRpc {

    /**
     * 兑换码核销
     * @param distributorId
     * @param platform
     * @param code 第三方兑换码
     * @return
     */
    Response writeOffCode(Integer distributorId,String platform,String code,Integer orderId);
}
