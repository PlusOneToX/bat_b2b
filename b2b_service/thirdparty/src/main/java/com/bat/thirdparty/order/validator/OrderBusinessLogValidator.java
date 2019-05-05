package com.bat.thirdparty.order.validator;

import com.alibaba.fastjson.JSONObject;
import com.bat.thirdparty.common.base.ThirdPartyException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdNameErrorCode;
import com.bat.thirdparty.common.error.log.OrderBusinessLogErrorCode;
import com.bat.thirdparty.common.error.order.ThirdOrderErrorCode;
import com.bat.thirdparty.common.order.OrderHeaderConstant;
import com.bat.thirdparty.common.util.MessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderBusinessLogValidator {

    /**
     * 校验请求体参数不能为空
     * @param requestBody
     */
    public static void validateRequestBody(String requestBody){
        if(StringUtils.isBlank(requestBody)){
            throw ThirdPartyException.buildException(OrderBusinessLogErrorCode.T_LOG_REQUEST_BODY_NULL);
        }
    }

    /**
     * 校验请求头参数不能为空
     * @param headers
     */
    public static void validateRequestHeaders(String headers){
        if(StringUtils.isBlank(headers)){
            throw ThirdPartyException.buildException(OrderBusinessLogErrorCode.T_LOG_REQUEST_HEADER_NULL);
        }
        JSONObject jsonObject = JSONObject.parseObject(headers);
        if(StringUtils.isBlank(jsonObject.getString(OrderHeaderConstant.HEADER_NAME_DISTRIBUTOR_ID))){
            String message= MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_DISTRIBUTOR)+MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL);
            throw  ThirdPartyException.buildException(message);
        }
        if(StringUtils.isBlank(jsonObject.getString(OrderHeaderConstant.HEADER_NAME_ORDER_SOURCE))){
            throw ThirdPartyException.buildException(ThirdOrderErrorCode.T_ORDER_SOURCE_NULL);
        }
    }
}
