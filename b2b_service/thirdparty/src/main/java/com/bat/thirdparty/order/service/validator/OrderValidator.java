package com.bat.thirdparty.order.service.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bat.thirdparty.alibaba.taobao.api.dto.TaoBaoHttpRequestDTO;
import com.bat.thirdparty.common.ThirdCommonConstant;
import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdNameErrorCode;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.error.order.ThirdSkuOpenErrorConstant;
import com.bat.thirdparty.common.order.OrderHeaderConstant;
import com.bat.thirdparty.common.util.MD5Utils;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.thirdparty.common.util.SHA256Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnCodeQry;
import com.bat.dubboapi.flexible.order.dto.OrderDetailBaseOnIdQry;
import com.bat.dubboapi.flexible.third.dto.MolejiCaseCmd;
import com.bat.dubboapi.order.order.dto.OrderCancelCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnCodeCmd;
import com.bat.thirdparty.order.api.dto.OrderBaseOnIdCmd;
import com.bat.thirdparty.order.api.dto.common.AddressQry;
import com.bat.thirdparty.order.api.dto.common.UserInfoQry;
import com.bat.thirdparty.order.api.dto.moleji.MolejiOrderCreateCmd;
import com.bat.thirdparty.order.api.dto.provisional.ProvisionalOrderInfo;
import com.bat.thirdparty.order.api.log.OrderBusinessLogServiceI;
import com.bat.thirdparty.order.dao.dataobject.log.OrderBusinessLogDO;
import com.bat.thirdparty.order.service.convertor.OrderOpenConvertor;

@Component
public class OrderValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderValidator.class);

    @Autowired
    private OrderOpenConvertor orderOpenConvertor;

    @Autowired
    private OrderBusinessLogServiceI orderBusinessLogServiceI;

    @Value("${moleji.factorySecret:16f96790f9a92e5f33ea379b160a3fac}")
    private String MolejiFactorySecret;

    public static void validDistributorCancelOrder(String orderNo, String distributorId,
        OrderBusinessLogDO orderBusinessLogDO) {
        if (StringUtils.isBlank(orderNo)) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_ID_NULL));
        }
        if (distributorId == null) {
            String message = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_DISTRIBUTOR)
                + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
            throw new ThirdPartyOpenApiException(message);
        }

    }

    public static void validDistributorCancelOrderStandard(OrderCancelCmd orderCancelCmd, HttpServletRequest request) {
        String distributorId = request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID);
        validateDistributorId(distributorId);
        orderCancelCmd.setDistributorId(Integer.parseInt(distributorId));
        String orderSource = request.getHeader(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE);
        if (StringUtils.isNotBlank(orderCancelCmd.getOrderNo())) {
            return;
        }
        if (orderCancelCmd.getOrderId() != null) {
            return;
        }
        if (StringUtils.isNotBlank(orderCancelCmd.getOrderThirdpartyNo())) {
            return;
        }
        if (StringUtils.isNotBlank(orderCancelCmd.getOrderErpNo())) {
            return;
        }
        // 订单编码不能为空
        throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_ORDER)
            + MessageUtils.get(ThirdCommonErrorCode.COMMON_CODE_NULL));
    }

    /**
     * 校验系统订单推送、基于id
     * 
     * @param orderBaseOnIdCmd
     * @param businessLogDO
     * @param taoBaoHttpRequestDTO
     */
    public void validOrderBaseOnIdPush(OrderBaseOnIdCmd orderBaseOnIdCmd, HttpServletRequest request,
        OrderBusinessLogDO businessLogDO, TaoBaoHttpRequestDTO taoBaoHttpRequestDTO) {

        // 1. 将所有约定的数据信息进行读取
        String distributorId = null;
        String orderSource = null;
        String timestamp = null;
        String sign = null;
        if (request != null) {
            // 1. 将所有约定的数据信息进行读取
            distributorId = request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID);
            orderSource = request.getHeader(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE);
            timestamp = request.getHeader(OrderHeaderConstant.HEADER_NAME_TIMESTAMP);
            sign = request.getHeader(OrderHeaderConstant.HEADER_NAME_SIGN);
        }
        if (request == null && taoBaoHttpRequestDTO != null) {
            // 1. 将所有约定的数据信息进行读取
            distributorId = taoBaoHttpRequestDTO.getDistributorId();
            orderSource = taoBaoHttpRequestDTO.getOrderSource();
            timestamp = taoBaoHttpRequestDTO.getTimestamp();
            sign = taoBaoHttpRequestDTO.getOrderSign();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID, distributorId);
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE, orderSource);
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_TIMESTAMP, timestamp);
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_SIGN, sign);
        String headerParam = JSON.toJSONString(jsonObject);
        businessLogDO.setHeaderParamJson(headerParam);
        String requestBody = JSON.toJSONString(orderBaseOnIdCmd);
        businessLogDO.setRequestParamJson(requestBody);
        LOGGER.info("请求参数：{},请求体参数：{}", headerParam, requestBody);
        // 校验请求头
        String message = null;
        try {
            // 校验请求头
            validHeaders(distributorId, orderSource, timestamp, sign);
            businessLogDO.setDistributorId(Integer.parseInt(distributorId));
            businessLogDO.setPlatform(orderSource);
            // 0.校验参数信息
            validOrder(orderBaseOnIdCmd);
            businessLogDO.setOtherOrderNo(orderBaseOnIdCmd.getOrderNo());
            // 校验地址信息
            validAddress(orderBaseOnIdCmd.getAddress());
            businessLogDO.setBusinessData(orderBaseOnIdCmd.getUserInfo().getUserNo());
            businessLogDO.setBusinessData(orderBaseOnIdCmd.getUserInfo().getUserNo());
            // 订单明细校验
            validOrderDetailList(orderBaseOnIdCmd.getOrderDetails());
            // 校验签名
            validSign(distributorId, orderSource, timestamp, sign, requestBody);
            // 校验用户信息
            validUserInfo(orderBaseOnIdCmd.getUserInfo(), distributorId);
        } catch (ThirdPartyOpenApiException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            businessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            businessLogDO.setResponseMsg(e.getMessage());
            orderBusinessLogServiceI.save(businessLogDO);
            throw new ThirdPartyOpenApiException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            businessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
        }
    }

    private void validSign(String distributorId, String orderSource, String timestamp, String sign,
        String requestBody) {

        String checkSignature = orderOpenConvertor.getSign(orderSource, distributorId, requestBody, timestamp);
        LOGGER.info(checkSignature);
        if (!sign.equalsIgnoreCase(checkSignature)) {
            throw new ThirdPartyOpenApiException("签名信息错误");
        }
    }

    private void validOrder(OrderBaseOnIdCmd jiuJiOrderCmd) {
        if (jiuJiOrderCmd == null) {
            throw new ThirdPartyOpenApiException("订单数据异常");
        }
        if (StringUtils.isBlank(jiuJiOrderCmd.getOrderNo())) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdCommonErrorCode.COMMON_ORDER_NO_NULL));
        }
    }

    public static void validateDistributorId(String distributorId) {
        if (StringUtils.isBlank(distributorId)) {
            String message = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_DISTRIBUTOR)
                + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
            throw new ThirdPartyOpenApiException(message);
        }
    }

    private void validHeaders(String distributorId, String orderSource, String timestamp, String sign) {
        String message;
        validateDistributorId(distributorId);
        if (StringUtils.isBlank(orderSource)) {
            message = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_SOURCE_NULL);
            throw new ThirdPartyOpenApiException(message);
        }
        if (StringUtils.isBlank(timestamp)) {
            message = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_TIMESTAMP_NULL);
            throw new ThirdPartyOpenApiException(message);
        }
        if (StringUtils.isBlank(sign)) {
            message = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_SIGN_NULL);
            throw new ThirdPartyOpenApiException(message);
        }
    }

    private void validUserInfo(UserInfoQry userInfo, String distributorId) {
        if (userInfo == null) {
            throw new ThirdPartyOpenApiException("收件人信息异常");
        }
        if (StringUtils.isBlank(userInfo.getPhoneNumber())) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_USER_MOBILE_NULL));
        }
        if (StringUtils.isBlank(userInfo.getUserName())) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_USER_NAME_NULL));
        }
        if (StringUtils.isBlank(userInfo.getUserNo())) {
            userInfo.setUserNo(distributorId);
        }
    }

    /**
     * 校验地址
     * 
     * @param address
     */
    private void validAddress(AddressQry address) {
        if (address == null) {
            throw new ThirdPartyOpenApiException("地址信息异常");
        }
        if (StringUtils.isBlank(address.getProvince())) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_ADDRESS_PROVINCE_NULL));
        }
        if (StringUtils.isBlank(address.getCity())) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_ADDRESS_CITY_NULL));
        }
        if (StringUtils.isBlank(address.getArea())) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_ADDRESS_AREA_NULL));
        }
        if (StringUtils.isBlank(address.getDetail())) {
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_ADDRESS_DETAIL_NULL));
        }
    }

    /**
     * 校验订单明细
     * 
     * @param orderDetailList
     */
    private void validOrderDetailList(List<OrderDetailBaseOnIdQry> orderDetailList) {
        if (orderDetailList == null || orderDetailList.size() == 0) {
            throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.T_ORDER_DETAIL_NULL);
        }
        orderDetailList.stream().forEach(thirdPartyOrderDetailQry -> {
            if (thirdPartyOrderDetailQry.getModelId() == null) {
                String msg = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_MODEL)
                    + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (thirdPartyOrderDetailQry.getBrandId() == null) {
                String msg = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_BRAND)
                    + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (thirdPartyOrderDetailQry.getPictureId() == null) {
                String msg = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_PICTURE)
                    + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (thirdPartyOrderDetailQry.getMaterialId() == null) {
                String msg = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_MATERIAL)
                    + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (StringUtils.isBlank(thirdPartyOrderDetailQry.getGenerateImage())) {
                String msg = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_GENERATE_IMAGE_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (StringUtils.isBlank(thirdPartyOrderDetailQry.getImage())) {
                String msg = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_PREVIEW_IMAGE_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (thirdPartyOrderDetailQry.getCount() == null) {
                throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_COUNT_NULL));
            }
            if (thirdPartyOrderDetailQry.getCount() < 1) {
                throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdCommonErrorCode.T_NUMBER_ILLEGAL));
            }
        });
    }

    public void validOrderBaseOnCodePush(OrderBaseOnCodeCmd orderBaseOnCodeCmd, HttpServletRequest request,
        OrderBusinessLogDO orderBusinessLogDO) {
        // 1. 将所有约定的数据信息进行读取
        String distributorId = request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID);
        String orderSource = request.getHeader(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE);
        String timestamp = request.getHeader(OrderHeaderConstant.HEADER_NAME_TIMESTAMP);
        String sign = request.getHeader(OrderHeaderConstant.HEADER_NAME_SIGN);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID, distributorId);
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE, orderSource);
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_TIMESTAMP, timestamp);
        jsonObject.put(OrderHeaderConstant.HEADER_NAME_SIGN, sign);
        String headerParam = JSON.toJSONString(jsonObject);
        orderBusinessLogDO.setHeaderParamJson(headerParam);
        String requestBody = JSON.toJSONString(orderBaseOnCodeCmd);
        orderBusinessLogDO.setRequestParamJson(requestBody);
        LOGGER.info("请求参数：{},请求体参数：{}", headerParam, requestBody);
        // 校验请求头
        String message = null;
        try {
            // 校验请求头
            validHeaders(distributorId, orderSource, timestamp, sign);
            orderBusinessLogDO.setDistributorId(Integer.parseInt(distributorId));
            orderBusinessLogDO.setPlatform(orderSource);
            // 0.校验参数信息
            if (orderBaseOnCodeCmd == null) {
                throw new ThirdPartyOpenApiException("订单信息不能为空");
            }
            if (StringUtils.isBlank(orderBaseOnCodeCmd.getOrderNo())) {
                throw new ThirdPartyOpenApiException(ThirdCommonErrorCode.COMMON_ORDER_NO_NULL);
            }
            orderBusinessLogDO.setOtherOrderNo(orderBaseOnCodeCmd.getOrderNo());
            // 校验地址信息
            validAddress(orderBaseOnCodeCmd.getAddress());
            // 设置用户编码
            orderBusinessLogDO.setBusinessData(orderBaseOnCodeCmd.getUserInfo().getUserNo());
            // 订单明细校验
            validOrderDetailListBaseOnCode(orderBaseOnCodeCmd.getOrderDetails());
            // 校验签名
            validSign(distributorId, orderSource, timestamp, sign, requestBody);
            // 校验用户信息
            validUserInfo(orderBaseOnCodeCmd.getUserInfo(), distributorId);
        } catch (ThirdPartyOpenApiException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            orderBusinessLogDO.setResponseMsg(e.getMessage());
            orderBusinessLogServiceI.save(orderBusinessLogDO);
            throw new ThirdPartyOpenApiException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            orderBusinessLogDO.setStatus(ThirdCommonConstant.API_REQUEST_FAIL);
            orderBusinessLogServiceI.save(orderBusinessLogDO);
            throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdCommonErrorCode.SYSTEM_EXCEPTION));
        }
    }

    private void validOrderDetailListBaseOnCode(List<OrderDetailBaseOnCodeQry> orderDetailList) {
        if (orderDetailList == null || orderDetailList.size() == 0) {
            throw new ThirdPartyOpenApiException("订单详情信息不可为空");
        }
        orderDetailList.stream().forEach(orderDetailBaseOnCodeQry -> {
            if (StringUtils.isBlank(orderDetailBaseOnCodeQry.getModelNo())) {
                String msg = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_MODEL)
                    + MessageUtils.get(ThirdCommonErrorCode.COMMON_CODE_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }

            if (StringUtils.isBlank(orderDetailBaseOnCodeQry.getPictureNo())) {
                String msg = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_PICTURE)
                    + MessageUtils.get(ThirdCommonErrorCode.COMMON_CODE_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (StringUtils.isBlank(orderDetailBaseOnCodeQry.getMaterialNo())) {
                String msg = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_MATERIAL)
                    + MessageUtils.get(ThirdCommonErrorCode.COMMON_CODE_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (StringUtils.isBlank(orderDetailBaseOnCodeQry.getGenerateImage())) {
                String msg = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_GENERATE_IMAGE_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (StringUtils.isBlank(orderDetailBaseOnCodeQry.getImage())) {
                String msg = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_PREVIEW_IMAGE_NULL);
                throw new ThirdPartyOpenApiException(msg);
            }
            if (orderDetailBaseOnCodeQry.getCount() == null) {
                throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdOrderErrorCode.T_ORDER_COUNT_NULL));
            }
            if (orderDetailBaseOnCodeQry.getCount() < 0) {
                throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdCommonErrorCode.T_NUMBER_ILLEGAL));
            }
        });
    }

    public static void main(String[] args) {
        try {
            System.out.println(11 / 0);
            throw new ThirdPartyOpenApiException("00");
        } catch (ThirdPartyOpenApiException e) {
            LOGGER.info("t");
            e.printStackTrace();
            throw new ThirdPartyOpenApiException("111");
        } catch (Exception e) {
            LOGGER.info("e");
            e.printStackTrace();
            throw new ThirdPartyOpenApiException("2222");
        }
    }

    public void validProvisionalOrder(ProvisionalOrderInfo provisionalOrderInfo, HttpServletRequest request) {
        String distributorId = request.getHeader(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID);
        String orderSource = request.getHeader(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE);
        LOGGER.info("提交临时订单到第三方系统，参数distributorId{}，orderSource{}，参数{}", distributorId, orderSource,
            JSON.toJSONString(provisionalOrderInfo));
        String message;
        if (StringUtils.isBlank(distributorId)) {
            message = MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_DISTRIBUTOR)
                + MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
            throw new ThirdPartyOpenApiException(message);
        }
        if (StringUtils.isBlank(orderSource)) {
            message = MessageUtils.get(ThirdOrderErrorCode.T_ORDER_SOURCE_NULL);
            throw new ThirdPartyOpenApiException(message);
        }
        provisionalOrderInfo.setOrderSource(orderSource);
        try {
            provisionalOrderInfo.setDistributorId(Integer.parseInt(distributorId));
        } catch (NumberFormatException e) {
            throw new ThirdPartyOpenApiException("请求头信息异常");
        }
        // 处理明细
        OrderDetailBaseOnIdQry orderDetailBaseOnIdQry = provisionalOrderInfo.getOrderDetail();

        if ("1410".equals(distributorId) && (orderDetailBaseOnIdQry.getExtendField() == null
            || StringUtils.isBlank(orderDetailBaseOnIdQry.getExtendField().getUrlParam()))) {
            throw new ThirdPartyOpenApiException("云南通信的扩展URL不能为空");
        }
        List<OrderDetailBaseOnIdQry> orderDetailBaseOnIdQryList = new ArrayList<>();
        orderDetailBaseOnIdQryList.add(orderDetailBaseOnIdQry);
        validOrderDetailList(orderDetailBaseOnIdQryList);

    }

    public void validOrderFromMoleji(MolejiOrderCreateCmd molejiOrderCreateCmd, OrderBusinessLogDO logDO) {
        String timestamp = String.valueOf(molejiOrderCreateCmd.getTimestamp());
        String sign = String.valueOf(molejiOrderCreateCmd.getSignature());

        // 签名取出来
        molejiOrderCreateCmd.setSignature(null);
        String jsonString = JSON.toJSONString(molejiOrderCreateCmd);

        // 将参数转义
        jsonString = jsonString.replace("caseArray", "case");
        // 2. 校验签名是否正确
        // String signStr = distributorIdStr +platformInfo.getAppKey() +orderSource +jsonString +timestamp;
        JSONObject jsonObject = JSON.parseObject(jsonString, Feature.OrderedField);
        String param = "";
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            if (entry.getKey().equals("case")) {
                JSONArray jsonArray = jsonObject.getJSONArray("case");
                param = param + entry.getKey() + "=[";
                if (jsonArray == null || jsonArray.size() == 0) {
                    throw new ThirdPartyOpenApiException(ThirdSkuOpenErrorConstant.MobileShellNullError.getMsg());
                }
                for (int x = 0; x < jsonArray.size(); x++) {
                    param += "{";
                    JSONObject caseObject = jsonArray.getJSONObject(x);
                    for (Map.Entry<String, Object> caseEntry : caseObject.entrySet()) {
                        param += caseEntry.getKey() + "=" + caseEntry.getValue() + ", ";
                    }
                    // 去掉最后的空格、后面还得加一个空格
                    param = param.substring(0, param.length() - 2) + "}, ";
                }
                // 移除逗号和空格
                param = param.substring(0, param.length() - 2);
                param += "]&";
            } else {
                param = param + entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        param = param.substring(0, param.length() - 1);
        LOGGER.info("订单同步：" + param);
        String checkSignature = MD5Utils
            .digest32(SHA256Utils.getSHA256(MolejiFactorySecret + timestamp).toLowerCase() + param).toLowerCase();
        LOGGER.info("sign：" + checkSignature);
        if (sign.equals(checkSignature)) {
            throw new ThirdPartyOpenApiException("签名信息错误");
        }
    }

    public void validOrderDetailFromMoleji(List<MolejiCaseCmd> caseCmdList) {
        if (caseCmdList == null || caseCmdList.size() == 0) {

        }
        for (int x = 0; x < caseCmdList.size(); x++) {
            MolejiCaseCmd molejiCaseCmd = caseCmdList.get(x);
            if (StringUtils.isBlank(molejiCaseCmd.getSku())) {
                // sku编码不能为空
                throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.T_SKU_NO_NULL);
            }
            if (molejiCaseCmd.getQuantity() == null) {
                throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.T_ORDER_COUNT_NULL);
            }
            if (molejiCaseCmd.getQuantity() < 1) {
                throw new ThirdPartyOpenApiException(ThirdCommonErrorCode.T_NUMBER_ILLEGAL);
            }
            if (StringUtils.isBlank(molejiCaseCmd.getPrevImgUrl())) {
                throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.T_ORDER_PREVIEW_IMAGE_NULL);
            }
            if (StringUtils.isBlank(molejiCaseCmd.getImgUrl())) {
                throw new ThirdPartyOpenApiException(ThirdOrderErrorCode.T_ORDER_GENERATE_IMAGE_NULL);
            }
        }
    }
}
