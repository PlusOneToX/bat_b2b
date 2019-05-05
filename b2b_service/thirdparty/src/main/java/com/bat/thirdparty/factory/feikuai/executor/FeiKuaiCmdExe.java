package com.bat.thirdparty.factory.feikuai.executor;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.order.delivery.api.OrderDeliveryServiceRpc;
import com.bat.dubboapi.order.order.dto.enmus.FactoryEnum;
import com.bat.dubboapi.order.order.dto.factory.FactoryOrderAddCmd;
import com.bat.dubboapi.order.order.dto.factory.feikuai.FeiKuaiOrderAddCmd;
import com.bat.dubboapi.platform.tenant.dto.data.PlatformTenantDiyFactoryRpcDTO;
import com.bat.dubboapi.system.logistics.api.SystemLogisticsServiceRpc;
import com.bat.dubboapi.system.logistics.dto.LogisticsRpcQry;
import com.bat.dubboapi.system.logistics.dto.data.LogisticsRpcDTO;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.config.ConfigQry;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.factory.common.service.CommonFactoryServiceImpl;
import com.bat.thirdparty.factory.common.service.dto.OrderDeliveryCommonResp;
import com.bat.thirdparty.factory.dto.FactoryDeliverOrderReq;
import com.bat.thirdparty.factory.enums.DFStatusEnum;
import com.bat.thirdparty.factory.feikuai.dto.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.bat.thirdparty.factory.executor.ErrorCode.B_FACTORY_ERROR;

@Slf4j
@Component
public class FeiKuaiCmdExe {

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderDeliveryServiceRpc orderDeliveryServiceRpc;
    @DubboReference(check = false, timeout = 10000, retries = 0)
    private SystemLogisticsServiceRpc systemLogisticsServiceRpc;

    @Resource
    private CommonFactoryServiceImpl commonFactoryService;

    private final org.apache.commons.codec.binary.Base64 encoder = new Base64();

    @Resource
    private ConfigQry configQry;

    @Resource
    private HttpUtil httpUtil;

    /**
     * 获取签名数据
     * @param kv
     * @param secretKey
     * @return
     */
    private String getSign(Map<String,String> kv, String secretKey){

        List<Map.Entry<String, String>> list = sortHashKeyAsc(kv);
        StringBuilder source = new StringBuilder(secretKey);
        list.forEach(entry ->{
            source.append(entry.getKey()).append(entry.getValue());
        });

        StringBuilder sign = new StringBuilder();
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.toString().getBytes(StandardCharsets.UTF_8));
            for (byte aByte : bytes) {

                String hex = Integer.toHexString(aByte & 255);
                if (hex.length() == 1) {

                    sign.append("0");
                }
                sign.append(hex.toUpperCase());
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sign.toString();
    }

    /**
     * 按字母排序
     * @param kv
     * @return
     */
    public static List<Map.Entry<String, String>> sortHashKeyAsc(Map<String, String> kv) {

        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(kv.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2){
                // 升序排序
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return list;
    }

    /**
     * 同步工厂订单
     * @param addCmd
     * @return
     */
    public String syncOrder(FactoryOrderAddCmd addCmd) {
        FactoryEnum factoryEnum = addCmd.getFactoryEnum();
        String factoryNo = factoryEnum.name().toUpperCase();
        FeiKuaiOrderAddCmd feiKuaiOrderAddCmd = addCmd.getFeiKuaiOrderAddCmd();
        PlatformTenantDiyFactoryRpcDTO data = configQry.getTenantFactoryConfig(factoryNo);
        Map<String, String> configMap = JSONObject.parseObject(data.getConfigOther(), Map.class);
        // 处理商户信息
        feiKuaiOrderAddCmd.getTrade().setPlatform(Short.valueOf(configMap.get("platform")));
        feiKuaiOrderAddCmd.getTrade().setUser_id(Integer.valueOf(configMap.get("user_id")));
        feiKuaiOrderAddCmd.getTrade().setSeller_nick(data.getShopName());
        feiKuaiOrderAddCmd.getBarcode().forEach(barcode -> {
            barcode.setTo_user_id(Integer.valueOf(configMap.get("dd_user_id")));
            barcode.setPlatform(Short.valueOf(configMap.get("platform")));
        });
        Map<String, String> postParamMap = getPostParamMap(feiKuaiOrderAddCmd, configMap, data.getAppId(), data.getAppSecret());
        log.info("飞快同步订单参数:{}", JSONObject.toJSONString(postParamMap));
        HttpHeaders headers = getHttpHeaders();
        FeiKuaiAddOrderResp result = httpUtil.requestFor(data.getOrderAddUrl(), HttpMethod.POST, headers, postParamMap, FeiKuaiAddOrderResp.class);
//        String result = httpUtil.requestFor(data.getOrderAddUrl(), HttpMethod.POST, headers, postParamMap, String.class);
        log.info("飞快同步订单结果:{}", JSONObject.toJSONString(result));
//        return result;
        if (result.getCode() == 0 && result.getResponseData() != null) {
            return result.getResponseData().getTid();
        } else {
            String msg = result.getErrMessage();
            if (StringUtils.isEmpty(msg)) {
                msg = result.getMsg();
            }
            if (StringUtils.isNotEmpty(msg) && msg.equals("订单已提交过") && result.getResponseData() != null) {
                return result.getResponseData().getTid();
            }
            throw ThirdPartyException.buildException(String.valueOf(result.getCode()), msg);
        }
    }

    private HttpHeaders getHttpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    /**
     * 组装参数
     * @param addCmd
     * @return
     */
    @SneakyThrows
    private Map<String,String> getPostParamMap(Object addCmd,Map<String, String> configMap,String app_id,String appSecret){

        Map<String,Object> shardView =new HashMap<>();
        Map<String,Object> postParam =new HashMap<>();

        shardView.put("shardKeySchema",configMap.get("shardKeySchema"));
        shardView.put("shardKeyTable",configMap.get("shardKeyTable"));
        shardView.put("shardKeyTableNumber",Integer.valueOf(configMap.get("shardKeyTableNumber")));

        postParam.put("req_para",addCmd);
        postParam.put("shardView",shardView);
        log.info(JSONObject.toJSONString(postParam));

        Map<String,String> postParamMap =new HashMap<>();
        postParamMap.put("jsonStringParameter",JSONObject.toJSONString(postParam));
        postParamMap.put("time", String.valueOf(System.currentTimeMillis()/1000));
        postParamMap.put("bizID",configMap.get("bizID"));
        postParamMap.put("userID",configMap.get("userID"));
        postParamMap.put("groupID",configMap.get("groupID"));
        postParamMap.put("app_id",app_id);
        postParamMap.put("sign",getSign(postParamMap, appSecret));
        postParamMap.put("jsonStringParameter", URLEncoder.encode(encoder.encodeBase64String(postParamMap.get("jsonStringParameter").getBytes(StandardCharsets.UTF_8))));
        postParamMap.remove("app_id");
        log.info(JSONObject.toJSONString(postParam));

        return postParamMap;
    }

    /**
     * 取消工厂订单
     * @param factoryCode
     * @param orderNo
     * @return
     */
    public Boolean cancelOrderToFactory(String factoryCode, String orderNo) {
        FeiKuaiCancelOrderReq req = new FeiKuaiCancelOrderReq();
        FeiKuaiCancelOrderReq.OrderList order =new FeiKuaiCancelOrderReq.OrderList();
        order.setTid(orderNo);
        PlatformTenantDiyFactoryRpcDTO data = configQry.getTenantFactoryConfig(factoryCode);
        Map<String, String> configMap = JSONObject.parseObject(data.getConfigOther(), Map.class);
        order.setPlatform(Short.valueOf(configMap.get("platform")));
        order.setUser_id(Integer.valueOf(configMap.get("user_id")));
        req.getOrderList().add(order);
        Map<String, String> postParamMap = getPostParamMap(req, configMap, data.getAppId(), data.getAppSecret());
        log.info("飞快同步取消订单参数:{}", JSONObject.toJSONString(postParamMap));
        HttpHeaders headers = getHttpHeaders();
        FeiKuaiCancelOrderResp result = httpUtil.requestFor(data.getOrderCancelUrl(), HttpMethod.POST, headers, postParamMap, FeiKuaiCancelOrderResp.class);
        log.info("飞快同步取消订单结果:{}", JSONObject.toJSONString(result));
        if (result.getCode() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 同步物流信息
     * @param factoryCode
     * @param orderNo
     */
    public void synchronizedLogisticsByOrderID(String factoryCode, String orderNo) {
        FeiKuaiExpressNumberReq req = new FeiKuaiExpressNumberReq();
        FeiKuaiExpressNumberReq.OrderList order =new FeiKuaiExpressNumberReq.OrderList();
        order.setTid(orderNo);
        PlatformTenantDiyFactoryRpcDTO data = configQry.getTenantFactoryConfig(factoryCode);
        Map<String, String> configMap = JSONObject.parseObject(data.getConfigOther(), Map.class);
        order.setPlatform(Short.valueOf(configMap.get("platform")));
        order.setUser_id(Integer.valueOf(configMap.get("user_id")));
        req.getOrderList().add(order);
        Map<String, String> postParamMap = getPostParamMap(req, configMap, data.getAppId(), data.getAppSecret());
        log.info("飞快同步物流参数:{}", JSONObject.toJSONString(postParamMap));
        HttpHeaders headers = getHttpHeaders();
        FeiKuaiExpressResp result = httpUtil.requestFor(configMap.get("deliverQueryUrl"), HttpMethod.POST, headers, postParamMap, FeiKuaiExpressResp.class);
        log.info("飞快同步物流结果:{}", JSONObject.toJSONString(result));
        if(result.getCode() == 0){
            List<FeiKuaiExpressResp.ResponseData> responseData = result.getResponseData();
            responseData.forEach(expressData->{
                if(expressData.getDFStatus().equals(DFStatusEnum.STATUS_4.getValue()) && StringUtils.isNotEmpty(expressData.getCpNum())){
                    // 根据快递名称查询快递编码
                    LogisticsRpcQry logisticsRpcQry = new LogisticsRpcQry();
                    // 根据快递公司名称去查询
                    logisticsRpcQry.setLogisticsFactoryId(expressData.getCpCode());
                    LogisticsRpcDTO logisticsRpcDTO = null;
                    List<LogisticsRpcDTO> logisticsRpcDTOList =
                            systemLogisticsServiceRpc.listLogisticsFromRpcByParams(logisticsRpcQry).getData();
                    if (ObjectUtils.isNotEmpty(logisticsRpcDTOList)) {
                        logisticsRpcDTO = logisticsRpcDTOList.get(0);
                    }
                    // 手动同步物流需要的参数
                    FactoryDeliverOrderReq orderResp = new FactoryDeliverOrderReq();
                    OrderDeliveryCommonResp systemResp = new OrderDeliveryCommonResp();
                    systemResp.setExpressNo(expressData.getCpNum());
                    if(logisticsRpcDTO != null){
                        systemResp.setExpressCode(logisticsRpcDTO.getLogisticsKdnCode());
                        systemResp.setExpressName(logisticsRpcDTO.getName());
                    }else {
                        systemResp.setExpressCode(expressData.getCpCode());
                        systemResp.setExpressName(expressData.getCpCode());
                    }
                    systemResp.setOrderNo(orderNo);
                    orderResp.setSystemResp(systemResp);
                    try {
                        // 手动同步物流
                        commonFactoryService.deliverOrder(orderResp);
                    } catch (Exception e) {
                        throw  ThirdPartyException.buildException(B_FACTORY_ERROR, e.getMessage());
                    }
                }else {
                    throw  ThirdPartyException.buildException(B_FACTORY_ERROR, DFStatusEnum.getName(Integer.valueOf(expressData.getDFStatus())));
                }
            });
        }else {
            String msg = result.getErrMessage();
            if (StringUtils.isEmpty(msg)) {
                msg = result.getMsg();
            }
            throw ThirdPartyException.buildException(String.valueOf(result.getCode()), msg);
        }
    }


}
