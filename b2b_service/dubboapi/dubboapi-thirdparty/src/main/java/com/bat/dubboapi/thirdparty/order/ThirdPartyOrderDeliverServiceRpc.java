package com.bat.dubboapi.thirdparty.order;

import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.order.dto.OrderLogistics;

public interface ThirdPartyOrderDeliverServiceRpc {

    /**
     * 同步物流信息到第三方服务
     * @param orderId
     * @param otherOrderNo
     * @param distributionName
     * @param logistics
     * @param distributionId 配送方式id
     * @param platform
     * @return
     */
    Response<Short> syncLogisticsToThirdParty(Integer orderId, String otherOrderNo, String distributionName, OrderLogistics logistics,
                                  Integer distributionId,String platform);


}
