package com.bat.order.service.flexible.validator;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.bat.dubboapi.flexible.common.Response;
import com.bat.dubboapi.flexible.material.api.MaterialServiceRpc;
import com.bat.dubboapi.flexible.material.dto.MaterialDTORpcQry;
import com.bat.order.api.common.exception.OrderException;
import com.bat.order.api.common.utils.MessageUtils;
import com.bat.order.service.common.Constant;
import com.bat.order.service.common.constant.FlexibleRpcConstant;
import com.bat.order.service.common.error.FlexibleRpcErrorCode;
import com.bat.order.service.common.error.OrderNameErrorCode;

@Component
public class MaterialValidator {

    private static MaterialServiceRpc materialServiceRpc;

    @DubboReference(check = false, retries = 0, timeout = 50000)
    public void setMaterialServiceRpc(MaterialServiceRpc materialServiceRpc) {
        MaterialValidator.materialServiceRpc = materialServiceRpc;
    }

    public static void validMaterial(MaterialDTORpcQry materialDTORpcQry) {
        if (materialDTORpcQry == null) {
            return;
        }
        validOpenFlag(materialDTORpcQry.getOpenFlag());
        validIsAtLastTrademark(materialDTORpcQry.getAtLastTrademark());
        ValidDelFlag(materialDTORpcQry.getDelFlag());

    }

    private static void ValidDelFlag(Short delFlag) {
        if (FlexibleRpcConstant.COMMON_DEL_FLAG_YES.equals(delFlag)) {
            throw OrderException.buildException(MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                + MessageUtils.get(FlexibleRpcErrorCode.FLEXIBLE_DATA_DEL_ALREADY));
        }
    }

    public static void validOpenFlag(Short openFlag) {
        if (FlexibleRpcConstant.COMMON_OPEN_FLAG_NO.equals(openFlag)) {
            throw OrderException.buildException(MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                + MessageUtils.get(FlexibleRpcErrorCode.COMMON_OPEN_FLAG_NO_FORBIN_USE));
        }
    }

    /**
     * 检查是否最终的材质
     * 
     * @param atLastTrademark
     */
    public static void validIsAtLastTrademark(Short atLastTrademark) {
        if (FlexibleRpcConstant.AT_LAST_TRADEMARK_NO.equals(atLastTrademark)) {
            // 非最终的材质
            throw OrderException
                .buildException(MessageUtils.get(FlexibleRpcErrorCode.COMMON_AT_LAST_TRADEMARK_NOT_BELONG_YES)
                    + MessageUtils.get(OrderNameErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL));
        }
    }

    /**
     * 检查购物的材质是否可用、不报错
     * 
     * @param materialDTORpcQry
     * @return
     */
    public static Boolean validUsableByCart(MaterialDTORpcQry materialDTORpcQry) {
        if (materialDTORpcQry == null) {
            return false;
        }
        if (FlexibleRpcConstant.COMMON_OPEN_FLAG_NO.equals(materialDTORpcQry.getOpenFlag())) {
            return false;
        }
        if (FlexibleRpcConstant.AT_LAST_TRADEMARK_NO.equals(materialDTORpcQry.getAtLastTrademark())) {
            // 非最终的材质
            return false;
        }
        if (Constant.COMMON_DEL_FLAG_YES.equals(materialDTORpcQry.getDelFlag())) {
            // 已删除
            return false;
        }
        return true;
    }

    /**
     * 根据材质id获取材质对象、不可用报错
     * 
     * @param materialId
     * @return
     */
    public static MaterialDTORpcQry getUsableByMaterialId(Integer materialId) {
        Response<MaterialDTORpcQry> response = materialServiceRpc.getByMaterialId(materialId);
        MaterialDTORpcQry materialDTORpcQry = response.getData();
        validMaterial(materialDTORpcQry);
        return materialDTORpcQry;
    }

    /**
     * 根据材质id获取材质对象、不判断状态是否可用
     * 
     * @param materialId
     * @return
     */
    public static MaterialDTORpcQry getByMaterialId(Integer materialId) {
        Response<MaterialDTORpcQry> response = materialServiceRpc.getByMaterialId(materialId);
        MaterialDTORpcQry materialDTORpcQry = response.getData();
        validIsAtLastTrademark(materialDTORpcQry.getAtLastTrademark());
        ValidDelFlag(materialDTORpcQry.getDelFlag());
        return materialDTORpcQry;
    }

}
