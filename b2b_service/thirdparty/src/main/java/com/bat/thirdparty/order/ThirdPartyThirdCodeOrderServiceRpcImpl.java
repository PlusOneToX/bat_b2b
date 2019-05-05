package com.bat.thirdparty.order;

import java.util.Date;

import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.common.util.IdWorker;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.thirdparty.order.api.dto.thirdcode.AishideConstant;
import com.bat.thirdparty.order.api.dto.thirdcode.AishideRequest;
import com.bat.thirdparty.order.api.dto.thirdcode.AishideResponse;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.utils.Sign;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.platform.api.DistributorPlatformApiServiceRpc;
import com.bat.dubboapi.distributor.platform.api.DistributorSysPlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.distributor.platform.dto.SysPlatformRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderGoodsThirdCodeRpcDTO;
import com.bat.dubboapi.thirdparty.common.Response;
import com.bat.dubboapi.thirdparty.order.ThirdPartyThirdCodeOrderServiceRpc;

@DubboService
public class ThirdPartyThirdCodeOrderServiceRpcImpl implements ThirdPartyThirdCodeOrderServiceRpc {

    private static final Logger logger = LoggerFactory.getLogger(ThirdPartyThirdCodeOrderServiceRpcImpl.class);

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorSysPlatformServiceRpc distributorSysPlatformServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorPlatformApiServiceRpc distributorPlatformApiServiceRpc;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private OrderBusinessLogServiceI orderReceiveLogServiceI;

    @Override
    public Response writeOffCode(Integer distributorId, String platform, String code, Integer orderId) {
        try {
            com.bat.dubboapi.distributor.common.Response<SysPlatformRpcDTO> sysPlatformResponse =
                distributorSysPlatformServiceRpc.getByPlatformAndDistributorId(platform, distributorId);
            logger.info("获取到的分销商平台信息:{}", JSONObject.toJSONString(sysPlatformResponse));

            com.bat.dubboapi.distributor.common.Response<DistributorPlatformApiRpcDTO> distributorPlatformResponse =
                distributorPlatformApiServiceRpc.getByDistributorIdAndApiTypeAndPlatform(distributorId, (short)5,
                    platform);
            logger.info("获取到的分销商平台信息api:{}", JSONObject.toJSONString(distributorPlatformResponse));

            SysPlatformRpcDTO sysPlatform = sysPlatformResponse.getData();
            DistributorPlatformApiRpcDTO distributorPlatformApi = distributorPlatformResponse.getData();

            AishideRequest aishideRequest = new AishideRequest();
            AishideRequest.RequestBody body = new AishideRequest.RequestBody();
            IdWorker idWorker = new IdWorker();
            body.setReqId(String.valueOf(idWorker.nextId()));
            body.setFlowId(code);
            body.setStatus(AishideConstant.SUCCESS_STATUS);
            aishideRequest.setData(JSONObject.toJSONString(body));
            aishideRequest.setApiname(AishideConstant.API_NAME);
            aishideRequest.setSign(Sign.HMACSHA256(JSONObject.toJSONString(body), sysPlatform.getAppSecret()));
            aishideRequest.setChannelid(sysPlatform.getAppId());
            AishideResponse aishideResponse = null;
            try {
                logger.info("开始对订单核销:{}", JSONObject.toJSONString(aishideRequest));
                aishideResponse = httpUtil.requestFor(sysPlatform.getHostName() + distributorPlatformApi.getUri(),
                    HttpMethod.POST, aishideRequest, AishideResponse.class);
                logger.info("订单核销返回:{}", JSONObject.toJSONString(aishideResponse));
            } catch (Exception e) {
                sendLoger(distributorId, platform, JSONObject.toJSONString(aishideRequest),
                    ThirdCommonConstant.API_REQUEST_FAIL, "核销出现异常:" + JSONObject.toJSONString(e),
                    JSONObject.toJSONString(aishideResponse));
            }
            if (aishideResponse == null || !aishideResponse.getSuccess()) {
                messageSendService.oredrLogPackage(orderId, "兑换码核销", "核销失败:" + JSONObject.toJSONString(aishideResponse),
                    JSONObject.toJSONString(aishideRequest));
                sendLoger(distributorId, platform, JSONObject.toJSONString(aishideRequest),
                    ThirdCommonConstant.API_REQUEST_FAIL, "核销失败:" + JSONObject.toJSONString(aishideResponse),
                    JSONObject.toJSONString(aishideResponse));
                return Response.buildFailure(ThirdCommonErrorCode.COMMON_OPERATE_FAIL,
                    MessageUtils.get(ThirdCommonErrorCode.COMMON_OPERATE_FAIL));
            }
            messageSendService.oredrLogPackage(orderId, "兑换码核销", "核销成功:" + JSONObject.toJSONString(aishideResponse),
                JSONObject.toJSONString(aishideRequest));
            sendLoger(distributorId, platform, JSONObject.toJSONString(aishideRequest),
                ThirdCommonConstant.API_REQUEST_SUCCESS, "核销成功:" + JSONObject.toJSONString(aishideResponse),
                JSONObject.toJSONString(aishideResponse));
            return Response.buildSuccess();
        } catch (Exception e) {
            OrderGoodsThirdCodeRpcDTO dto = new OrderGoodsThirdCodeRpcDTO();
            dto.setCode(code);
            dto.setDistributorId(distributorId);
            dto.setPlatform(platform);
            dto.setOrderId(orderId);
            messageSendService.oredrLogPackage(orderId, "兑换码核销", "核销出现异常:" + JSONObject.toJSONString(e),
                JSONObject.toJSONString(dto));
            logger.error("核销出现异常:{}", e);
            return Response.buildFailure(ThirdCommonErrorCode.SYSTEM_EXCEPTION,
                MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
        }
    }

    private void sendLoger(Integer distributorId, String platform, String requestParamJson, short status,
        String errorMsg, String businessData) {
        try {
            OrderBusinessLogDO pushLogDO = new OrderBusinessLogDO();
            pushLogDO.setLogType(OrderBusinessLogEnum.PUSH_WRITE_OFF_TO_THIRD.getLogType());
            pushLogDO.setTowardType(OrderBusinessLogEnum.PUSH_WRITE_OFF_TO_THIRD.getTowardType());
            pushLogDO.setDistributorId(distributorId);
            pushLogDO.setPlatform(platform);
            pushLogDO.setRequestParamJson(requestParamJson);
            pushLogDO.setCreateTime(new Date());
            pushLogDO.setStatus(status);
            pushLogDO.setErrorMsg(errorMsg);
            pushLogDO.setBusinessData(businessData);
            orderReceiveLogServiceI.create(pushLogDO);
        } catch (Exception e) {
            logger.error("插入第三方日志出现异常:{}", e);
        }
    }

}
