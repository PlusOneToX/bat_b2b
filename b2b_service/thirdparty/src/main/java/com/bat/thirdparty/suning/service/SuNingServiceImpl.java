package com.bat.thirdparty.suning.service;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.AutoReleaseLock;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.bat.thirdparty.common.ThirdKeyConstant;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.distributor.DistributorConstant;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.quanyi.QuanyiErrorCode;
import com.bat.thirdparty.suning.api.dto.OrderDispatchCmd;
import com.bat.thirdparty.suning.common.SuNingConstant;
import com.bat.thirdparty.suning.request.CreateOrderRequest;
import com.bat.thirdparty.suning.response.*;
import com.bat.thirdparty.suning.service.executor.SuNingExe;
import com.bat.thirdparty.tenant.TenantContext;
import com.bat.dubboapi.distributor.wx.api.WxPlatformServiceRpc;
import com.bat.dubboapi.distributor.wx.dto.WxPlatformRpcDTO;
import com.bat.dubboapi.thirdparty.wx.api.WxServiceRpc;
import com.bat.thirdparty.quanyi.api.QuanyiServiceI;
import com.bat.thirdparty.quanyi.dao.dataobject.ThirdQuanyiDO;
import com.bat.thirdparty.quanyi.service.executor.ThirdQuanyiQryExe;
import com.bat.thirdparty.suning.api.SuNingServiceI;
import com.bat.thirdparty.suning.request.CommonUrlRequest;
import com.bat.thirdparty.suning.request.ModifyOrderRequest;
import com.bat.thirdparty.suning.response.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SuNingServiceImpl implements SuNingServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuNingServiceImpl.class);

    @Autowired
    private SuNingExe suNingExe;

    @Autowired
    private QuanyiServiceI quanyiServiceI;

    @Value("${suning.distributor.id}")
    private Integer suNingDistributorId;

    @Autowired
    private ThirdQuanyiQryExe thirdQuanyiQryExe;

    @CreateCache(name = ThirdKeyConstant.EXCHANGE_QUANYI_PRE)
    private Cache<String, Integer> exchangeQuanyiCache;

    @CreateCache(name = ThirdKeyConstant.EXCHANGE_SU_NING_ORDER_PRE)
    private Cache<String, Integer> exchangeSuNingOrderCache;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private WxPlatformServiceRpc wxPlatformServiceRpc;

    @Resource
    private WxServiceRpc wxServiceRpc;


    @Override
    public OrderCreateResponse createOrder(CommonUrlRequest commonUrlRequest, JSONObject json) {
        CreateOrderRequest createOrderRequest = JSONObject.parseObject(json.toJSONString(), CreateOrderRequest.class);
        LOGGER.info("苏宁参数转化成实体参数:{}", JSONObject.toJSONString(createOrderRequest));
        OrderCreateResponse response = new OrderCreateResponse();
        response.setOrderId(createOrderRequest.getOrderId());
        AutoReleaseLock autoReleaseLock = null;
        try {
            //签名校验
            boolean flag = suNingExe.checkSign(commonUrlRequest, json.toJSONString());
            if (!flag) {
                response.setReturnCode(SuNingConstant.returnCodeF);
                response.setReturnMessage(SuNingConstant.returnSignError);
                return response;
            }
            if(StringUtils.isBlank(createOrderRequest.getOrderId())){
                response.setReturnCode(SuNingConstant.returnCodeF);
                response.setReturnMessage(SuNingConstant.requestOrderNoCantNull);
                return response;
            }
            if(StringUtils.isBlank(createOrderRequest.getSrvcmmdtyCode())){
                response.setReturnCode(SuNingConstant.returnCodeF);
                response.setReturnMessage(SuNingConstant.requestSrvcmmdtyCodeCantNull);
                return response;
            }
            // 分布式锁
            autoReleaseLock =
                    exchangeSuNingOrderCache.tryLock(TenantContext.getTenantNo() + ":" + createOrderRequest.getOrderId(), 10, TimeUnit.SECONDS);
            if (autoReleaseLock == null) {
                LOGGER.error("订单创建加锁失败，服务订单号:{}",createOrderRequest.getOrderId());
                // 加锁
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_ORDER_CREATE_ERROR);
            }
            String url = "?method=" + commonUrlRequest.getMethod() + "&app_key=" + commonUrlRequest.getApp_key() + "&sign=" + commonUrlRequest.getSign();
            response.setReturnCode(SuNingConstant.returnCodeS);
            OrderDispatchCmd orderDispatchCmd = new OrderDispatchCmd();
            quanyiServiceI.addSuningOrder(url, json, createOrderRequest, response, orderDispatchCmd);
            //进行派工（异步进行）
            suNingExe.orderDispatchAsync(orderDispatchCmd);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.setReturnCode(SuNingConstant.returnCodeF);
            response.setReturnMessage(SuNingConstant.returnUnknownError);
            return response;
        }finally {
            try {
                if (autoReleaseLock != null) {
                    autoReleaseLock.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public OrderModifyResponse modifyOrder(CommonUrlRequest commonUrlRequest, JSONObject json) {
        OrderModifyResponse response = new OrderModifyResponse();
        String url="";
        ModifyOrderRequest modifyOrderRequest =new ModifyOrderRequest();
        boolean flag = false;
        modifyOrderRequest = JSONObject.parseObject(json.toJSONString(), ModifyOrderRequest.class);
        response.setServiceNum(modifyOrderRequest.getServiceNum());
        AutoReleaseLock autoReleaseLock = null;
        try {
            //签名校验
            flag = suNingExe.checkSign(commonUrlRequest, json.toJSONString());
            if (!flag) {
                response.setReturnCode(SuNingConstant.returnCodeF);
                response.setReturnMessage(SuNingConstant.returnSignError);
                return response;
            }
            if(StringUtils.isBlank(modifyOrderRequest.getServiceNum())){
                response.setReturnCode(SuNingConstant.returnCodeF);
                response.setReturnMessage(SuNingConstant.requestOrderNoCantNull);
                return response;
            }
            LOGGER.info("苏宁修改订单参数转化成实体参数:{}", JSONObject.toJSONString(modifyOrderRequest));
            url = "?method=" + commonUrlRequest.getMethod() + "&app_key=" + commonUrlRequest.getApp_key() + "&sign=" + commonUrlRequest.getSign();

            ThirdQuanyiDO thirdQuanyiDO = thirdQuanyiQryExe.findByDistributorIdAndThirdCode(suNingDistributorId, modifyOrderRequest.getServiceNum());
            if (thirdQuanyiDO == null) {
                response.setReturnCode(SuNingConstant.returnCodeF);
                response.setReturnMessage(SuNingConstant.orderCantFind);
                return response;
            }
            // 分布式锁
            autoReleaseLock =
                    exchangeQuanyiCache.tryLock(TenantContext.getTenantNo() + ":" + thirdQuanyiDO.getId(), 10, TimeUnit.SECONDS);
            if (autoReleaseLock == null) {
                LOGGER.error("修改订单加锁失败,权益id:{}",thirdQuanyiDO.getId());
                // 加锁
                throw ThirdPartyException.buildException(QuanyiErrorCode.QUANYI_SIGN_ERROR);
            }
            response = quanyiServiceI.modifyOrder(url, json, modifyOrderRequest);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.setReturnCode(SuNingConstant.returnCodeF);
            response.setReturnMessage(SuNingConstant.returnUnknownError);
            return response;
        } finally {
            try {
                //签名通过则增加日志
                if (flag) {
                    quanyiServiceI.modifyOrderLog(url, json, modifyOrderRequest, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (autoReleaseLock != null) {
                    autoReleaseLock.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void orderDispatch(OrderDispatchCmd orderDispatchCmd) {
        suNingExe.orderDispatch(orderDispatchCmd);
    }

    //有事务的情况下新创建事务
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @Override
    public OrderSignInResponse sign(String orderId, Integer thirdQuanyiId) {
       return suNingExe.signIn(orderId,thirdQuanyiId);
    }

    //有事务的情况下新创建事务
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    @Override
    public OrderDestroryResponse destroy(String thirdCode, Integer thirdQuanyiId, String thirdUserRemark, Integer orderId) {
      return suNingExe.destroy(thirdCode,thirdQuanyiId,thirdUserRemark,orderId);
    }

    @Override
    public ActiveBaseResponse errorMsg(String msg) {
        ActiveBaseResponse response = new ActiveBaseResponse();
        response.setReturnCode(SuNingConstant.returnCodeF);
        response.setReturnMessage(msg);
        if (StringUtils.isBlank(msg)) {
            response.setReturnMessage(SuNingConstant.returnUnknownError);
        }
        return response;
    }

    @Override
    public String link(Integer distributorId, String pageUrl) {
        com.bat.dubboapi.distributor.common.Response<List<WxPlatformRpcDTO>> distributorResponse =
                wxPlatformServiceRpc.listByDistributorIdAndType(distributorId,
                        DistributorConstant.WX_PLATFORM_TYPE_PROGRAM);
        if (distributorResponse == null || !distributorResponse.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        List<WxPlatformRpcDTO> platformRpcDTOList = distributorResponse.getData();
        if (platformRpcDTOList == null || platformRpcDTOList.size() == 0) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }

        com.bat.dubboapi.thirdparty.common.Response<String> response = wxServiceRpc.createWechatProgramSchemeLink(platformRpcDTOList.get(0).getAppId(), pageUrl, platformRpcDTOList.get(0).getAppSecret());
        if (response == null || !response.isSuccess() || StringUtils.isBlank(response.getData())) {
            throw ThirdPartyException.buildException(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
        }
        return response.getData();
    }


}
