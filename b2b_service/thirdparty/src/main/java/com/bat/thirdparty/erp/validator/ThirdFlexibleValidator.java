package com.bat.thirdparty.erp.validator;

import com.bat.thirdparty.common.base.ThirdPartyBaseException;
import com.bat.thirdparty.common.error.ThirdCommonErrorCode;
import com.bat.thirdparty.common.error.ThirdNameErrorCode;
import com.bat.thirdparty.common.error.flexible.exchange.ErpGoodsCustomInfoErrorConstant;
import com.bat.thirdparty.common.util.MessageUtils;
import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoBomCmd;
import com.bat.dubboapi.flexible.model.dto.ErpGoodsCustomInfoListCmd;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ThirdFlexibleValidator {

    /**
     * 校验ERP设置型号和材质关系是否缺货
     */
    public static void validateErpGoodsCustomInfoListCmd(ErpGoodsCustomInfoListCmd erpGoodsCustomInfoListCmd){
        List<ErpGoodsCustomInfoBomCmd> bomCmdList = erpGoodsCustomInfoListCmd.getBomCodeList();
        if(bomCmdList ==null || bomCmdList.size()==0){
            throw new ThirdPartyBaseException(ErpGoodsCustomInfoErrorConstant.BomCodeListNullError.getCode(), MessageUtils.get(ErpGoodsCustomInfoErrorConstant.BomCodeListNullError.getMsg()));
        }
        if(StringUtils.isBlank(erpGoodsCustomInfoListCmd.getOperateUserName())){
            throw new ThirdPartyBaseException(ErpGoodsCustomInfoErrorConstant.OperateUserNameNullError.getCode(),MessageUtils.get(ErpGoodsCustomInfoErrorConstant.OperateUserNameNullError.getMsg()));

        }
        for(int x=0;x<bomCmdList.size();x++){
            ErpGoodsCustomInfoBomCmd infoBomCmd = bomCmdList.get(x);
            if(StringUtils.isBlank(infoBomCmd.getBomCode())){
                throw new ThirdPartyBaseException(ErpGoodsCustomInfoErrorConstant.BomCodeNullError.getCode(), MessageUtils.get(ErpGoodsCustomInfoErrorConstant.BomCodeNullError.getMsg()));
            }
            if(infoBomCmd.getStockOutStatus()==null){
                throw new ThirdPartyBaseException(ErpGoodsCustomInfoErrorConstant.StockOutStatusNullError.getCode(), MessageUtils.get(ErpGoodsCustomInfoErrorConstant.StockOutStatusNullError.getMsg()));
            }
            if(StringUtils.isBlank(infoBomCmd.getItemCode())){
                throw new ThirdPartyBaseException(ErpGoodsCustomInfoErrorConstant.ItemCodeNullError.getCode(),MessageUtils.get(ThirdNameErrorCode.T_ERROR_NAME_ITEM)+MessageUtils.get(ThirdCommonErrorCode.COMMON_CODE_NULL));
            }
        }
    }
}
