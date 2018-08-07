package com.bat.order.service.flexible.validator;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.model.api.ModelServiceRpc;
import com.bat.dubboapi.flexible.model.dto.ModelDTORpcQry;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.constant.FlexibleRpcConstant;
import com.bat.order.service.common.error.FlexibleRpcErrorCode;
import com.bat.order.service.common.error.OrderNameErrorCode;

@Component
public class ModelValidator {

    private static ModelServiceRpc modelServiceRpc;

    @DubboReference(check = false, retries = 0, timeout = 5000)
    public void setModelServiceRpc(ModelServiceRpc modelServiceRpc) {
        ModelValidator.modelServiceRpc = modelServiceRpc;
    }

    public static void validModel(ModelDTORpcQry modelDTORpcQry) {
        if (modelDTORpcQry == null) {
            return;
        }
        validOpenFlag(modelDTORpcQry.getOpenFlag());
        validIsAtLastTrademark(modelDTORpcQry.getAtLastTrademark());
    }

    public static void validOpenFlag(Short openFlag) {
        if (FlexibleRpcConstant.COMMON_OPEN_FLAG_NO.equals(openFlag)) {
            throw OrderException.buildException(MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_MODEL)
                + MessageUtils.get(FlexibleRpcErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE));
        }
    }

    /**
     * 检查是否最终的型号
     * 
     * @param atLastTrademark
     */
    public static void validIsAtLastTrademark(Short atLastTrademark) {
        if (FlexibleRpcConstant.AT_LAST_TRADEMARK_NO.equals(atLastTrademark)) {
            // 非最终的型号
            throw OrderException
                .buildException(MessageUtils.get(FlexibleRpcErrorCode.COMMON_AT_LAST_TRADEMARK_NOT_BELONG_YES)
                    + MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_MODEL));
        }
    }

    public static boolean validUsableByCart(ModelDTORpcQry modelDTORpcQry) {
        if (modelDTORpcQry == null) {
            return false;
        }
        if (FlexibleRpcConstant.COMMON_OPEN_FLAG_NO.equals(modelDTORpcQry.getOpenFlag())) {
            return false;
        }
        if (FlexibleRpcConstant.AT_LAST_TRADEMARK_NO.equals(modelDTORpcQry.getAtLastTrademark())) {
            // 非最终的材质
            return false;
        }
        if (Constant.COMMON_DEL_FLAG_YES.equals(modelDTORpcQry.getDelFlag())) {
            // 已删除
            return false;
        }
        return true;
    }

    /**
     * 根据型号id查询可用型号
     * 
     * @param modelId
     * @return
     */
    public static ModelDTORpcQry getUsableByModelId(Integer modelId) {
        Response<ModelDTORpcQry> response = modelServiceRpc.getByModelIdOrModelNo(modelId, null);
        ModelDTORpcQry modelDTORpcQry = response.getData();
        validModel(modelDTORpcQry);
        return modelDTORpcQry;
    }
}
