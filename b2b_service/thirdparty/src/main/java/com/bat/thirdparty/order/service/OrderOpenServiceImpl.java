package com.bat.thirdparty.order.service;

import static com.bat.thirdparty.Sumsung.constants.Constant.SA_ERROR_CODE_SUCCESS;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.alibaba.taobao.api.dto.TaoBaoHttpRequestDTO;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.Response;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.distributor.ThirdDistributorPlatformApiConstant;
import com.bat.thirdparty.common.enumtype.OrderStatus;
import com.bat.thirdparty.common.enumtype.PayStatus;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdDubboServiceErrorCode;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.common.order.OrderHeaderConstant;
import com.bat.thirdparty.common.util.IdWorker;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.common.util.SHA256withRSAUtils;
import com.bat.thirdparty.common.util.Sha1Handler;
import com.bat.thirdparty.message.service.MessageSendService;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.response.CustomInfoPushResponse;
import com.bat.thirdparty.order.service.convertor.OrderOpenConvertor;
import com.bat.thirdparty.order.service.executor.OrderQryExe;
import com.bat.thirdparty.order.service.validator.OrderValidator;
import com.bat.thirdparty.utils.CommonUtil;
import com.bat.thirdparty.utils.Sign;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.bat.dubboapi.distributor.customer.api.CustomerServiceRpc;
import com.bat.dubboapi.distributor.customer.dto.data.CustomerRpcDTO;
import com.bat.dubboapi.distributor.platform.api.DistributorPlatformApiServiceRpc;
import com.bat.dubboapi.distributor.platform.api.DistributorSysPlatformServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.distributor.platform.dto.SysPlatformRpcDTO;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.dubboapi.flexible.order.dto.OrderGoodsDiyParamDTO;
import com.bat.dubboapi.flexible.third.api.ThirdSkuServiceRpc;
import com.bat.dubboapi.flexible.third.dto.MolejiCaseCmd;
import com.bat.dubboapi.order.cost.api.OrderCustomerCostServiceRpc;
import com.bat.dubboapi.order.cost.dto.OrderCustomerCostRpcQryDTO;
import com.bat.dubboapi.order.order.api.*;
import com.bat.dubboapi.order.order.dto.*;
import com.bat.dubboapi.order.order.dto.data.OrderCustomerDataRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderDTO;
import com.bat.dubboapi.order.order.dto.data.OrderExtendDataSimpleRpcDTO;
import com.bat.dubboapi.order.order.dto.data.OrderGoodsThirdCodeRpcDTO;
import com.bat.dubboapi.order.order.dto.goods.OrderGoodsDetailRpcDTO;
import com.bat.dubboapi.order.order.dto.info.OrderInfoRpcQryDTO;
import com.bat.thirdparty.Sumsung.OrderHttp;
import com.bat.thirdparty.Sumsung.request.OrderRequest;
import com.bat.thirdparty.Sumsung.response.BaseResponse;
import com.bat.thirdparty.order.api.OrderOpenServiceI;
import com.bat.thirdparty.order.api.dto.OrderBaseOnCodeCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.common.AddressQry;
import com.bat.thirdparty.order.api.dto.common.OrderNoResp;
import com.bat.thirdparty.order.api.dto.common.ThirdOrderResp;
import com.bat.thirdparty.order.api.dto.common.UserInfoQry;
import com.bat.thirdparty.order.api.dto.moleji.MolejiOrderCreateCmd;
import com.bat.thirdparty.order.api.dto.provisional.ProvisionalOrderInfo;
import com.bat.thirdparty.order.api.dto.thirdcode.AishideConstant;
import com.bat.thirdparty.order.api.dto.thirdcode.AishideRequest;
import com.bat.thirdparty.order.api.dto.thirdcode.AishideResponse;
import com.bat.thirdparty.order.api.dto.zhaolianji.ZhaoLiangJiCustomInfo;

/**
 * 订单参数转换
 */
@Service
public class OrderOpenServiceImpl implements OrderOpenServiceI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderOpenServiceImpl.class);

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderBusinessLogServiceI orderReceiveLogServiceI;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private DistributorSysPlatformServiceRpc distributorSysPlatformServiceRpc;

    @DubboReference(check = false, timeout = 5000, retries = 0)
    private DistributorPlatformApiServiceRpc distributorPlatformApiServiceRpc;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private OrderOpenConvertor orderOpenConvertor;

    @Autowired
    private OrderBusinessLogServiceI orderBusinessLogServiceI;

    @Autowired
    private MessageSendService messageSendService;

    @DubboReference(check = false, timeout = 6000000, retries = 0)
    private OrderServiceRpc orderServiceRpc;

    @DubboReference(check = false, timeout = 9000, retries = 0)
    private ThirdSkuServiceRpc thirdSkuServiceRpc;

    @Value("${moleji.distributor.id}")
    private Integer molejiDistributorId;

    @Value("${moleji.orderSource}")
    private String molejiOrderSource;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private CustomerServiceRpc customerServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderExtendDataServiceRpc orderExtendDataServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderGoodsDubboServiceRpc orderGoodsDubboServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderInfoServiceRpc orderInfoServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderCustomerDataServiceRpc orderCustomerDataServiceRpc;

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private OrderCustomerCostServiceRpc orderCustomerCostServiceRpc;

    @Value("${sumsung.orderUrl}")
    private String sumsungOrderUrl;

    @Value("${sumsung.orderDetailUrl}")
    private String sumsungOrderDetailUrl;

    @Resource
    private OrderHttp orderHttp;

    @Autowired
    private OrderQryExe orderQryExe;

    @Value("${huawei.platform.no}")
    private String huaweiPlatformNo;

    @Value("${huawei.public.key}")
    private String huaweiPublicKey;

    @Value("${huawei.private.key}")
    private String huaweiPrivateKey;

    /**
     * 下单详情基于Id,
     *
     * @param request
     * @param orderBaseOnIdCmd
     * @param taoBaoHttpRequestDTO
     * @return
     */
    @Override
    public ResponseEntity<Object> createOrderBaseOnId(HttpServletRequest request, OrderBaseOnIdCmd orderBaseOnIdCmd,
                                                      TaoBaoHttpRequestDTO taoBaoHttpRequestDTO, Integer logId) {
        LOGGER.info("基于id接口获取的业务日志id{}", logId);
        OrderBusinessLogDO logDO = new OrderBusinessLogDO();

        // 暂停推单功能，除开苏宁以外的都不能推送进来了
        if (!request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID).equals("58")) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.PAUSE_PUSH_ORDER_FUNCTION);
        }

        if (logDO.getId() == null) {
            logDO.setCreateTime(new Date());
        } else {
            logDO = orderBusinessLogServiceI.getById(logId);
        }
        logDO.setLogType(OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_ID.getLogType());
        logDO.setTowardType(OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_ID.getTowardType());
        orderValidator.validOrderBaseOnIdPush(orderBaseOnIdCmd, request, logDO, taoBaoHttpRequestDTO);
        // 校验是否重复传（之前已推送成功）
        OrderNoResp orderNoResp =
            validSendSuccess(logDO.getDistributorId(), orderBaseOnIdCmd.getOrderNo(), logDO.getLogType());
        if (orderNoResp == null) {
            orderNoResp = new OrderNoResp();
        } else {
            LOGGER.info(orderBaseOnIdCmd.getOrderNo() + "第三方订单重复推送（之前推送已成功）、返回{}", JSON.toJSONString(orderNoResp));
            return ResponseEntity.ok(orderNoResp);
        }
        String errorMsg = null;
        try {

            // 参数转换
            OrderInfoCmd orderInfoCmd = orderOpenConvertor.toOrderInfoBaseId(orderBaseOnIdCmd, logDO);
            LOGGER.info("第三方（基于ID）推送订单参数:{}", JSON.toJSONString(orderInfoCmd));
            try {
                com.bat.dubboapi.order.common.Response<List<OrderDTO>> distributorOrderResp =
                    orderServiceRpc.createDistributorOrder(orderInfoCmd);
                LOGGER.info("第三方（基于ID）推送订单返回:{}", JSON.toJSONString(distributorOrderResp));
                if (distributorOrderResp == null) {
                    throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
                }
                if (!distributorOrderResp.isSuccess()) {
                    throw new ThirdPartyOpenApiException(distributorOrderResp.getErrMessage());
                }
                logDO.setStatus(ThirdCommonConstant.API_REQUEST_SUCCESS);
                OrderDTO orderDTO = distributorOrderResp.getData().get(0);
                logDO.setOrderNo(orderDTO.getOrderNo());
                orderNoResp.setOrderId(orderDTO.getId());
                // 历史原因、基于ID和基于编码不一样（ID返回的是bat的编码、基于编码返回的是第三方的订单号）
                orderNoResp.setOrderNo(orderDTO.getOrderNo());
                orderNoResp.setThirdOrderNo(orderBaseOnIdCmd.getOrderNo());
                orderNoResp.setorderNo(orderDTO.getOrderNo());
                orderNoResp.setCode(200);
                orderNoResp.setUserMsg("推送成功");
                orderNoResp.setMsg("SUCCESS");
                OrderNoResp.OrderData data = new OrderNoResp.OrderData();
                org.springframework.beans.BeanUtils.copyProperties(orderNoResp, data);
                // 保持一致
                orderNoResp.setData(data);
                logDO.setResponseMsg(JSON.toJSONString(orderNoResp));
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("提交第三方订单（基于ID）出现异常：{}", e.getMessage());
                throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            }
        } catch (ThirdPartyOpenApiException e) {
            e.printStackTrace();
            LOGGER.error("第三方对外订单接口订单确认、基于id对接,出现异常,{}", e);
            logDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(MessageUtils.get(errorMsg))) {
                errorMsg = MessageUtils.get(errorMsg);
            }
            logDO.setErrorMsg(errorMsg);
            orderNoResp.setCode(20020);
            orderNoResp.setUserMsg("推送失败:" + errorMsg);
            orderNoResp.setMsg(e.getMessage());
            logDO.setResponseMsg(JSON.toJSONString(orderNoResp));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("第三方对外订单接口基于ID订单确认、出现异常,{}", e.getMessage());
            errorMsg = MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION);
            logDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            orderNoResp.setCode(20020);
            orderNoResp.setUserMsg("推送失败:" + errorMsg);
            orderNoResp.setMsg("FAIL");
            logDO.setErrorMsg(errorMsg);
            logDO.setResponseMsg(JSON.toJSONString(orderNoResp));
        } finally {
            orderReceiveLogServiceI.save(logDO);
            if (StringUtils.isNotBlank(errorMsg)) {
                throw new ThirdPartyOpenApiException(errorMsg);
            }

            return ResponseEntity.ok(orderNoResp);
        }

    }

    /**
     * 校验是否重复推送
     * 
     * @param distributorId
     * @param otherOrderNo
     * @param logType
     * @return
     */
    private OrderNoResp validSendSuccess(Integer distributorId, String otherOrderNo, Short logType) {
        OrderNoResp orderNoResp = new OrderNoResp();
        List<OrderBusinessLogDO> orderBusinessLogDOList = orderBusinessLogServiceI.listByCondition(logType, null, null,
            ThirdCommonConstant.API_REQUEST_SUCCESS, null, otherOrderNo, null, null, distributorId);
        if (orderBusinessLogDOList == null || orderBusinessLogDOList.size() == 0) {
            return null;
        }
        OrderBusinessLogDO orderBusinessLogDO = orderBusinessLogDOList.get(0);
        orderNoResp.setCode(200);
        orderNoResp.setMsg("SUCCESS");
        orderNoResp.setUserMsg("订单重复推送、返回之前推送成功的单号");
        try {
            OrderInfoRpcQryDTO orderInfoRpcQryDTO = orderQryExe.getOrderInfoByOrderNo(orderBusinessLogDO.getOrderNo());
            orderNoResp.setOrderId(orderInfoRpcQryDTO.getId());
            orderNoResp.setorderNo(orderBusinessLogDO.getOrderNo());
            orderNoResp.setThirdOrderNo(otherOrderNo);
            orderNoResp.setOrderNo(orderInfoRpcQryDTO.getOrderNo());
            if (OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_CODE.getLogType().equals(logType)) {
                // 编码返回的是第三方编码
                orderNoResp.setOrderNo(otherOrderNo);
            }
            OrderNoResp.OrderData data = new OrderNoResp.OrderData();
            BeanUtils.copyProperties(orderNoResp, data);
            orderNoResp.setData(data);
        } catch (ThirdPartyException e) {
            e.printStackTrace();
            LOGGER.error("查询第三方重复推单异常{}", e.getMsg());
            throw new ThirdPartyOpenApiException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询第三方重复推单系统异常{}", e.getMessage());
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
        }
        return orderNoResp;
    }

    /**
     * 下单详情基于编码
     *
     * @param orderBaseOnCodeCmd
     * @param request
     * @return
     */

    @Override
    public ResponseEntity<Object> createOrderBaseOnCode(OrderBaseOnCodeCmd orderBaseOnCodeCmd,
        HttpServletRequest request, Integer logId) {
        OrderBusinessLogDO orderBusinessLogDO = new OrderBusinessLogDO();
        orderBusinessLogDO.setId(logId);
        if (orderBusinessLogDO.getId() == null) {
            orderBusinessLogDO.setCreateTime(new Date());
        }
        orderBusinessLogDO.setLogType(OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_CODE.getLogType());
        orderBusinessLogDO.setTowardType(OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_CODE.getTowardType());

        // 暂停推单功能，除开苏宁以外的都不能推送进来了
        if (!request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID).equals("58")) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.PAUSE_PUSH_ORDER_FUNCTION);
        }

        orderValidator.validOrderBaseOnCodePush(orderBaseOnCodeCmd, request, orderBusinessLogDO);
        // 校验是否重复传（之前已推送成功）
        OrderNoResp orderNoResp = validSendSuccess(orderBusinessLogDO.getDistributorId(),
            orderBaseOnCodeCmd.getOrderNo(), orderBusinessLogDO.getLogType());
        if (orderNoResp == null) {
            orderNoResp = new OrderNoResp();
        } else {
            LOGGER.info(orderBaseOnCodeCmd.getOrderNo() + "第三方订单重复推送（之前已推送成功）、返回{}", JSON.toJSONString(orderNoResp));
            return ResponseEntity.ok(orderNoResp);
        }
        String errorMsg = null;
        try {
            // 参数转换
            OrderInfoCmd orderInfoCmd =
                orderOpenConvertor.toOrderInfoBaseOnCode(orderBaseOnCodeCmd, orderBusinessLogDO);
            LOGGER.info("第三方（基于编码）推送订单参数:{}", JSON.toJSONString(orderInfoCmd));
            com.bat.dubboapi.order.common.Response<List<OrderDTO>> distributorOrderResp =
                orderServiceRpc.createDistributorOrder(orderInfoCmd);
            LOGGER.info("第三方（基于编码）推送订单返回:{}", JSON.toJSONString(distributorOrderResp));

            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_SUCCESS);

            if (distributorOrderResp == null) {
                throw new ThirdPartyOpenApiException(
                    MessageUtils.get(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION));
            }
            if (!distributorOrderResp.isSuccess()) {
                throw new ThirdPartyOpenApiException(distributorOrderResp.getErrMessage());
            }
            OrderDTO orderDTO = distributorOrderResp.getData().get(0);
            orderBusinessLogDO.setOrderNo(orderDTO.getOrderNo());
            orderNoResp.setOrderId(orderDTO.getId());
            // 基于编码的、传回去的orderNo是第三方的订单号、历史原因、千万不要搞错
            // 历史原因、基于ID和基于编码不一样（ID返回的是bat的编码、基于编码返回的是第三方的订单号）
            orderNoResp.setOrderNo(orderBusinessLogDO.getOtherOrderNo());
            // 改为返回bat的订单编码
            orderNoResp.setorderNo(orderDTO.getOrderNo());
            orderNoResp.setOrderId(orderDTO.getId());
            orderNoResp.setCode(200);
            orderNoResp.setMsg("SUCCESS");
            orderNoResp.setUserMsg("推送成功");
            orderNoResp.setThirdOrderNo(orderBaseOnCodeCmd.getOrderNo());
            OrderNoResp.OrderData data = new OrderNoResp.OrderData();
            org.springframework.beans.BeanUtils.copyProperties(orderNoResp, data);
            orderNoResp.setData(data);
            orderBusinessLogDO.setResponseMsg(JSON.toJSONString(orderNoResp));
        } catch (ThirdPartyOpenApiException e) {
            e.printStackTrace();
            LOGGER.error("第三方对外订单接口订单确认、基于编码对接,出现异常,{}", e);
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            errorMsg = e.getMessage();
            orderBusinessLogDO.setResponseMsg(e.getMessage());
            orderBusinessLogDO.setErrorMsg(e.getMessage());
            orderNoResp.setCode(20020);
            orderNoResp.setMsg(e.getMessage());
            orderNoResp.setUserMsg("推送失败：" + e.getMessage());
            orderBusinessLogDO.setResponseMsg(JSON.toJSONString(orderNoResp));
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("第三方对外订单接口基于编码订单确认、出现异常,{}", e.getMessage());
            errorMsg = MessageUtils.get(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            orderBusinessLogDO.setErrorMsg(errorMsg);
            orderNoResp.setCode(20020);
            orderNoResp.setMsg(e.getMessage());
            orderNoResp.setUserMsg("推送失败：系统异常");
            orderBusinessLogDO.setResponseMsg(JSON.toJSONString(orderNoResp));
        } finally {

            orderReceiveLogServiceI.save(orderBusinessLogDO);
            if (StringUtils.isNotBlank(errorMsg)) {
                throw new ThirdPartyOpenApiException(errorMsg);
            }

            return ResponseEntity.ok(orderNoResp);
        }
    }

    @Override
    public ResponseEntity<Object> cancelOrderFromDistributor(String distributorId, String orderNo) {
        LOGGER.info("分销商系统取消订单、分销商编码,{},订单号{}", distributorId, orderNo);
        OrderBusinessLogDO orderBusinessLogDO = new OrderBusinessLogDO();
        orderBusinessLogDO.setLogType(OrderBusinessLogEnum.CANCEL_ORDER_FROM_THIRD.getLogType());
        orderBusinessLogDO.setTowardType(OrderBusinessLogEnum.CANCEL_ORDER_FROM_THIRD.getTowardType());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("distributorId", distributorId);
        // 之前是bat的单号
        jsonObject.put("orderNo", orderNo);
        orderBusinessLogDO.setRequestParamJson(JSON.toJSONString(jsonObject));
        ThirdOrderResp thirdOrderResp = new ThirdOrderResp();
        try {
            com.bat.dubboapi.order.common.Response<OrderInfoRpcQryDTO> orderInfoRpcResponse =
                orderInfoServiceRpc.getByOrderNo(orderNo);
            if (!orderInfoRpcResponse.isSuccess() || orderInfoRpcResponse.getData() == null) {
                throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            }
            com.bat.dubboapi.order.common.Response<OrderExtendDataSimpleRpcDTO> orderExtendDataSimpleResponse =
                orderExtendDataServiceRpc.getExtendDataSimpleByOrderId(orderInfoRpcResponse.getData().getId());
            if (!orderExtendDataSimpleResponse.isSuccess() || orderExtendDataSimpleResponse.getData() == null) {
                throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.DUBBO_ORDER_SERVICE_EXCEPTION);
            }
            String factoryNo = orderExtendDataSimpleResponse.getData().getOrderFactoryNo();
            if (StringUtils.isNotBlank(factoryNo)) {
                throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.HAS_SYN_FACTORY_CANT_CANCEL);
            }
            OrderValidator.validDistributorCancelOrder(orderNo, distributorId, orderBusinessLogDO);
            OrderCancelCmd orderCancelCmd = new OrderCancelCmd();
            orderCancelCmd.setDistributorId(Integer.parseInt(distributorId));
            orderCancelCmd.setOrderNo(orderNo);
            LOGGER.info("第三方取消订单、请求参数{}", JSON.toJSONString(orderCancelCmd));
            com.bat.dubboapi.order.common.Response response =
                orderServiceRpc.orderCancelByThirdparty(orderCancelCmd);
            LOGGER.info("第三方取消订单、接口返回{}", JSON.toJSONString(response));
            if (!response.isSuccess()) {
                throw new ThirdPartyOpenApiException(response.getErrMessage());
            } else {
                thirdOrderResp.setCode(0);
                thirdOrderResp.setMsg("SUCCESS");
                thirdOrderResp.setUserMsg("取消成功");
                orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_SUCCESS);
            }
        } catch (ThirdPartyOpenApiException e) {
            LOGGER.error("第三方取消订单失败{}", e.getMessage());
            e.printStackTrace();
            orderBusinessLogDO.setErrorMsg(e.getMessage());
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            thirdOrderResp.setCode(20020);
            thirdOrderResp.setMsg(e.getMessage());
            thirdOrderResp.setUserMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("第三方取消订单系统异常{}", e.getMessage());
            orderBusinessLogDO.setErrorMsg(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            thirdOrderResp.setCode(20020);
            thirdOrderResp.setMsg(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
            thirdOrderResp.setUserMsg(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
        } finally {
            orderBusinessLogDO.setCreateTime(new Date());
            orderBusinessLogDO.setResponseMsg(JSON.toJSONString(thirdOrderResp));
            orderBusinessLogServiceI.create(orderBusinessLogDO);
            return ResponseEntity.ok(thirdOrderResp);
        }

    }

    @Override
    public Response submitToThird(ProvisionalOrderInfo provisionalOrderInfo, HttpServletRequest servletRequest) {

        // 暂停推单功能，除开苏宁以外的都不能推送进来了
        if (!servletRequest.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID).equals("58")) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.PAUSE_PUSH_ORDER_FUNCTION);
        }

        // 校验临时订单
        orderValidator.validProvisionalOrder(provisionalOrderInfo, servletRequest);

        String skipLink = null;
        Integer distributorId = provisionalOrderInfo.getDistributorId();

        com.bat.dubboapi.distributor.common.Response<DistributorPlatformApiRpcDTO> distributorUrlResp =
            distributorPlatformApiServiceRpc.getByDistributorIdAndApiTypeAndPlatform(distributorId,
                ThirdDistributorPlatformApiConstant.Distributor_SYS_PLATFORM_API_TYPE_DIY_DETAIL_PUSH,
                String.valueOf(provisionalOrderInfo.getOrderSource()));
        if (distributorUrlResp == null || !distributorUrlResp.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
        }
        DistributorPlatformApiRpcDTO distributorPlatformApiRpcDTO = distributorUrlResp.getData();
        if (distributorPlatformApiRpcDTO == null) {
            throw ThirdPartyException.buildException("该分销商尚未配置订单推送链接、请与客服沟通处理");
        }
        com.bat.dubboapi.distributor.common.Response<SysPlatformRpcDTO> platformRpcDTOResponse =
            distributorSysPlatformServiceRpc
                .getByPlatformAndDistributorId(String.valueOf(provisionalOrderInfo.getOrderSource()), distributorId);
        if (platformRpcDTOResponse == null || !platformRpcDTOResponse.isSuccess()) {
            throw ThirdPartyException.buildException(ThirdDubboServiceErrorCode.DUBBO_DISTRIBUTOR_SERVICE_EXCEPTION);
        }
        SysPlatformRpcDTO sysPlatformRpcDTO = platformRpcDTOResponse.getData();
        if (sysPlatformRpcDTO == null) {
            throw ThirdPartyException.buildException("该分销商尚未APPKEY、请与客服沟通处理");
        }

        OrderDetailBaseOnIdQry orderDetail = provisionalOrderInfo.getOrderDetail();
        ZhaoLiangJiCustomInfo zhaoLiangJiCustomInfo = null;
        orderOpenConvertor.toProvisionalOrderParam(distributorId, distributorPlatformApiRpcDTO, orderDetail,
            zhaoLiangJiCustomInfo);
        String json = null;
        if (zhaoLiangJiCustomInfo != null) {
            json = JSON.toJSONString(zhaoLiangJiCustomInfo, SerializerFeature.WriteMapNullValue);
        } else {
            json = JSON.toJSONString(orderDetail);
        }
        // 获取请求头
        /* HttpHeaders httpHeaders = HttpUtil.getHttpHeaders(json, distributorPlatformApiRpcDTO.getAppId(),
            distributorPlatformApiRpcDTO.getAppKey());*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID, distributorId);
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE, provisionalOrderInfo.getOrderSource());
        OrderBusinessLogDO pushLogDO = new OrderBusinessLogDO();
        pushLogDO.setLogType(OrderBusinessLogEnum.PUSH_DIY_TO_THIRD.getLogType());
        pushLogDO.setTowardType(OrderBusinessLogEnum.PUSH_DIY_TO_THIRD.getTowardType());
        pushLogDO.setDistributorId(distributorId);
        pushLogDO.setPlatform(provisionalOrderInfo.getOrderSource());
        pushLogDO.setHeaderParamJson(JSON.toJSONString(jsonObject));
        pushLogDO.setRequestParamJson(json);
        pushLogDO.setCreateTime(new Date());
        /* String result = httpUtil.requestFor(distributorPlatformApiRpcDTO.getHostName() + distributorPlatformApiRpcDTO.getUri(),
                HttpMethod.POST, httpHeaders, zhaoLiangJiCustomInfo == null ? orderDetail : zhaoLiangJiCustomInfo, String.class);
        LOGGER.info("返回result{}",result);*/
        String url = distributorPlatformApiRpcDTO.getHostName() + distributorPlatformApiRpcDTO.getUri();
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = builder.build();
        HttpPost httpPost = new HttpPost(url);
        long currentTimeMillis = System.currentTimeMillis();
        httpPost.setHeader("timestamp", String.valueOf(currentTimeMillis));
        String signStr = distributorPlatformApiRpcDTO.getAppKey() + json + currentTimeMillis;
        LOGGER.info(url + "提交订单签名字符串" + signStr);
        String encryption = null;
        if (!provisionalOrderInfo.getOrderSource().equals(huaweiPlatformNo)) {
            encryption = Sha1Handler.encryption(signStr);
            LOGGER.info("得到签名：" + encryption);
        } else {
            signStr = json + currentTimeMillis;
            LOGGER.info(url + "签名字符串" + signStr);
            encryption = SHA256withRSAUtils.sign(huaweiPublicKey, signStr, huaweiPrivateKey);
        }
        httpPost.setHeader("sign", encryption);
        httpPost.setHeader("appId", distributorPlatformApiRpcDTO.getAppId());;
        Header header = new BasicHeader("Content-Type", "application/json");
        httpPost.setHeader(header);
        ContentType contentType;
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        entity.setContentEncoding(HTTP.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
            throw ThirdPartyException.buildException("提交订单信息异常");
        }
        /*CloseableHttpResponse response =
            httpUtil.requestFor(distributorPlatformApiRpcDTO.getHostName() + distributorPlatformApiRpcDTO.getUri(),
                HttpMethod.POST, httpHeaders, zhaoLiangJiCustomInfo == null ? orderDetail : zhaoLiangJiCustomInfo,
                CloseableHttpResponse.class);*/
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity responseEntity = response.getEntity();
            try {
                String jsonStr = EntityUtils.toString(responseEntity, Charset.defaultCharset());
                LOGGER.info("定制信息同步返回的数据:" + jsonStr);
                pushLogDO.setResponseMsg(jsonStr);
                if (org.apache.commons.lang3.StringUtils.isNoneBlank(jsonStr)) {
                    CustomInfoPushResponse pushResponse = JSON.parseObject(jsonStr, CustomInfoPushResponse.class);
                    LOGGER.info("推送定制信息到第三方、返回{}", JSON.toJSONString(pushResponse));
                    if (pushResponse != null
                        && org.apache.commons.lang3.StringUtils.isNoneBlank(pushResponse.getData())) {
                        skipLink = pushResponse.getData();
                    } else {
                        throw ThirdPartyException.buildException("定制信息推送失败、原因是返回跳转链接为空");
                    }
                } else {
                    throw ThirdPartyException.buildException("定制信息推送失败、原因是返回数据为空");
                }
            } catch (IOException e) {
                e.printStackTrace();
                pushLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
                pushLogDO.setErrorMsg("定制信息推送失败");
                orderBusinessLogServiceI.create(pushLogDO);
                return Response.buildFailure("ORDER_DETAIL_PUSH_FAIL", "定制信息推送失败");
            } catch (ThirdPartyException e) {
                e.printStackTrace();
                pushLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
                pushLogDO.setErrorMsg(e.getMsg());
                orderBusinessLogServiceI.create(pushLogDO);
                return Response.buildFailure("ORDER_DETAIL_PUSH_FAIL", "定制信息推送失败");
            }
        } else {
            pushLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            String jsonStr = null;
            try {
                jsonStr = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
                pushLogDO.setResponseMsg(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
                pushLogDO.setErrorMsg("定制信息推送失败");
                orderBusinessLogServiceI.create(pushLogDO);
            }
            LOGGER.info("订单定制信息推送失败：code：" + response.getStatusLine().getStatusCode() + "," + jsonStr);
            return Response.buildFailure("ORDER_DETAIL_PUSH_FAIL", "定制信息推送失败");
        }
        Map<String, String> map = new HashMap<>();
        map.put("url", skipLink);
        pushLogDO.setStatus(ThirdCommonConstant.API_REQUEST_SUCCESS);
        pushLogDO.setBusinessData(skipLink);
        // orderBusinessLogServiceI.create(pushLogDO);
        return Response.of(map);
    }

    @Override
    public void writeOffThirdCodeOrder() {
        // 获取需要核销的订单
        com.bat.dubboapi.order.common.Response<List<OrderGoodsThirdCodeRpcDTO>> response =
            orderServiceRpc.thirdWillWriteOffOrder();
        List<OrderGoodsThirdCodeRpcDTO> dtos = response.getData();
        LOGGER.info("未核销的订单:{}", JSONObject.toJSONString(dtos));
        // 根本分销商id以及平台编码分组
        Map<String, List<OrderGoodsThirdCodeRpcDTO>> orderGoodsThirdCodeMap =
            dtos.stream().collect(Collectors.groupingBy(o -> {
                StringBuffer bf = new StringBuffer();
                bf.append(o.getDistributorId()).append("_");
                bf.append(o.getPlatform());
                return bf.toString();
            }));
        List<Integer> orderGoodsThirdCodeIds = new ArrayList<>();
        List<Integer> orderIds = new ArrayList<>();

        short status = ThirdCommonConstant.API_REQUEST_SUCCESS;
        String errorMsg = null;
        String requestParamJson = null;
        String businessData = null;
        for (Map.Entry<String, List<OrderGoodsThirdCodeRpcDTO>> entry : orderGoodsThirdCodeMap.entrySet()) {
            try {
                List<OrderGoodsThirdCodeRpcDTO> orderGoodsThirdCodes = entry.getValue();
                if (orderGoodsThirdCodes.size() > 0) {

                    LOGGER.info("正在获取分销商平台信息:{}", orderGoodsThirdCodes.get(0).getPlatform());
                    com.bat.dubboapi.distributor.common.Response<SysPlatformRpcDTO> sysPlatformResponse =
                        distributorSysPlatformServiceRpc.getByPlatformAndDistributorId(
                            orderGoodsThirdCodes.get(0).getPlatform(), orderGoodsThirdCodes.get(0).getDistributorId());
                    LOGGER.info("获取到的分销商平台信息:{}", JSONObject.toJSONString(sysPlatformResponse));

                    LOGGER.info("正在获取分销商平台信息api:{}", JSONObject.toJSONString(orderGoodsThirdCodes.get(0)));
                    com.bat.dubboapi.distributor.common.Response<
                        DistributorPlatformApiRpcDTO> distributorPlatformResponse = distributorPlatformApiServiceRpc
                            .getByDistributorIdAndApiTypeAndPlatform(orderGoodsThirdCodes.get(0).getDistributorId(),
                                (short)5, orderGoodsThirdCodes.get(0).getPlatform());
                    LOGGER.info("获取到的分销商平台信息api:{}", JSONObject.toJSONString(distributorPlatformResponse));

                    SysPlatformRpcDTO sysPlatform = sysPlatformResponse.getData();
                    DistributorPlatformApiRpcDTO distributorPlatformApiRpc = distributorPlatformResponse.getData();

                    for (OrderGoodsThirdCodeRpcDTO dto : orderGoodsThirdCodes) {
                        AishideRequest aishideRequest = new AishideRequest();
                        AishideRequest.RequestBody body = new AishideRequest.RequestBody();
                        IdWorker idWorker = new IdWorker();
                        body.setReqId(String.valueOf(idWorker.nextId()));
                        body.setFlowId(dto.getCode());
                        body.setStatus(AishideConstant.SUCCESS_STATUS);
                        aishideRequest.setData(JSONObject.toJSONString(body));
                        aishideRequest.setApiname(AishideConstant.API_NAME);
                        aishideRequest
                            .setSign(Sign.HMACSHA256(JSONObject.toJSONString(body), sysPlatform.getAppSecret()));
                        aishideRequest.setChannelid(sysPlatform.getAppId());
                        try {
                            LOGGER.info("开始对订单核销:{}", JSONObject.toJSONString(aishideRequest));
                            AishideResponse aishideResponse =
                                httpUtil.requestFor(sysPlatform.getHostName() + distributorPlatformApiRpc.getUri(),
                                    HttpMethod.POST, aishideRequest, AishideResponse.class);
                            LOGGER.info("订单核销返回:{}", JSONObject.toJSONString(aishideResponse));
                            requestParamJson = JSONObject.toJSONString(aishideRequest);
                            businessData = JSONObject.toJSONString(aishideResponse);
                            if (aishideResponse != null && aishideResponse.getSuccess()) {
                                messageSendService.oredrLogPackage(dto.getOrderId(), "兑换码核销",
                                    "核销成功:" + JSONObject.toJSONString(aishideResponse),
                                    JSONObject.toJSONString(aishideRequest));
                                orderGoodsThirdCodeIds.add(dto.getId());
                                orderIds.add(dto.getOrderId());
                            } else {
                                status = ThirdCommonConstant.API_REQUEST_FAIL;
                                messageSendService.oredrLogPackage(dto.getOrderId(), "兑换码核销",
                                    "核销失败:" + JSONObject.toJSONString(aishideResponse),
                                    JSONObject.toJSONString(aishideRequest));
                                if (aishideResponse != null) {
                                    errorMsg = aishideResponse.getMessage();
                                }
                            }
                        } catch (Exception e) {
                            status = ThirdCommonConstant.API_REQUEST_FAIL;
                            errorMsg = e.getMessage();
                            messageSendService.oredrLogPackage(dto.getOrderId(), "兑换码核销",
                                "核销出现异常:" + JSONObject.toJSONString(e), JSONObject.toJSONString(aishideRequest));
                            LOGGER.error("核销订单出现异常:{}", e);
                        }
                        sendLoger(dto.getDistributorId(), dto.getPlatform(), requestParamJson, status, errorMsg,
                            businessData);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("处理核销订单出现异常:{}", e);
            }
        }
        ThirdCodeOrderCmd cmd = new ThirdCodeOrderCmd();
        cmd.setIds(orderGoodsThirdCodeIds);
        try {
            if (orderGoodsThirdCodeIds.size() > 0) {
                LOGGER.info("开始更改订单核销状态:{}", JSONObject.toJSONString(cmd));
                // 批量修改核销状态
                com.bat.dubboapi.order.common.Response response1 = orderServiceRpc.writeOffThirdCodeOrders(cmd);
                LOGGER.info("更改订单核销状态返回:{}", JSONObject.toJSONString(response1));
                if (response1 != null && response1.isSuccess()) {
                    messageSendService.oredrsLogPackage(orderIds, "更改兑换码核销状态", "更改成功", JSONObject.toJSONString(cmd));
                } else {
                    messageSendService.oredrsLogPackage(orderIds, "更改兑换码核销状态", "更改失败", JSONObject.toJSONString(cmd));
                }
            }
        } catch (Exception e) {
            messageSendService.oredrsLogPackage(orderIds, "更改兑换码核销状态", "更改状态出现异常:" + JSONObject.toJSONString(e),
                JSONObject.toJSONString(cmd));
            LOGGER.error("核销订单修改状态出现异常:{}", e);
        }
    }

    /**
     * 取消订单标准接口、支持
     * 
     * @param orderCancelCmd
     * @param request
     * @return
     */
    @Override
    public Response cancelOrderFromDistributorStandard(OrderCancelCmd orderCancelCmd, HttpServletRequest request) {
        LOGGER.info("取消订单标准接口，参数为{}", JSON.toJSONString(orderCancelCmd));
        Response response = new Response();
        OrderBusinessLogDO orderBusinessLogDO = new OrderBusinessLogDO();
        orderBusinessLogDO.setLogType(OrderBusinessLogEnum.CANCEL_ORDER_FROM_THIRD.getLogType());
        orderBusinessLogDO.setTowardType(OrderBusinessLogEnum.CANCEL_ORDER_FROM_THIRD.getTowardType());

        orderBusinessLogDO.setRequestParamJson(JSON.toJSONString(orderCancelCmd));

        try {
            // 校验标准取消订单参数
            OrderValidator.validDistributorCancelOrderStandard(orderCancelCmd, request);

            LOGGER.info("第三方取消订单(标准接口)、请求参数{}", JSON.toJSONString(orderCancelCmd));
            com.bat.dubboapi.order.common.Response orderCancelResponse =
                orderServiceRpc.orderCancelByThirdparty(orderCancelCmd);
            LOGGER.info("第三方取消订单（标准接口）、接口返回{}", JSON.toJSONString(orderCancelResponse));
            if (!orderCancelResponse.isSuccess()) {
                throw new ThirdPartyOpenApiException(orderCancelResponse.getErrMessage());
            } else {
                orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_SUCCESS);
                response.setCode(0);
                response.setSuccess(true);
                response.setMsg("取消成功");
            }
        } catch (ThirdPartyOpenApiException e) {
            LOGGER.error("第三方取消订单（标准接口）失败{}", e.getMessage());
            e.printStackTrace();
            orderBusinessLogDO.setErrorMsg(e.getMessage());
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            response.setSuccess(false);
            response.setCode(20020);
            response.setErrMessage(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("第三方取消订单（标准接口）系统异常{}", e.getMessage());
            orderBusinessLogDO.setErrorMsg(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            response.setSuccess(false);
            response.setCode(20020);
            response.setErrMessage(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
        } finally {
            orderBusinessLogDO.setCreateTime(new Date());
            orderBusinessLogDO.setResponseMsg(JSON.toJSONString(response));
            orderBusinessLogServiceI.create(orderBusinessLogDO);
            return response;
        }
    }

    @Override
    public ResponseEntity<Object> createOrderFromMoleji(MolejiOrderCreateCmd molejiOrderCreateCmd) {
        // 0.校验参数信息
        // 1. 将所有约定的数据信息进行读取
        OrderBusinessLogDO logDO = new OrderBusinessLogDO();
        logDO.setCreateTime(new Date());
        logDO.setLogType(OrderBusinessLogEnum.RECEIVE_ORDER_FROM_MOLEJI.getLogType());
        logDO.setTowardType(OrderBusinessLogEnum.RECEIVE_ORDER_FROM_MOLEJI.getTowardType());
        logDO.setRequestParamJson(JSON.toJSONString(molejiOrderCreateCmd));
        orderValidator.validOrderFromMoleji(molejiOrderCreateCmd, logDO);
        OrderNoResp orderNoResp = new OrderNoResp();
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 暂停推单功能，除开苏宁以外的都不能推送进来了
        if (!request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID).equals("58")) {
            throw new ThirdPartyOpenApiException(ThirdDubboServiceErrorCode.PAUSE_PUSH_ORDER_FUNCTION);
        }

        String errorMsg = null;
        try {
            OrderInfoCmd orderInfoCmd = new OrderInfoCmd();
            orderInfoCmd.setDistributorId(molejiDistributorId);
            orderInfoCmd.setOrderSource(molejiOrderSource);
            orderInfoCmd.setInvoiceFlag((short)1);
            orderInfoCmd.setCurrencyType("CNY");
            orderInfoCmd.setRemark(molejiOrderCreateCmd.getRemark());
            orderInfoCmd.setOrderThirdpartyNo(molejiOrderCreateCmd.getOrderNo());

            List<MolejiCaseCmd> caseCmdList =
                JSONArray.parseArray(JSON.toJSONString(molejiOrderCreateCmd.getCaseArray()), MolejiCaseCmd.class);
            // 校验摩乐吉订单详情
            orderValidator.validOrderDetailFromMoleji(caseCmdList);
            com.bat.dubboapi.flexible.common.Response<List<OrderGoodsDiyParamDTO>> response =
                thirdSkuServiceRpc.transformOrderSync(caseCmdList, molejiDistributorId);
            if (!response.isSuccess()) {
                throw new ThirdPartyOpenApiException(response.getErrMessage());
            }
            List<OrderGoodsDiyParamDTO> orderGoodsDiyParamDTOS = response.getData();

            orderInfoCmd.setGoodss(OrderOpenConvertor.toOrderGoodsCmdList(orderGoodsDiyParamDTOS));
            AddressQry addressQry = new AddressQry(molejiOrderCreateCmd.getProvince(), molejiOrderCreateCmd.getCity(),
                molejiOrderCreateCmd.getArea(), molejiOrderCreateCmd.getAddress());
            UserInfoQry userInfoQry =
                new UserInfoQry(molejiOrderCreateCmd.getUserName(), molejiOrderCreateCmd.getMobile(), null, null);
            OrderDeliveryCmd orderDeliveryCmd = orderOpenConvertor.toOrderDeliveryCmd(addressQry, userInfoQry);
            orderInfoCmd.setDelivery(orderDeliveryCmd);
            List<OrderLogisticsCmd> logisticsCmdList =
                orderOpenConvertor.listOrderLogisticsCmdByCondition(molejiDistributorId,
                    orderInfoCmd.getGoodss().get(0).getDiy().getManufactors(), "2", orderDeliveryCmd.getCityId());
            orderInfoCmd.setLogisticss(logisticsCmdList);
            LOGGER.info("摩乐吉同步订单、下单参数{}", JSON.toJSONString(orderInfoCmd));
            com.bat.dubboapi.order.common.Response<List<OrderDTO>> orderResponse =
                orderServiceRpc.createDistributorOrder(orderInfoCmd);

            return ResponseEntity.ok(null);
        } catch (Exception e) {
            LOGGER.error("摩乐吉同步订单至B2B出现异常：" + JSON.toJSONString(molejiOrderCreateCmd));
            e.printStackTrace();
            String message = e.getMessage();

        } finally {

        }
        return null;
    }

    @Override
    public void sumsungOrder(Integer orderId) {
        OrderInfoRpcQryDTO orderInfo = orderInfoServiceRpc.getById(orderId).getData();
        LOGGER.info("得到的订单信息:{}", JSONObject.toJSONString(orderInfo));
        if (orderInfo == null) {
            return;
        }
        OrderCustomerDataRpcDTO orderCustomerData = orderCustomerDataServiceRpc.getByOrderId(orderId).getData();
        LOGGER.info("得到的C订单信息:{}", JSONObject.toJSONString(orderCustomerData));
        if (orderCustomerData == null) {
            return;
        }
        List<OrderGoodsDetailRpcDTO> orderGoodsDetails =
            orderGoodsDubboServiceRpc.listOrderGoodsDetailByOrderId(orderId).getData();
        LOGGER.info("得到的商品信息列表:{}", JSONObject.toJSONString(orderGoodsDetails));
        OrderCustomerCostRpcQryDTO orderCustomerCost = orderCustomerCostServiceRpc
            .getOrderCustomerCost(orderCustomerData.getOrderId(), orderCustomerData.getCustomerId()).getData();
        LOGGER.info("得到的C端费用信息:{}", JSONObject.toJSONString(orderCustomerCost));
        if (orderCustomerCost == null) {
            return;
        }
        LOGGER.info("开始获取C端用户信息");
        com.bat.dubboapi.distributor.common.Response<CustomerRpcDTO> customerRpcDTOResponse =
            customerServiceRpc.getCustomerByCustomerIdAndDistributorIdAndPlatform(orderCustomerData.getCustomerId(),
                orderCustomerData.getDistributorId(), orderInfo.getOrderSource());
        CustomerRpcDTO customer = customerRpcDTOResponse.getData();
        LOGGER.info("得到的C端用户信息:{}", JSONObject.toJSONString(customer));
        if (customer == null || StringUtils.isBlank(customer.getOtherId())) {
            LOGGER.info("客户信息为空或者uid为空");
            return;
        }
        Gson gson = new GsonBuilder().create();
        OrderRequest orderRequest =
            getOrderRequest(orderInfo, orderCustomerCost, orderGoodsDetails, customer, gson, orderCustomerData);
        // 请求参数日志
        String requestJson = gson.toJson(orderRequest);
        LOGGER.info("开始请求订单同步地址url:" + sumsungOrderUrl + "--requestJson------------------>  " + requestJson);
        BaseResponse baseResponse = orderHttp.sysOrder(orderRequest, sumsungOrderUrl);
        // 请求结果日志
        String responseJson = gson.toJson(baseResponse);
        LOGGER.info("订单同步地址url:" + sumsungOrderUrl + "--responseJson------------------>  " + responseJson);
        if (!baseResponse.getStatusCode().equals(SA_ERROR_CODE_SUCCESS)) {
            messageSendService.oredrLogPackage(orderInfo.getId(), "同步信息到", "同步失败:" + baseResponse.getMessage(),
                JSONObject.toJSONString(orderRequest));
        }
        if (baseResponse != null && baseResponse.getStatusCode().equals(SA_ERROR_CODE_SUCCESS)) {
            messageSendService.oredrLogPackage(orderInfo.getId(), "同步信息到", "同步成功",
                JSONObject.toJSONString(orderRequest));
        }
        return;
    }

    private OrderRequest getOrderRequest(OrderInfoRpcQryDTO orderInfo, OrderCustomerCostRpcQryDTO orderCustomerCost,
        List<OrderGoodsDetailRpcDTO> orderItems, CustomerRpcDTO user, Gson gson,
        OrderCustomerDataRpcDTO orderCustomerData) {
        OrderRequest orderRequest = new OrderRequest();
        OrderRequest.Transaction transaction = new OrderRequest.Transaction();
        transaction.setUid(user.getOtherId());
        transaction.setTransactionDateTime(orderInfo.getCreateTime().getTime() / 1000);
        transaction.setUrl("订单详情URL");
        OrderRequest.TransactionData transactionData = new OrderRequest.TransactionData();
        transactionData.setOrderNumber(orderInfo.getOrderNo());
        transactionData.setTotalAmount(orderCustomerCost.getPayAmount().doubleValue());

        if (orderCustomerData.getOrderStatus() == OrderStatus.PENDING.getValue().shortValue()) {
            transactionData.setOrderStatus(new OrderRequest.OrderStatus(OrderStatus.PENDING.getName(),
                String.valueOf(orderCustomerData.getOrderStatus())));
        } else if (orderCustomerData.getOrderStatus() == OrderStatus.CONFIRMED.getValue().shortValue()) {
            transactionData.setOrderStatus(new OrderRequest.OrderStatus(OrderStatus.CONFIRMED.getName(),
                String.valueOf(orderCustomerData.getOrderStatus())));
        } else if (orderCustomerData.getOrderStatus() == OrderStatus.REDUSE.getValue().intValue()) {
            transactionData.setOrderStatus(new OrderRequest.OrderStatus(OrderStatus.REDUSE.getName(),
                String.valueOf(orderCustomerData.getOrderStatus())));
        } else if (orderCustomerData.getOrderStatus() == OrderStatus.CANCELLED.getValue().shortValue()) {
            transactionData.setOrderStatus(new OrderRequest.OrderStatus(OrderStatus.CANCELLED.getName(),
                String.valueOf(orderCustomerData.getOrderStatus())));
        } else if (orderCustomerData.getOrderStatus() == OrderStatus.COMPLETED.getValue().shortValue()) {
            transactionData.setOrderStatus(new OrderRequest.OrderStatus(OrderStatus.COMPLETED.getName(),
                String.valueOf(orderCustomerData.getOrderStatus())));
        }
        // 设置为未付款
        transactionData.setPaymentStatus("paymentDue");
        if (orderCustomerData.getPayStatus() == PayStatus.PAID.getValue().intValue()) {
            // 设置为已付款
            transactionData.setPaymentStatus("paid");
        }
        Long orderQuantity = 0L;
        for (OrderGoodsDetailRpcDTO orderItem : orderItems) {
            OrderRequest.OrderItem item = new OrderRequest.OrderItem();
            item.setImage(orderItem.getPreviewImage());
            item.setName(orderItem.getItemName());
            item.setQuantity(orderItem.getItemCount().longValue());
            item.setPrice(orderItem.getActualPrice().doubleValue());
            orderQuantity = orderQuantity + orderItem.getItemCount();
            transactionData.getOrderItem().add(item);
        }
        transactionData.setOrderQuantity(orderQuantity);
        transaction.setTransactionData(CommonUtil.base64(gson.toJson(transactionData)));
        transaction.setUrl(sumsungOrderDetailUrl + orderInfo.getId());
        orderRequest.getTransactions().add(transaction);
        return orderRequest;
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
            LOGGER.error("插入第三方日志出现异常:{}", e);
        }
    }

    public static void main(String[] args) {
        OrderNoResp orderNoResp = new OrderNoResp();
        orderNoResp.setOrderId(121);
        orderNoResp.setCode(200);

    }

}
