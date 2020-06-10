package com.bat.flexible.manager.label.validator;

import com.bat.flexible.api.util.MessageUtils;
import com.bat.flexible.api.FlexibleDubboApiException;
import com.bat.dubboapi.flexible.label.dto.OrderGoodsDiySimpleDTO;
import com.bat.dubboapi.flexible.label.dto.OrderLableCmd;
import com.bat.flexible.manager.error.common.FlexibleCommonErrorCode;
import com.bat.flexible.manager.error.model.ModelErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class LabelValidator {

    /**
     * 校验定制订单标签
     * @param orderLableCmd
     */
    public static void validaOrderCreateLabel(OrderLableCmd orderLableCmd){
        if(orderLableCmd.getOrderId() ==null){
            throw FlexibleDubboApiException.buildException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_ORDER)
                    +MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL));
        }
        if(orderLableCmd.getDistributorId() ==null){
            throw FlexibleDubboApiException.buildException(FlexibleCommonErrorCode.COMMON_DISTRIBUTOR_ID_NULL);
        }
        List<OrderGoodsDiySimpleDTO> diySimpleDTOList = orderLableCmd.getDiySimpleDTOList();
        if(diySimpleDTOList ==null || diySimpleDTOList.size() ==0){
            throw FlexibleDubboApiException.buildException("定制明细列表不能为空");
        }
        diySimpleDTOList.stream().forEach(diySimpleDTO -> {
            if(diySimpleDTO.getId() ==null){
                throw FlexibleDubboApiException.buildException(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL);
            }
            if (diySimpleDTO.getCategoryId() ==null){
                throw FlexibleDubboApiException.buildException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_PRODUCT_CATEGORY)
                        +MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL));
            }
            if(diySimpleDTO.getMaterialId() ==null){
                throw FlexibleDubboApiException.buildException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MATERIAL)
                        +MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL)
                );
            }
            if(diySimpleDTO.getModelId() ==null){
                throw FlexibleDubboApiException.buildException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_MODEL)
                        +MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL)
                );
            }
            if(diySimpleDTO.getPictureId() ==null){
                throw FlexibleDubboApiException.buildException(MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ERROR_NAME_PICTURE)
                        +MessageUtils.get(FlexibleCommonErrorCode.FLEXIBLE_ID_NULL)
                );
            }
            if(StringUtils.isBlank(diySimpleDTO.getModelName())){
                throw FlexibleDubboApiException.buildException(ModelErrorCode.M_MODEL_NAME_NULL);
            }
        });
    }

}
