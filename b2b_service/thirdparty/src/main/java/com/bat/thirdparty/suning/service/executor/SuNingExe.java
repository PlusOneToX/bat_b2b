package com.bat.thirdparty.suning.service.executor;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.suning.api.dto.OrderDispatchCmd;
import com.bat.thirdparty.suning.common.SuNingConstant;
import com.bat.thirdparty.suning.request.OrderSignInRequest;
import com.bat.thirdparty.suning.response.OrderDestroryResponse;
import com.bat.thirdparty.suning.response.OrderDispatchResponse;
import com.bat.thirdparty.suning.response.OrderSignInResponse;
import com.bat.thirdparty.suning.utils.DateUtils;
import com.bat.thirdparty.suning.utils.RequestUtil;
import com.bat.thirdparty.suning.utils.SuNingEncryptUtils;
import com.bat.thirdparty.quanyi.api.QuanyiServiceI;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.suning.request.CommonUrlRequest;
import com.bat.thirdparty.suning.request.OrderDestroyRequest;
import com.bat.thirdparty.suning.request.OrderDispatchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class SuNingExe {

    @Value("${suning.app.key}")
    private String suNingAppKey;

    @Value("${suning.secret}")
    private String suNingSecret;

    @Value("${suning.url}")
    private String suNingUrl;

    @Value("${suning.pg.code}")
    private String suNingPgCode;

    @Value("${suning.pg.name}")
    private String suNingPgName;

    @Value("${suning.pg.phone}")
    private String suNingPgPhone;

    @Value("${suning.spot.wxcs}")
    private String suNingSpotWxcs;

    @Value("${suning.spot.wxcs.dec}")
    private String suNingSpotWxcsDec;

    @Autowired
    private QuanyiServiceI quanyiServiceI;

    private static final Logger LOGGER = LoggerFactory.getLogger(SuNingExe.class);

    public boolean checkSign(CommonUrlRequest commonUrlRequest, String json) {
        if (!commonUrlRequest.getApp_key().equals(suNingAppKey)) {
            return false;
        }
        String signStr = commonUrlRequest.getApp_key() + commonUrlRequest.getMethod() + json + suNingSecret;
        String sign = SuNingEncryptUtils.getEncrypt(signStr);
        LOGGER.info("计算出来的签名:{}",sign);
        return commonUrlRequest.getSign().equals(sign);
    }

    public String getUrlEncode(String method, String json) {
        String signStr = suNingAppKey + method + json + suNingSecret;
        String sign = SuNingEncryptUtils.getUrlEncoderEncrypt(signStr);
        LOGGER.info("计算出来的签名(经过url转码):{}",sign);
        return sign;
    }

    public String getSign(String method, String json) {
        String signStr = suNingAppKey + method + json + suNingSecret;
        LOGGER.info("组装后的签名串:{}",signStr);
        String sign = SuNingEncryptUtils.getEncrypt(signStr);
        LOGGER.info("计算出来的签名:{}",sign);
        return sign;
    }

    /**
     * 对苏宁订单进行派工；异步进行
     * @param orderDispatchCmd
     * @return
     */
    @Async
    public void orderDispatchAsync(OrderDispatchCmd orderDispatchCmd) {
        orderDispatch(orderDispatchCmd);
    }

    /**
     * 对苏宁订单进行派工
     * @param orderDispatchCmd
     * @return
     */
    public void orderDispatch(OrderDispatchCmd orderDispatchCmd) {
        try {
            if (StringUtils.isBlank(orderDispatchCmd.getOrderId()) || orderDispatchCmd.getThirdQuanyiId() == null) {
                LOGGER.error("苏宁订单id或者权益id为空无法派工");
                return;
            }
            Thread.sleep(10000);
            ThirdQuanyiDO thirdQuanyiDO = quanyiServiceI.findById(orderDispatchCmd.getThirdQuanyiId());
            if (thirdQuanyiDO == null) {
                LOGGER.error("派工时,找不到对应权益;权益id:{}", orderDispatchCmd.getThirdQuanyiId());
                return;
            }
            if (thirdQuanyiDO.getSignInFlag() != null && thirdQuanyiDO.getSignInFlag() == ThirdCommonConstant.COMMON_FLAG_YES) {
                LOGGER.info("该权益已派工;无需重复处理;权益id:{}", orderDispatchCmd.getThirdQuanyiId());
                return;
            }
            OrderDispatchRequest orderDispatchRequest = new OrderDispatchRequest();
            orderDispatchRequest.setOrderId(orderDispatchCmd.getOrderId());
            long currentTime = System.currentTimeMillis();
            orderDispatchRequest.setZyry1Bp(suNingPgCode);
            orderDispatchRequest.setZyry1BpName(suNingPgName);
            orderDispatchRequest.setZyry1BpTel(suNingPgPhone);
            orderDispatchRequest.setLatestAssignmengtTime(DateUtils.dateStr(new Date(currentTime)));
            //srvTime需要延长一个小时
            currentTime += 60 * 60 * 1000;
            orderDispatchRequest.setSrvTime(DateUtils.dateStr(new Date(currentTime)));
            String jsonStr = JSONObject.toJSONString(orderDispatchRequest);
            String sign = getSign(SuNingConstant.orderDispatchCreate, jsonStr);
            //拼装请求url
            String url = suNingUrl + "?method=" + SuNingConstant.orderDispatchCreate + "&app_key=" + suNingAppKey + "&sign=" + sign;
            LOGGER.info("苏宁派工url:{}", url);
            LOGGER.info("苏宁派工请求参数:{}", jsonStr);
            CommonUrlRequest commonUrlRequest = new CommonUrlRequest();
            commonUrlRequest.setApp_key(suNingAppKey);
            commonUrlRequest.setMethod(SuNingConstant.orderDispatchCreate);
            commonUrlRequest.setSign(sign);
            String result = RequestUtil.sendRequest(commonUrlRequest, jsonStr, suNingUrl);
            LOGGER.info("苏宁派工请求返回:{}", result);
            OrderDispatchResponse response = JSONObject.parseObject(result, OrderDispatchResponse.class);
            //记录请求日志
            quanyiServiceI.addSuningOredrDispatchLog(url, jsonStr, response, orderDispatchCmd);
        } catch (Exception e) {
            LOGGER.error("订单派工出现异常:{}", e);
        }
    }

    /**
     * 苏宁签到
     * @param orderId
     * @param thirdQuanyiId
     * @return
     */
    public OrderSignInResponse signIn(String orderId, Integer thirdQuanyiId) {
            String dateStr = DateUtils.dateStr(new Date());
            OrderSignInRequest orderSignInRequest=new OrderSignInRequest();
            orderSignInRequest.setOrderId(orderId);
            orderSignInRequest.setWorkerBp(suNingPgCode);
            orderSignInRequest.setLatestDoorTime(dateStr);
            orderSignInRequest.setSmWd(SuNingConstant.smwd);
            orderSignInRequest.setSmJd(SuNingConstant.smjd);
            orderSignInRequest.setSmAddress(SuNingConstant.smAddress);
            orderSignInRequest.setSmZsbs(SuNingConstant.smzsbs);
            String jsonStr = JSONObject.toJSONString(orderSignInRequest);
            String sign = getSign(SuNingConstant.orderSignInCreate, jsonStr);
            //拼装请求url
            String url = suNingUrl + "?method=" + SuNingConstant.orderSignInCreate + "&app_key=" + suNingAppKey + "&sign=" + sign;
            LOGGER.info("苏宁签到url:{}", url);
            LOGGER.info("苏宁签到请求参数:{}", jsonStr);
            CommonUrlRequest commonUrlRequest=new CommonUrlRequest();
            commonUrlRequest.setApp_key(suNingAppKey);
            commonUrlRequest.setMethod(SuNingConstant.orderSignInCreate);
            commonUrlRequest.setSign(sign);
            String result = RequestUtil.sendRequest(commonUrlRequest,jsonStr,suNingUrl);
            LOGGER.info("苏宁签到请求返回:{}", result);
            OrderSignInResponse response = JSONObject.parseObject(result, OrderSignInResponse.class);
            quanyiServiceI.signInLog(url,jsonStr,response,thirdQuanyiId);
            return response;
    }

    public OrderDestroryResponse destroy(String thirdCode, Integer thirdQuanyiId, String thirdUserRemark, Integer orderId) {
        String dateStr = DateUtils.dateStr(new Date());
        OrderDestroyRequest orderDestroyRequest=new OrderDestroyRequest();
        orderDestroyRequest.setFromSys(SuNingConstant.fromSys);
        orderDestroyRequest.setObjectId(thirdCode);
        orderDestroyRequest.setPartnerNo(suNingPgCode);
        orderDestroyRequest.setUserStatus(SuNingConstant.userStatus3);
        orderDestroyRequest.setXdsj(dateStr);
        orderDestroyRequest.setXdAddress(SuNingConstant.smAddress);
        orderDestroyRequest.setSrvMemo(thirdUserRemark);
        orderDestroyRequest.setSpotWxcs(suNingSpotWxcs);
        orderDestroyRequest.setSpotWxcsDec(suNingSpotWxcsDec);
        orderDestroyRequest.setSign("");
        orderDestroyRequest.setSjwtList(new ArrayList());
        String jsonStr = JSONObject.toJSONString(orderDestroyRequest);
        String sign = getSign(SuNingConstant.orderDestroyCreate, jsonStr);
        //拼装请求url
        String url = suNingUrl+"?method=" + SuNingConstant.orderDestroyCreate + "&app_key=" + suNingAppKey + "&sign=" + sign;
        LOGGER.info("苏宁核销url:{}", url);
        LOGGER.info("苏宁核销请求参数:{}", jsonStr);
        CommonUrlRequest commonUrlRequest=new CommonUrlRequest();
        commonUrlRequest.setApp_key(suNingAppKey);
        commonUrlRequest.setMethod(SuNingConstant.orderDestroyCreate);
        commonUrlRequest.setSign(sign);
        String result = RequestUtil.sendRequest(commonUrlRequest,jsonStr,suNingUrl);
        LOGGER.info("苏宁核销请求返回:{}", result);
        OrderDestroryResponse response = JSONObject.parseObject(result, OrderDestroryResponse.class);
        quanyiServiceI.destroyAndLog(url,jsonStr,response,thirdQuanyiId,thirdUserRemark,orderId);
        return response;
    }
}
