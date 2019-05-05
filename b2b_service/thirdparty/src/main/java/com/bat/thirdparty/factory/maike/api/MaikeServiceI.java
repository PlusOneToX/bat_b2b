package com.bat.thirdparty.factory.maike.api;

import com.bat.thirdparty.factory.maike.request.devlivery.ApiOrderDeliveryCallbackModelRequest;
import com.bat.thirdparty.factory.maike.request.order.ApiMaikeOrderCancelModelRequest;
import com.bat.dubboapi.order.order.dto.factory.maike.MaikeOrderAddCmd;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;

public interface MaikeServiceI {

    /**
     * 麦客发货
     * 
     * @param request
     * @param signature
     * @param timestamp
     * @return
     */
    ResponseBaseBean deliverOrder(ApiOrderDeliveryCallbackModelRequest request, String signature, String timestamp);

    /**
     * 麦客取消订单
     * 
     * @param request
     * @param signature
     * @param timestamp
     * @return
     */
    ResponseBaseBean cancelOrderFromFactory(ApiMaikeOrderCancelModelRequest request, String signature,
                                            String timestamp);

    /**
     * 同步工厂、返回工厂单号
     * 
     * @param maikeOrderAddCmd
     * @return
     */
    String syncOrder(MaikeOrderAddCmd maikeOrderAddCmd);

    /**
     * 定时器触发同步工厂
     * 
     * @param manufactor
     *            工厂编码
     */
    void diyOrderSyncToFactory(String manufactor);
}