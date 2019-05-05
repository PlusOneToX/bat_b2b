package com.bat.thirdparty.suning.api;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.suning.api.dto.OrderDispatchCmd;
import com.bat.thirdparty.suning.request.CommonUrlRequest;
import com.bat.thirdparty.suning.response.*;
import com.bat.thirdparty.suning.response.*;

public interface SuNingServiceI {

    /**
     * 创建苏宁订单
     * @param commonUrlRequest
     * @return
     */
    OrderCreateResponse createOrder(CommonUrlRequest commonUrlRequest, JSONObject json);


    /**
     * 取消苏宁订单
     * @param commonUrlRequest
     * @return
     */
    OrderModifyResponse modifyOrder(CommonUrlRequest commonUrlRequest, JSONObject json);

    /**
     * 订单派工
     */
    void orderDispatch(OrderDispatchCmd orderDispatchCmd);


    /**
     * 订单签到
     */
    OrderSignInResponse sign(String orderId, Integer thirdQuanyiId);

    /**
     * 订单核销
     */
    OrderDestroryResponse destroy(String thirdCode, Integer thirdQuanyiId, String thirdUserRemark, Integer orderId);


    ActiveBaseResponse errorMsg(String msg);

    /**
     * 获取小程序scheme码
     * @param distributorId
     * @param pageUrl
     * @return
     */
    String link(Integer distributorId, String pageUrl);
}
