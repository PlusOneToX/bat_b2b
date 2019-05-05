package com.bat.thirdparty.quanyi.api;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.suning.api.dto.OrderDispatchCmd;
import com.bat.thirdparty.suning.request.CreateOrderRequest;
import com.bat.thirdparty.suning.request.ModifyOrderRequest;
import com.bat.thirdparty.suning.response.*;
import com.bat.dubboapi.thirdparty.quanyi.dto.ThirdQuanyiRpcCmd;
import com.bat.thirdparty.suning.response.*;

public interface QuanyiServiceI {

    /**
     * 添加苏宁订单
     * @param json
     * @param createOrderRequest
     */
    void addSuningOrder(String url, JSONObject json, CreateOrderRequest createOrderRequest, OrderCreateResponse response, OrderDispatchCmd orderDispatchCmd);

    /**
     * 苏宁请求修改订单
     * @param url
     * @param json
     * @param createOrderRequest
     */
    OrderModifyResponse modifyOrder(String url, JSONObject json, ModifyOrderRequest createOrderRequest);

    void modifyOrderLog(String url, JSONObject json, ModifyOrderRequest createOrderRequest,OrderModifyResponse response);

    /**
     * 添加苏宁订单派工日志
     */
    void addSuningOredrDispatchLog(String url, String jsonStr, OrderDispatchResponse response, OrderDispatchCmd orderDispatchCmd);

    /**
     * 签到并添加日志日志
     * @param url
     * @param jsonStr
     * @param response
     */
    void signInLog(String url, String jsonStr, OrderSignInResponse response, Integer thirdQuanyiId);

    /**
     * 核销并添加日志
     * @param url
     * @param jsonStr
     * @param response
     * @param thirdQuanyiId
     * @param thirdUserRemark
     * @param orderId
     */
    void destroyAndLog(String url, String jsonStr, OrderDestroryResponse response, Integer thirdQuanyiId, String thirdUserRemark, Integer orderId);

    /**
     * 核销并添加日志(不经过苏宁核销的情况下)
     * @param thirdQuanyiId
     * @param thirdUserRemark
     * @param orderId
     */
    void destroyAndLog(Integer thirdQuanyiId, String thirdUserRemark, Integer orderId);

    ThirdQuanyiDO findById(Integer thirdQuanyiId);
}
