package com.bat.thirdparty.order.executor.log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.alibaba.taobao.api.TaoBaoTradeServiceI;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.distributor.ThirdDistributorPlatformApiConstant;
import com.bat.thirdparty.common.error.log.OrderBusinessLogErrorCode;
import com.bat.thirdparty.common.http.HttpUtil;
import com.bat.thirdparty.common.log.OrderBusinessLogEnum;
import com.bat.thirdparty.common.order.OrderHeaderConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bat.dubboapi.distributor.platform.api.DistributorPlatformApiServiceRpc;
import com.bat.dubboapi.distributor.platform.dto.DistributorPlatformApiRpcDTO;
import com.bat.dubboapi.thirdparty.common.ResponseBaseBean;
import com.bat.thirdparty.order.api.OrderOpenServiceI;
import com.bat.thirdparty.order.api.dto.OrderBaseOnCodeCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.common.OrderNoResp;
import com.bat.thirdparty.order.api.dto.third.OrderNoPushCmd;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.validator.OrderBusinessLogValidator;

/**
 * 业务日志失败重试执行器
 */
@Component
public class OrderBusinessLogFailCmdExe {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBusinessLogFailCmdExe.class);

    @DubboReference(check = false, timeout = 10000, retries = 0)
    private DistributorPlatformApiServiceRpc distributorPlatformApiServiceRpc;

    @Autowired
    private OrderOpenServiceI orderOpenServiceI;

    @Resource
    private HttpUtil httpUtil;

    // 淘宝的平台编码
    @Value("${distributor.taobao.platform}")
    private String taobaoPlatforms;

    @Autowired
    private TaoBaoTradeServiceI taoBaoTradeServiceI;

    /**
     * 接口重试机制
     * 
     * @param orderBusinessLogDO
     * @param request
     */
    public void pushAgain(OrderBusinessLogDO orderBusinessLogDO, HttpServletRequest request) {

        if (OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_ID.getLogType().equals(orderBusinessLogDO.getLogType())) {
            DistributorPlatformApiRpcDTO distributorOrderNoCallBackUrl = null;
            // 获取订单返回类型
            Short orderCallBackType = getOrderCallBackTypeByPlatform(orderBusinessLogDO.getPlatform());
            try {
                OrderBusinessLogValidator.validateRequestHeaders(orderBusinessLogDO.getHeaderParamJson());
                OrderBusinessLogValidator.validateRequestBody(orderBusinessLogDO.getRequestParamJson());
                // 获取分销商接口url
                if (ThirdDistributorPlatformApiConstant.ORDER_NO_CALL_BACK_TYPE_DISTRIBUTOR_EXTERNAL
                    .equals(orderCallBackType)) {
                    distributorOrderNoCallBackUrl = getDistributorOrderNoCallBackUrl(orderBusinessLogDO);
                }
            } catch (ThirdPartyException e) {
                LOGGER.error("接口重推参数校验失败、原因是{}", e.getMsg());
                e.printStackTrace();
                throw ThirdPartyException.buildException(e.getCode(), "重推失败、原因是【" + e.getMsg() + "】");
            }
            OrderBaseOnIdCmd orderBaseOnIdCmd =
                JSONObject.parseObject(orderBusinessLogDO.getRequestParamJson(), OrderBaseOnIdCmd.class);
            HttpUtil.setRequestParam(orderBusinessLogDO.getHeaderParamJson(), request);

            try {
                LOGGER.info("业务日志id{}进行重推", orderBusinessLogDO.getId());
                ResponseEntity<Object> responseEntity =
                    orderOpenServiceI.createOrderBaseOnId(request, orderBaseOnIdCmd, null, orderBusinessLogDO.getId());
                LOGGER.info("重试请求返回{}", JSON.toJSONString(responseEntity));
                String url = null;
                if (distributorOrderNoCallBackUrl != null) {
                    url = distributorOrderNoCallBackUrl.getHostName() + distributorOrderNoCallBackUrl.getUri();
                }
                pushOrderNoToThird(responseEntity, url, orderCallBackType);
            } catch (ThirdPartyOpenApiException e) {
                e.printStackTrace();
                LOGGER.error("重试请求出现异常{}", e);
                throw ThirdPartyException.buildException(OrderBusinessLogErrorCode.T_LOG_REQUEST_AGAIN_FAIL,
                    "重推失败、原因是【" + e.getMessage() + "】");
            }
        }
        if (OrderBusinessLogEnum.RECEIVE_DIY_ORDER_BASE_CODE.getLogType().equals(orderBusinessLogDO.getLogType())) {
            DistributorPlatformApiRpcDTO distributorOrderNoCallBackUrl = null;
            // 获取订单返回类型
            Short orderCallBackType = getOrderCallBackTypeByPlatform(orderBusinessLogDO.getPlatform());
            try {
                OrderBusinessLogValidator.validateRequestBody(orderBusinessLogDO.getRequestParamJson());
                OrderBusinessLogValidator.validateRequestHeaders(orderBusinessLogDO.getHeaderParamJson());
                // 获取分销商接口url
                if (ThirdDistributorPlatformApiConstant.ORDER_NO_CALL_BACK_TYPE_DISTRIBUTOR_EXTERNAL
                    .equals(orderCallBackType)) {
                    distributorOrderNoCallBackUrl = getDistributorOrderNoCallBackUrl(orderBusinessLogDO);
                }
            } catch (ThirdPartyException e) {
                LOGGER.error("接口重推参数校验失败、原因是{}", e.getMsg());
                e.printStackTrace();
                throw ThirdPartyException.buildException(e.getCode(), "重推失败、原因是【" + e.getMsg() + "】");
            }
            OrderBaseOnCodeCmd orderBaseOnCodeCmd =
                JSONObject.parseObject(orderBusinessLogDO.getRequestParamJson(), OrderBaseOnCodeCmd.class);
            HttpUtil.setRequestParam(orderBusinessLogDO.getHeaderParamJson(), request);
            try {
                ResponseEntity<Object> responseEntity =
                    orderOpenServiceI.createOrderBaseOnCode(orderBaseOnCodeCmd, request, orderBusinessLogDO.getId());
                LOGGER.info("重试请求返回{}", JSON.toJSONString(responseEntity));
                String url = null;
                if (distributorOrderNoCallBackUrl != null) {
                    url = distributorOrderNoCallBackUrl.getHostName() + distributorOrderNoCallBackUrl.getUri();
                }
                pushOrderNoToThird(responseEntity, url, orderCallBackType);
            } catch (ThirdPartyOpenApiException e) {
                e.printStackTrace();
                LOGGER.error("重试请求出现异常{}", e);
                throw ThirdPartyException.buildException(OrderBusinessLogErrorCode.T_LOG_REQUEST_AGAIN_FAIL,
                    "重推失败、原因是【" + e.getMessage() + "】");
            }

        }

    }

    private Short getOrderCallBackTypeByPlatform(String platform) {
        if (StringUtils.isBlank(taobaoPlatforms)) {
            // 非淘宝
            return ThirdDistributorPlatformApiConstant.ORDER_NO_CALL_BACK_TYPE_DISTRIBUTOR_EXTERNAL;
        }
        String[] strArray = taobaoPlatforms.split(",");
        if (strArray != null && strArray.length > 0) {
            for (int x = 0; x < strArray.length; x++) {
                if (platform.equals(strArray[x])) {
                    // 相同编码
                    return ThirdDistributorPlatformApiConstant.ORDER_NO_CALL_BACK_TYPE_TAOBAO;
                }
            }
        }
        return ThirdDistributorPlatformApiConstant.ORDER_NO_CALL_BACK_TYPE_DISTRIBUTOR_EXTERNAL;
    }

    /**
     * 获取分销商订单编号回传接口URL
     * 
     * @param orderBusinessLogDO
     * @return
     */
    private DistributorPlatformApiRpcDTO getDistributorOrderNoCallBackUrl(OrderBusinessLogDO orderBusinessLogDO) {
        String headerParamJson = orderBusinessLogDO.getHeaderParamJson();
        JSONObject jsonObject = JSON.parseObject(headerParamJson);
        com.bat.dubboapi.distributor.common.Response<DistributorPlatformApiRpcDTO> distributorApiResponse =
            distributorPlatformApiServiceRpc.getByDistributorIdAndApiTypeAndPlatform(
                jsonObject.getInteger(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID),
                ThirdDistributorPlatformApiConstant.Distributor_SYS_PLATFORM_API_TYPE_ORDER_NO_CALLBACK,
                jsonObject.getString(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE));
        if (!distributorApiResponse.isSuccess()) {
            throw ThirdPartyException.buildException(distributorApiResponse.getErrCode(),
                distributorApiResponse.getErrMessage());
        }
        DistributorPlatformApiRpcDTO distributorPlatformApiRpcDTO = distributorApiResponse.getData();
        if (distributorPlatformApiRpcDTO == null) {
            throw ThirdPartyException.buildException("22", "该分销商尚未配置订单编号信息推送、无法重推、请与客服沟通处理");
        }
        return distributorPlatformApiRpcDTO;
    }

    /**
     * 回传订单编码到第三方
     * 
     * @param responseEntity
     * @param url
     */
    private void pushOrderNoToThird(ResponseEntity<Object> responseEntity, String url, Short orderCallBackType) {
        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            // 成功了
            OrderNoPushCmd orderNoPushCmd = new OrderNoPushCmd();
            OrderNoResp orderNoResp = (OrderNoResp)responseEntity.getBody();
            if (orderNoResp.getCode() != 200) {
                throw ThirdPartyException.buildException(orderNoResp.getMsg());
            }

            if (ThirdDistributorPlatformApiConstant.ORDER_NO_CALL_BACK_TYPE_TAOBAO.equals(orderCallBackType)) {
                // 淘宝
                try {
                    taoBaoTradeServiceI.hasDown(orderNoResp.getThirdOrderNo());
                } catch (Exception e) {
                    e.printStackTrace();
                    String error = "后台触发推单到B2B正常、但推单号回传淘宝数据异常";
                    LOGGER.error(error + "{}", e.getMessage());
                    throw ThirdPartyException.buildException(error);
                }
                return;
            }

            // 第三方的单号 （之前的参数要兼容）
            orderNoPushCmd.setJiujiNo(orderNoResp.getThirdOrderNo());
            orderNoPushCmd.setOtherOrderNo(orderNoResp.getThirdOrderNo());
            // 下面是bat的单号（之前的参数要兼容）
            orderNoPushCmd.setLuokeNo(orderNoResp.getorderNo());
            // 历史原因、基于ID和基于编码不一样（ID返回的是bat的编码、基于编码返回的是第三方的订单号）、这个orderNo表示传到第三方是bat的订单编码，不要去OrderResp的orderNo(基于编码是第三方的订单号)
            orderNoPushCmd.setOrderNo(orderNoResp.getorderNo());
            LOGGER.info("订单编号回传第三方接口参数：{}", JSON.toJSONString(orderNoPushCmd));

            ResponseBaseBean responseBaseBean =
                httpUtil.requestFor(url, HttpMethod.POST, orderNoPushCmd, ResponseBaseBean.class);
            LOGGER.info("订单编号回传返回字符串{}", JSON.toJSONString(responseBaseBean));

            if (responseBaseBean == null || responseBaseBean.getCode() != 0) {
                String error =
                    "后台触发推单到B2B正常、但推单号回传到第三方异常:" + (responseBaseBean == null ? "对方系统无反应" : responseBaseBean.getMsg());
                LOGGER.error(error);
                throw ThirdPartyException.buildException(error);
            } else {
                LOGGER.info("后台触发推单到B2B、B2B回传单号给第三方成功");
            }
        } else {
            throw ThirdPartyException.buildException("22", "重试请求失败");
        }
    }



}
