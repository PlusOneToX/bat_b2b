package com.bat.thirdparty.factory.haixing.service;

import static com.bat.thirdparty.common.error.factory.FactoryErrorCode.B_B2B_ORDER_NO_NOT_NULL;
import static com.bat.thirdparty.factory.enums.FactoryEnum.DuoHong;
import static com.bat.thirdparty.factory.enums.FactoryEnum.LHW;
import static com.bat.thirdparty.factory.executor.ErrorCode.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.annotation.Resource;

import com.bat.thirdparty.factory.haixing.service.dto.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.delivery.dto.OrderDeliveryCmd;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderQueCmd;
import com.bat.dubboapi.order.order.dto.factory.haixing.HaiXingOrderAddCmd;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ConfigQry;
import com.bat.thirdparty.factory.api.FactoryExpandService;
import com.bat.thirdparty.factory.api.FactoryService;
import com.bat.thirdparty.factory.common.service.CommonFactoryServiceImpl;
import com.bat.thirdparty.factory.common.service.dto.OrderDeliveryCommonResp;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.thirdparty.factory.haixing.service.dto.*;
import com.bat.thirdparty.factory.maike.common.MaikeErrorConstant;
import com.bat.thirdparty.tenant.TenantContext;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2019/11/29 15:08
 */
@Service("HaixingFactoryServiceImpl")
@Slf4j
public class HaixingFactoryServiceImpl implements FactoryService, FactoryExpandService {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @Resource
    private CommonFactoryServiceImpl commonFactoryService;

    @Resource
    private ConfigQry configQry;

    public static String getSign(Map<String, Object> params, String secret) throws UnsupportedEncodingException {
        System.out.println("haixing签名前数据:" + params.toString());
        StringBuilder sb = new StringBuilder();
        // step1：先对请求参数排序
        Set<String> keyset = params.keySet();
        TreeSet<String> sortSet = new TreeSet<>(keyset);
        for (String key : sortSet) {
            Object o = params.get(key);
            String encode = URLEncoder.encode(o.toString(), "UTF-8");
            sb.append(key).append("=").append(encode).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(secret);
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
    }

    @SneakyThrows
    @Override
    public String syncOrder(FactoryOrderAddCmd addCmd) {
        FactoryEnum factoryEnum = addCmd.getFactoryEnum();
        String factoryNo = factoryEnum.name().toUpperCase();
        HaiXingOrderAddCmd haiXingOrderAddCmd = addCmd.getHaiXingOrderAddCmd();
        PlatformTenantDiyFactoryRpcDTO data = configQry.getTenantFactoryConfig(factoryNo);
        String dateStr = Long.toString(System.currentTimeMillis() / 1000L);
        String appId = data.getAppId();
        // 海星则为店铺编码
        String shopCode = data.getShopName();
        String appSecret = data.getAppSecret();
        String orderAddUrl = data.getOrderAddUrl();
        haiXingOrderAddCmd.setStoreCode(shopCode);
        HashMap<String, Object> signMap = new HashMap<>();
        signMap.put("appId", appId);
        signMap.put("timestamp", dateStr);
        signMap.put("version", "1");
        signMap.put("method", "createOrder");
        Map map = JSON.parseObject(JSON.toJSONString(haiXingOrderAddCmd), Map.class);
        signMap.putAll(map);
        String sign = getSign(signMap, appSecret);
        signMap.put("sign", sign);
        String jsonString = JSON.toJSONString(signMap);
        log.info("海星同步订单最终请求参数:{}", jsonString);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(orderAddUrl);
        httpPost.setEntity(new StringEntity(jsonString, StandardCharsets.UTF_8));
        try (CloseableHttpResponse execute = httpclient.execute(httpPost)) {
            HttpEntity entity = execute.getEntity();
            String respJson = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            log.info("同步订单返回信息：{}", respJson);
            HaixingAddOrderResp resp = JSON.parseObject(respJson, HaixingAddOrderResp.class);
            if (resp.getSuccess()) {
                HaixingAddOrderResp.Result result = resp.getResult();
                String orderNo = result.getOrderNo();
                Integer orderId = result.getOrderId();
                // 订单发生产
                prodOrder(dateStr, appId, appSecret, orderAddUrl, orderId);
                return orderNo;
            } else {
                if ("24002".equals(resp.getCode()) && "商家单号重复".equals(resp.getMsg())) {
                    return queryOrder(dateStr, appId, appSecret, orderAddUrl, haiXingOrderAddCmd.getSellerOrderNo());
                } else {
                    throw ThirdPartyException.buildException(resp.getCode(), resp.getMsg());
                }
            }
        }
    }

    private String queryOrder(String dateStr, String appId, String appSecret, String orderAddUrl,
        String sellerOrderNo) {
        try {
            HaiXingOrderQry qry = new HaiXingOrderQry();
            qry.setSellerOrderNo(sellerOrderNo);
            HashMap<String, Object> signMap = new HashMap<>();
            signMap.put("appId", appId);
            signMap.put("timestamp", dateStr);
            signMap.put("version", "1");
            signMap.put("method", "queryOrder");
            Map map = JSON.parseObject(JSON.toJSONString(qry), Map.class);
            signMap.putAll(map);
            String sign = getSign(signMap, appSecret);
            signMap.put("sign", sign);
            String jsonString = JSON.toJSONString(signMap);
            log.info("海星查询订单最终请求参数:{}", jsonString);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(orderAddUrl);
            httpPost.setEntity(new StringEntity(jsonString, StandardCharsets.UTF_8));
            try (CloseableHttpResponse execute = httpclient.execute(httpPost)) {
                HttpEntity entity = execute.getEntity();
                String respJson = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                log.info("查询订单返回信息：{}", respJson);
                HaiXingOrderResp resp = JSON.parseObject(respJson, HaiXingOrderResp.class);
                if (resp.getSuccess()) {
                    HaiXingOrderResp.ResultDTO result = resp.getResult();
                    List<HaiXingOrderResp.ResultDTO.RowsDTO> rows = result.getRows();
                    if (!rows.isEmpty()) {
                        return rows.get(0).getOrderNo();
                    }
                    return null;
                } else {
                    throw ThirdPartyException.buildException(resp.getCode(), resp.getMsg());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean prodOrder(String dateStr, String appId, String appSecret, String orderAddUrl, Integer orderId) {
        try {
            HashMap<String, Object> mapSign = new HashMap<>();
            mapSign.put("appId", appId);
            mapSign.put("timestamp", dateStr);
            mapSign.put("version", "1");
            mapSign.put("method", "prodOrder");
            mapSign.put("orderId", orderId);
            mapSign.put("sellerOrderNo", "");
            String prodOrderSign = getSign(mapSign, appSecret);
            mapSign.put("sign", prodOrderSign);
            String jsonString = JSON.toJSONString(mapSign);
            log.info("海星发生产最终请求参数:{}", jsonString);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(orderAddUrl);
            httpPost.setEntity(new StringEntity(jsonString, StandardCharsets.UTF_8));
            try (CloseableHttpResponse execute = httpclient.execute(httpPost)) {
                HttpEntity entity = execute.getEntity();
                String respJson = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                log.info("发生产返回信息：{}", respJson);
                HaixingAddOrderResp resp = JSON.parseObject(respJson, HaixingAddOrderResp.class);
                if (resp.getSuccess()) {
                    log.info("订单：{}，发生产成功", orderId);
                    return true;
                } else {
                    log.error("订单：{}，发生产失败", orderId);
                    throw ThirdPartyException.buildException(resp.getCode(), resp.getMsg());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deliverOrder(FactoryDeliverOrderReq resp) {
        HaixingDeliverOrderResp orderResp = resp.getHaixingResp();
        HaixingExpressReq request = JSON.parseObject(orderResp.getData(), HaixingExpressReq.class);
        // 其实没有验签
        log.info("验签后的物流信息 json :{}", JSON.toJSONString(request));
        if (StringUtils.isBlank(request.getSellerOrderNo())) {
            throw ThirdPartyException.buildException(B_B2B_ORDER_NO_NOT_NULL, "订单编号不能为空");
        }
        OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
        orderDeliveryCmd.setExpressCode(request.getLcCode());
        orderDeliveryCmd.setExpressName(request.getLcName());
        orderDeliveryCmd.setExpressNo(request.getLcOrderno());
        orderDeliveryCmd.setOrderNo(request.getOrderNo());
        orderDeliveryCmd.setExpressTime(null);
        com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
            orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
        log.info("海星发货、返回{}", JSON.toJSONString(responseBean));
        if (responseBean.getCode() != 0) {
            // 需要判断是否bat
            if (700012 - responseBean.getCode() == 0) {
                log.info("海星发货转发到bat租户执行");
                ResponseBaseBean responseBaseBean = DeliverOrder(request);
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

    private ResponseBaseBean DeliverOrder(HaixingExpressReq request) {
        log.info("租户切换");
        TenantContext.removeTenantNo();
        TenantContext.setTenantNo("101");
        log.info("切换后租户编码:{}", TenantContext.getTenantNo());
        try {
            OrderDeliveryCmd orderDeliveryCmd = new OrderDeliveryCmd();
            orderDeliveryCmd.setExpressCode(request.getLcCode());
            orderDeliveryCmd.setExpressName(request.getLcName());
            orderDeliveryCmd.setExpressNo(request.getLcOrderno());
            orderDeliveryCmd.setOrderNo(request.getOrderNo());
            orderDeliveryCmd.setExpressTime(null);
            com.bat.dubboapi.order.common.ResponseBaseBean responseBean =
                orderDeliveryServiceRpc.deliverOrder(orderDeliveryCmd);
            log.info("bat租户-海星发货、返回{}", JSON.toJSONString(responseBean));
            if (responseBean.getCode() == 0) {
                return ResponseBaseBean.responseBean();
            }
            log.info("bat租户-海星发货失败、返回{}", JSON.toJSONString(responseBean));
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, responseBean.getMsg());
        } catch (Exception e) {
            log.error("bat租户-海星发货失败", e);
            return new ResponseBaseBean(MaikeErrorConstant.DeliveryErrorCode, e.getMessage());
        } finally {
            TenantContext.removeTenantNo();
        }
    }

    /**
     * <h2>按订单 ID 同步物流</h2>
     *
     * @param orderCmd
     * @return
     */
    @Override
    public Response synchronizedLogisticsByOrderID(FactoryOrderQueCmd orderCmd) {
        if (ObjectUtils.isEmpty(orderCmd) || ObjectUtils.isEmpty(orderCmd.getFactoryEnum())
            || ObjectUtils.isEmpty(orderCmd.getOrderFactoryNo())) {
            return Response.buildFailure(THE_FACTORY_INFORMATION_FOR_THE_ORDER_IS_MISSING, "工厂订单信息暂未发现");
        }
        FactoryEnum factoryEnum = orderCmd.getFactoryEnum();
        String factoryNo = factoryEnum.name().toUpperCase();
        // HaiXingOrderQueCmd haiXingOrderQueCmd = orderCmd.getHaiXingOrderQueCmd();
        // 查询工厂信息
        PlatformTenantDiyFactoryRpcDTO data = configQry.getTenantFactoryConfig(factoryNo);
        // 手动同步联辉王的物流信息
        String dateStr = Long.toString(System.currentTimeMillis() / 1000L);
        String appId = data.getAppId();
        // 海星则为店铺编码
        String appSecret = data.getAppSecret();
        String orderAddUrl = data.getOrderAddUrl();
        // 海星订单号
        String orderNo = orderCmd.getOrderFactoryNo();
        HaiXingOrderResp.ResultDTO.RowsDTO rowsDTO =
            queryOrderDesc(dateStr, appId, appSecret, orderAddUrl, orderNo);

        if (ObjectUtils.isEmpty(rowsDTO)) {
            return Response.buildFailure(B_FACTORY_ERROR, "该订单的工厂编号不存在或错误");
        }
        HaiXingOrderResp.ResultDTO.RowsDTO.GoodsDTO goodsDTO = rowsDTO.getGoods().get(0);

        if (ObjectUtils.isEmpty(goodsDTO.getLcName()) && ObjectUtils.isEmpty(goodsDTO.getLcOrderno())) {
            return Response.buildFailure(NO_COURIER_INFORMATION, "暂无快递信息");
        }

        // 根据快递名称查询快递编码
        LogisticsRpcQry logisticsRpcQry = new LogisticsRpcQry();
        // 根据快递公司名称去查询
        logisticsRpcQry.setName(goodsDTO.getLcName().toString().replace("快递", ""));
        LogisticsRpcDTO logisticsRpcDTO = null;
        List<LogisticsRpcDTO> logisticsRpcDTOList =
            systemLogisticsServiceRpc.listLogisticsFromRpcByParams(logisticsRpcQry).getData();
        if (ObjectUtils.isNotEmpty(logisticsRpcDTOList)) {
            logisticsRpcDTO = logisticsRpcDTOList.get(0);
        }
        // 手动同步物流需要的参数
        FactoryDeliverOrderReq orderResp = new FactoryDeliverOrderReq();
        OrderDeliveryCommonResp systemResp = new OrderDeliveryCommonResp();
        systemResp.setExpressNo(goodsDTO.getLcOrderno().toString());
        systemResp.setExpressCode(logisticsRpcDTO.getLogisticsFactoryId());
        systemResp.setExpressName(goodsDTO.getLcName().toString());
        systemResp.setOrderNo(orderNo);
        orderResp.setSystemResp(systemResp);
        try {
            // 手动同步物流
            commonFactoryService.deliverOrder(orderResp);
            return Response.buildSuccess();
        } catch (Exception e) {
            return Response.buildFailure(B_FACTORY_ERROR, e.getMessage());
        }
    }

    @Override
    public Response factoryTrackingNumber(FactoryOrderQueCmd orderCmd, String expressNo) {

        if (ObjectUtils.isEmpty(orderCmd) || ObjectUtils.isEmpty(orderCmd.getFactoryEnum())
            || ObjectUtils.isEmpty(orderCmd.getOrderFactoryNo())) {
            return Response.buildFailure(THE_FACTORY_INFORMATION_FOR_THE_ORDER_IS_MISSING, "工厂订单信息暂未发现");
        }
        // 如果不是联辉王就不进行工厂订单校验了，因为暂时没有对接其它。
        if (!orderCmd.getFactoryEnum().name().equals(LHW.name())) {
            return Response.buildSuccess();
        }

        FactoryEnum factoryEnum = orderCmd.getFactoryEnum();
        String factoryNo = factoryEnum.name().toUpperCase();

        // 查询工厂信息
        PlatformTenantDiyFactoryRpcDTO data = configQry.getTenantFactoryConfig(factoryNo);

        String dateStr = Long.toString(System.currentTimeMillis() / 1000L);
        String appId = data.getAppId();
        // 海星则为店铺编码
        String appSecret = data.getAppSecret();
        String orderAddUrl = data.getOrderAddUrl();
        // 海星订单号
        String orderNo = orderCmd.getOrderFactoryNo();
        HaiXingOrderResp.ResultDTO.RowsDTO rowsDTO = queryOrderDesc(dateStr, appId, appSecret, orderAddUrl, orderNo);

        if (ObjectUtils.isEmpty(rowsDTO)) {
            return Response.buildFailure(B_FACTORY_ERROR, "该订单的工厂编号不存在或错误");
        }
        HaiXingOrderResp.ResultDTO.RowsDTO.GoodsDTO goodsDTO = rowsDTO.getGoods().get(0);

        if (ObjectUtils.isEmpty(goodsDTO.getLcName()) && ObjectUtils.isEmpty(goodsDTO.getLcOrderno())) {
            return Response.buildFailure(NO_COURIER_INFORMATION, "暂无快递信息");
        }
        // 如果手动输入的订单号和工厂返回的订单号不一致，则说明输错了
        if (!goodsDTO.getLcOrderno().toString().equals(expressNo)) {
            return Response.buildFailure(B_FACTORY_ERROR, "物流信息输入有误");
        }
        return Response.buildSuccess();
    }

    // public static void main(String[] args) {
    // String dateStr = Long.toString(System.currentTimeMillis() / 1000L);
    // String appId = "974000719614125888";
    // String appSecret = "ea8018ed85eacfc18a46a74d39771050";
    // String orderAddUrl = "https://diyopen.i-smartnet.com/open/api.do";
    // String orderNo = "O12019121504977";
    // // String orderNo = "O12019032409518";
    // queryOrderDesc(dateStr, appId, appSecret, orderAddUrl, orderNo);
    // }

    private HaiXingOrderResp.ResultDTO.RowsDTO queryOrderDesc(String dateStr, String appId, String appSecret,
        String orderAddUrl, String orderNo) {
        try {
            HaiXingOrderQry qry = new HaiXingOrderQry();
            qry.setOrderNo(orderNo);
            HashMap<String, Object> signMap = new HashMap<>();
            signMap.put("appId", appId);
            signMap.put("timestamp", dateStr);
            signMap.put("version", "1");
            signMap.put("method", "queryOrder");
            Map map = JSON.parseObject(JSON.toJSONString(qry), Map.class);
            signMap.putAll(map);
            String sign = getSign(signMap, appSecret);
            signMap.put("sign", sign);
            String jsonString = JSON.toJSONString(signMap);
            log.info("海星查询订单最终请求参数:{}", jsonString);
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(orderAddUrl);
            httpPost.setEntity(new StringEntity(jsonString, StandardCharsets.UTF_8));
            try (CloseableHttpResponse execute = httpclient.execute(httpPost)) {
                HttpEntity entity = execute.getEntity();
                String respJson = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                log.info("查询订单返回信息：{}", respJson);
                HaiXingOrderResp resp = JSON.parseObject(respJson, HaiXingOrderResp.class);
                if (resp.getSuccess()) {
                    HaiXingOrderResp.ResultDTO result = resp.getResult();
                    List<HaiXingOrderResp.ResultDTO.RowsDTO> rows = result.getRows();
                    if (ObjectUtils.isNotEmpty(rows)) {
                        return rows.get(0);
                    }
                    return null;
                } else {
                    throw ThirdPartyException.buildException(resp.getCode(), resp.getMsg());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
