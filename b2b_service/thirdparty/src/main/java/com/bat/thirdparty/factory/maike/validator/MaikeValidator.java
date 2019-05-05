package com.bat.thirdparty.factory.maike.validator;

import com.bat.dubboapi.thirdparty.maike.dto.order.OrderDetailLocalUploadCmd;
import com.bat.thirdparty.common.base.ThirdPartyOpenApiException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdNameErrorCode;
import com.bat.thirdparty.common.util.MessageUtils;

import java.util.List;

public class MaikeValidator {


    /**
     * 校验
     * @param uploadCmdList
     */
    public static void validateFtpParam(List<OrderDetailLocalUploadCmd> uploadCmdList){
        if(uploadCmdList ==null || uploadCmdList.size()==0){
            throw new ThirdPartyOpenApiException(ThirdCommonErrorCode.COMMON_LIST_NULL);
        }
        uploadCmdList.stream().forEach(orderDetailLocalUploadCmd -> {
            if(orderDetailLocalUploadCmd.getOrderGoodsDiyId() ==null){
                throw new ThirdPartyOpenApiException(MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_ORDER_GOODS_DIY)+MessageUtils.get(ThirdCommonErrorCode.COMMON_ID_NULL));
            }
            if(orderDetailLocalUploadCmd.getUrlList()==null || orderDetailLocalUploadCmd.getUrlList().size()==0){
                throw new ThirdPartyOpenApiException(ThirdCommonErrorCode.COMMON_LIST_NULL);
            }
        });
    }
}
