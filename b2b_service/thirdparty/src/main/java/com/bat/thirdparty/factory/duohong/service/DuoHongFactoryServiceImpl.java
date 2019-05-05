package com.bat.thirdparty.factory.duohong.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bat.thirdparty.factory.duohong.service.dto.DuoHongAddOrderResp;
import com.bat.thirdparty.factory.duohong.service.dto.DuoHongDeliverOrderResp;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliveryCmd;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.duohong.DuoHongOrderAddCmd;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ConfigQry;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.thirdparty.factory.enums.LogisticEnum;
import com.bat.thirdparty.factory.maike.common.MaikeErrorConstant;
import com.bat.thirdparty.factory.utils.Md5Utils;
import com.bat.thirdparty.factory.api.FactoryService;
import com.bat.thirdparty.tenant.TenantContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/26 9:27
 */
@Service("DuoHongFactoryServiceImpl")
@Slf4j
public class DuoHongFactoryServiceImpl implements FactoryService {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @Resource
    private ConfigQry configQry;

    @SneakyThrows
    @Override
    public String syncOrder(FactoryOrderAddCmd addCmd) {
        FactoryEnum factoryEnum = addCmd.getFactoryEnum();
        String factoryNo = factoryEnum.name().toUpperCase();
        DuoHongOrderAddCmd duoHongOrderAddCmd = addCmd.getDuoHongOrderAddCmd();
        PlatformTenantDiyFactoryRpcDTO data = configQry.getTenantFactoryConfig(factoryNo);
        String orderAddUrl = data.getOrderAddUrl();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(orderAddUrl);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(duoHongOrderAddCmd.getTrades());
        String sign = sign(data);
        log.info("多鸿同步订单最终请求参数：{}", json);
        StringEntity requestEntity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(requestEntity);
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("ak", data.getAppKey()));
        nvps.add(new BasicNameValuePair("push_companyId", data.getAppId()));
        nvps.add(new BasicNameValuePair("data", json));
        nvps.add(new BasicNameValuePair("access_token", sign));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
        try (CloseableHttpResponse execute = httpclient.execute(httpPost)) {
            HttpEntity entity = execute.getEntity();
            String respJson = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            // {"rspType":"msg", "status":"success", "msg":"推送成功"}
            // {"rspType":"msg", "status":"fail", "msg":"推送失败，以下订单号已存在不能重复推送(如确认没有重复推送，请修改订单编号长度)：O1637823142473"}
            log.info("多鸿同步订单返回信息：{}", respJson);
            DuoHongAddOrderResp resp = objectMapper.readValue(respJson, DuoHongAddOrderResp.class);
            if ("success".equals(resp.getStatus())) {
                return duoHongOrderAddCmd.getTrades().get(0).getTid();
            } else {
                throw ThirdPartyException.buildException(resp.getStatus(), resp.getMsg());
            }
        }
    }

    public static String sign(PlatformTenantDiyFactoryRpcDTO data) throws IllegalAccessException {
        String appSecret = data.getAppSecret();
        List<String> keys = new ArrayList<>();
        keys.add("ak");
        keys.add("push_companyId");
        keys.add("data");
        log.debug("参与签名的 key：{}", keys);
        String paramsKey = keys.stream().map(String::toUpperCase).sorted().collect(Collectors.joining(","));
        log.debug("排序,拼接 paramsKey：{}", paramsKey);
        String paramsKey2 = appSecret + "," + paramsKey + "," + appSecret;
        log.debug("两边加上appsecret：{}", paramsKey2);
        String encode = Md5Utils.GetMD5Code(paramsKey2);
        log.info("access_token：{}", encode);
        return encode;
    }

    @Override
    public void deliverOrder(FactoryDeliverOrderReq resp) {
        DuoHongDeliverOrderResp request = resp.getDuoHongResp();
        OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
        orderDeliveryCmd.setExpressCode(request.getExpressCode());
        orderDeliveryCmd.setExpressName(LogisticEnum.valueOf(request.getExpressCode()).getLogisticName());
        orderDeliveryCmd.setExpressNo(request.getOutSid());
        orderDeliveryCmd.setOrderNo(request.getTid());
        orderDeliveryCmd.setExpressTime(null);
        com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
            orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
        log.info("多鸿发货、返回{}", JSON.toJSONString(responseBean));
        if (responseBean.getCode() != 0) {
            // 需要判断是否bat
            if (700012 - responseBean.getCode() == 0) {
                log.info("多鸿发货转发到bat租户执行");
                ResponseBaseBean responseBaseBean = DeliverOrder(resp);
                if (responseBaseBean.getCode() != 0) {
                    throw ThirdPartyException.buildException(responseBaseBean.getCode() + "",
                        responseBaseBean.getMsg());
                }
            } else {
                throw ThirdPartyException.buildException(MaikeErrorConstant.DeliveryErrorCode + "",
                    responseBean.getMsg());
            }
        }
    }

    @Override
    public void diyOrderSyncToFactory(String startTime) {

    }

    private ResponseBaseBean DeliverOrder(FactoryDeliverOrderReq resp) {
        log.info("租户切换");
        TenantContext.removeTenantNo();
        TenantContext.setTenantNo("101");
        log.info("切换后租户编码:{}", TenantContext.getTenantNo());
        try {
            DuoHongDeliverOrderResp request = resp.getDuoHongResp();
            OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
            orderDeliveryCmd.setExpressCode(request.getExpressCode());
            orderDeliveryCmd.setExpressName(LogisticEnum.valueOf(request.getExpressCode()).getLogisticName());
            orderDeliveryCmd.setExpressNo(request.getOutSid());
            orderDeliveryCmd.setOrderNo(request.getTid());
            orderDeliveryCmd.setExpressTime(null);
            com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
                orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
            log.info("bat租户-多鸿发货、返回{}", JSON.toJSONString(responseBean));
            if (responseBean.getCode() == 0) {
                return ResponseBaseBean.responseBean();
            }
            log.info("bat租户-多鸿发货失败、返回{}", JSON.toJSONString(responseBean));
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, responseBean.getMsg());
        } catch (Exception e) {
            log.error("bat租户-多鸿发货失败", e);
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, e.getMessage());
        } finally {
            TenantContext.removeTenantNo();
        }
    }
}
